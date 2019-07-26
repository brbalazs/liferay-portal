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

import com.liferay.commerce.machine.learning.internal.forecast.api.CommerceForecastField;
import com.liferay.commerce.machine.learning.internal.forecast.api.ForecastPeriod;
import com.liferay.commerce.machine.learning.internal.forecast.model.Forecast;
import com.liferay.commerce.machine.learning.internal.search.api.CommerceIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseCommerceForecastServiceImpl<T extends Forecast> {

	protected T getBaseForecastModel(T forecast, Document document) {
		forecast.setActual(
			GetterUtil.getFloat(document.get(CommerceForecastField.ACTUAL)));

		forecast.setCompanyId(
			GetterUtil.getLong(document.get(Field.COMPANY_ID)));

		forecast.setForecast(
			GetterUtil.getFloat(document.get(CommerceForecastField.FORECAST)));

		forecast.setForecastId(
			GetterUtil.getLong(
				document.get(CommerceForecastField.FORECAST_ID)));

		forecast.setForecastLowerBound(
			GetterUtil.getFloat(
				document.get(CommerceForecastField.FORECAST_LOWER_BOUND)));

		forecast.setForecastUpperBound(
			GetterUtil.getFloat(
				document.get(CommerceForecastField.FORECAST_UPPER_BOUND)));

		forecast.setJobId(document.get(CommerceForecastField.JOB_ID));

		forecast.setLevel(document.get(CommerceForecastField.LEVEL));

		forecast.setPeriod(document.get(CommerceForecastField.PERIOD));

		forecast.setTarget(document.get(CommerceForecastField.TARGET));

		try {
			forecast.setTimestamp(
				document.getDate(CommerceForecastField.TIMESTAMP));
		}
		catch (ParseException pe) {
		}

		return forecast;
	}

	protected BooleanQuery getBaseQuery(
			String level, String period, String target, Date startDate,
			Date endDate)
		throws com.liferay.portal.kernel.search.ParseException {

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		TermQueryImpl levelTermQuery = new TermQueryImpl(
			CommerceForecastField.LEVEL, level);

		booleanQuery.add(levelTermQuery, BooleanClauseOccur.MUST);

		TermQueryImpl periodTermQuery = new TermQueryImpl(
			CommerceForecastField.PERIOD, period);

		booleanQuery.add(periodTermQuery, BooleanClauseOccur.MUST);

		TermQueryImpl targetTermQuery = new TermQueryImpl(
			CommerceForecastField.TARGET, target);

		booleanQuery.add(targetTermQuery, BooleanClauseOccur.MUST);

		TermRangeQuery termRangeQuery = new TermRangeQueryImpl(
			CommerceForecastField.TIMESTAMP, _formatSearchDate(startDate),
			_formatSearchDate(endDate), true, true);

		booleanQuery.add(termRangeQuery, BooleanClauseOccur.MUST);

		return booleanQuery;
	}

	protected Date getEndDate(ForecastPeriod forecastPeriod) {
		LocalDateTime endDate = LocalDateTime.now(ZoneOffset.UTC);

		if (forecastPeriod.equals(ForecastPeriod.MONTH)) {
			endDate = endDate.minusMonths(_DEFAULT_FORECAST_POINTS);
		}
		else {
			endDate = endDate.minusWeeks(_DEFAULT_FORECAST_POINTS);
		}

		return Date.from(endDate.toInstant(ZoneOffset.UTC));
	}

	protected List<T> getSearchResults(
		long companyId, Query query, Date startDate, Date endDate) {

		SearchSearchRequest searchRequest = _getSearchRequest(
			forecastCommerceIndexer.getIndexName(companyId), query, startDate,
			endDate);

		SearchSearchResponse searchSearchResponse = searchEngineAdapter.execute(
			searchRequest);

		return _getForecastList(searchSearchResponse.getHits());
	}

	protected Date getStartDate(Date startDate, ForecastPeriod forecastPeriod) {
		Instant instant = startDate.toInstant();

		OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.UTC);

		if (forecastPeriod.equals(ForecastPeriod.MONTH)) {
			offsetDateTime = offsetDateTime.minusMonths(
				_DEFAULT_FORECAST_HISTORY);
		}
		else {
			offsetDateTime = offsetDateTime.minusWeeks(
				_DEFAULT_FORECAST_HISTORY);
		}

		return Date.from(offsetDateTime.toInstant());
	}

	protected abstract T toForecastModel(Document document);

	@Reference(
		target = "(component.name=com.liferay.commerce.machine.learning.internal.forecast.search.index.ForecastCommerceIndexer)"
	)
	protected volatile CommerceIndexer forecastCommerceIndexer;

	@Reference
	protected volatile SearchEngineAdapter searchEngineAdapter;

	private String _formatSearchDate(Date searchDate) {
		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			_INDEX_DATE_FORMAT_PATTERN);

		return dateFormat.format(searchDate);
	}

	private List<T> _getForecastList(Hits hits) {
		List<Document> documents = hits.toList();

		Stream<Document> documentsStream = documents.stream();

		return documentsStream.map(
			this::toForecastModel
		).collect(
			Collectors.toList()
		);
	}

	private SearchSearchRequest _getSearchRequest(
		String indexName, Query query, Date startDate, Date endDate) {

		SearchSearchRequest searchRequest = new SearchSearchRequest();

		searchRequest.setIndexNames(new String[] {indexName});

		searchRequest.setQuery(query);

		searchRequest.setSize(_getSearchSize(startDate, endDate));

		Sort sort = SortFactoryUtil.create(
			_getSortableFieldName(CommerceForecastField.TIMESTAMP), false);

		searchRequest.setSorts(new Sort[] {sort});

		searchRequest.setStats(Collections.emptyMap());

		return searchRequest;
	}

	private int _getSearchSize(Date startDate, Date endDate) {
		Instant startDateInstant = startDate.toInstant();

		OffsetDateTime startOffsetDateTime = startDateInstant.atOffset(
			ZoneOffset.UTC);

		Instant endDateInstant = endDate.toInstant();

		OffsetDateTime endOffsetDateTime = endDateInstant.atOffset(
			ZoneOffset.UTC);

		long weeksBetween = ChronoUnit.MONTHS.between(
			startOffsetDateTime, endOffsetDateTime);

		return (int)weeksBetween + 1;
	}

	private String _getSortableFieldName(String fieldName) {
		return fieldName.concat(_SORTABLE_FIELD_SUFFIX);
	}

	private static final int _DEFAULT_FORECAST_HISTORY = 8;

	private static final int _DEFAULT_FORECAST_POINTS = 3;

	private static final String _INDEX_DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

	private static final String _SORTABLE_FIELD_SUFFIX = "_sortable";

}