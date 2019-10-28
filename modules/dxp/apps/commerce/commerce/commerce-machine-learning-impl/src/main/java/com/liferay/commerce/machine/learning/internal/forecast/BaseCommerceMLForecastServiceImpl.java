/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.machine.learning.internal.forecast;

import com.liferay.commerce.machine.learning.forecast.model.CommerceMLForecast;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastField;
import com.liferay.commerce.machine.learning.internal.forecast.constants.CommerceMLForecastPeriod;
import com.liferay.commerce.machine.learning.internal.search.api.CommerceMLIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermRangeQueryImpl;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;

import java.text.DateFormat;
import java.text.ParseException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseCommerceMLForecastServiceImpl
	<T extends CommerceMLForecast> {

	protected BooleanFilter getBaseBooleanFilter(
		String scope, String period, String target) {

		BooleanFilter booleanFilter = new BooleanFilter();

		TermFilter scopeTermFilter = new TermFilter(
			CommerceMLForecastField.SCOPE, scope);

		booleanFilter.add(scopeTermFilter, BooleanClauseOccur.MUST);

		TermFilter periodTermFilter = new TermFilter(
			CommerceMLForecastField.PERIOD, period);

		booleanFilter.add(periodTermFilter, BooleanClauseOccur.MUST);

		TermFilter targetTermFilter = new TermFilter(
			CommerceMLForecastField.TARGET, target);

		booleanFilter.add(targetTermFilter, BooleanClauseOccur.MUST);

		return booleanFilter;
	}

	protected T getBaseCommerceMLForecastModel(
		T commerceMLForecast, Document document) {

		commerceMLForecast.setActual(
			GetterUtil.getFloat(
				document.get(CommerceMLForecastField.ACTUAL), Float.MIN_VALUE));

		commerceMLForecast.setCompanyId(
			GetterUtil.getLong(document.get(Field.COMPANY_ID)));

		commerceMLForecast.setForecast(
			GetterUtil.getFloat(
				document.get(CommerceMLForecastField.FORECAST),
				Float.MIN_VALUE));

		commerceMLForecast.setForecastId(
			GetterUtil.getLong(
				document.get(CommerceMLForecastField.FORECAST_ID)));

		commerceMLForecast.setForecastLowerBound(
			GetterUtil.getFloat(
				document.get(CommerceMLForecastField.FORECAST_LOWER_BOUND)));

		commerceMLForecast.setForecastUpperBound(
			GetterUtil.getFloat(
				document.get(CommerceMLForecastField.FORECAST_UPPER_BOUND)));

		commerceMLForecast.setJobId(
			document.get(CommerceMLForecastField.JOB_ID));

		commerceMLForecast.setScope(
			document.get(CommerceMLForecastField.SCOPE));

		commerceMLForecast.setPeriod(
			document.get(CommerceMLForecastField.PERIOD));

		commerceMLForecast.setTarget(
			document.get(CommerceMLForecastField.TARGET));

		try {
			commerceMLForecast.setTimestamp(
				document.getDate(CommerceMLForecastField.TIMESTAMP));
		}
		catch (ParseException pe) {
		}

		return commerceMLForecast;
	}

	protected BooleanQuery getBaseQuery(
		String scope, String period, String target) {

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		BooleanFilter baseBooleanFilter = getBaseBooleanFilter(
			scope, period, target);

		booleanQuery.setPreBooleanFilter(baseBooleanFilter);

		return booleanQuery;
	}

	protected BooleanQuery getBaseQuery(
			String scope, String period, String target, Date startDate,
			Date endDate)
		throws com.liferay.portal.kernel.search.ParseException {

		BooleanQuery booleanQuery = getBaseQuery(scope, period, target);

		TermRangeQuery termRangeQuery = new TermRangeQueryImpl(
			CommerceMLForecastField.TIMESTAMP, _formatSearchDate(startDate),
			_formatSearchDate(endDate), true, true);

		booleanQuery.add(termRangeQuery, BooleanClauseOccur.MUST);

		return booleanQuery;
	}

	protected Date getEndDate(
		Date endDate, CommerceMLForecastPeriod commerceMLForecastPeriod,
		int stepCount) {

		Instant endDateInstant = endDate.toInstant();

		LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(
			endDateInstant, DEFAULT_ZONE_OFFSET);

		endLocalDateTime = endLocalDateTime.truncatedTo(ChronoUnit.DAYS);

		if (commerceMLForecastPeriod.equals(CommerceMLForecastPeriod.MONTH)) {
			endLocalDateTime = endLocalDateTime.with(
				ChronoField.DAY_OF_MONTH, 1);

			endLocalDateTime = endLocalDateTime.plusMonths(stepCount);
		}
		else {
			endLocalDateTime = endLocalDateTime.with(
				ChronoField.DAY_OF_WEEK, 1);

			endLocalDateTime = endLocalDateTime.plusWeeks(stepCount);
		}

		return _toDate(endLocalDateTime);
	}

	protected List<T> getForecastList(Hits hits) {
		List<Document> documents = _getDocumentList(hits);

		Stream<Document> documentsStream = documents.stream();

		return documentsStream.map(
			this::toForecastModel
		).collect(
			Collectors.toList()
		);
	}

	protected SearchSearchRequest getSearchRequest(
		String indexName, Query query, int start, int size, boolean reverse) {

		SearchSearchRequest searchRequest = new SearchSearchRequest();

		searchRequest.setIndexNames(new String[] {indexName});

		searchRequest.setQuery(query);

		searchRequest.setStart(start);

		searchRequest.setSize(size);

		Sort sort = SortFactoryUtil.create(
			CommerceMLForecastField.TIMESTAMP.concat(SORTABLE_FIELD_SUFFIX),
			reverse);

		searchRequest.setSorts(new Sort[] {sort});

		searchRequest.setStats(Collections.emptyMap());

		return searchRequest;
	}

	protected List<T> getSearchResults(
		SearchSearchRequest searchSearchRequest) {

		SearchSearchResponse searchSearchResponse = searchEngineAdapter.execute(
			searchSearchRequest);

		return getForecastList(searchSearchResponse.getHits());
	}

	protected int getSearchSize(
		Date startDate, Date endDate,
		CommerceMLForecastPeriod commerceMLForecastPeriod) {

		Instant startDateInstant = startDate.toInstant();

		LocalDateTime startLocalDate = LocalDateTime.ofInstant(
			startDateInstant, DEFAULT_ZONE_OFFSET);

		Instant endDateInstant = endDate.toInstant();

		LocalDateTime endLocalDate = LocalDateTime.ofInstant(
			endDateInstant, DEFAULT_ZONE_OFFSET);

		int steps = 1;

		if (commerceMLForecastPeriod.equals(CommerceMLForecastPeriod.MONTH)) {
			steps += ChronoUnit.MONTHS.between(startLocalDate, endLocalDate);
		}
		else {
			steps += ChronoUnit.WEEKS.between(startLocalDate, endLocalDate);
		}

		return steps;
	}

	protected Date getStartDate(
		Date startDate, CommerceMLForecastPeriod commerceMLForecastPeriod,
		int stepCount) {

		Instant startDateInstant = startDate.toInstant();

		LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(
			startDateInstant, DEFAULT_ZONE_OFFSET);

		startLocalDateTime = startLocalDateTime.truncatedTo(ChronoUnit.DAYS);

		if (commerceMLForecastPeriod.equals(CommerceMLForecastPeriod.MONTH)) {
			startLocalDateTime = startLocalDateTime.with(
				ChronoField.DAY_OF_MONTH, 1);

			startLocalDateTime = startLocalDateTime.minusMonths(stepCount);
		}
		else {
			startLocalDateTime = startLocalDateTime.with(
				ChronoField.DAY_OF_WEEK, 1);

			startLocalDateTime = startLocalDateTime.minusWeeks(stepCount);
		}

		return _toDate(startLocalDateTime);
	}

	protected abstract T toForecastModel(Document document);

	protected static final int DEFAULT_FORECAST_LENGTH = 3;

	protected static final int DEFAULT_HISTORY_LENGTH = 8;

	protected static final ZoneId DEFAULT_ZONE_OFFSET =
		ZoneOffset.systemDefault();

	protected static final String SORTABLE_FIELD_SUFFIX = "_sortable";

	@Reference(
		target = "(component.name=com.liferay.commerce.machine.learning.internal.forecast.search.index.CommerceMLForecastIndexer)"
	)
	protected volatile CommerceMLIndexer commerceMLIndexer;

	@Reference
	protected volatile SearchEngineAdapter searchEngineAdapter;

	private String _formatSearchDate(Date searchDate) {
		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			_INDEX_DATE_FORMAT_PATTERN);

		return dateFormat.format(searchDate);
	}

	private List<Document> _getDocumentList(Hits hits) {
		List<Document> list = new ArrayList<>(hits.toList());

		Map<String, Hits> groupedHits = hits.getGroupedHits();

		for (Map.Entry<String, Hits> entry : groupedHits.entrySet()) {
			list.addAll(_getDocumentList(entry.getValue()));
		}

		return list;
	}

	private Date _toDate(LocalDateTime localDateTime) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(DEFAULT_ZONE_OFFSET);

		return Date.from(zonedDateTime.toInstant());
	}

	private static final String _INDEX_DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

}