/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.repository.PageAssetMetricRepository;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.RecentPage;
import com.liferay.osb.asah.common.model.TimeRange;

import java.math.BigDecimal;

import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Marcellus Tavares
 */
@Repository("PageAssetMetricRepository")
public class PageAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<PageMetric>
	implements PageAssetMetricRepository {

	@Override
	public AssetType getAssetType() {
		return AssetType.PAGE;
	}

	@Override
	public List<HistogramMetric> getExperimentHistogramMetrics(
		Long experimentId, PageMetricType pageMetricType, TimeRange timeRange,
		@Nullable String variantId) {

		Field field = DSL.timestamp(
			dslHelper.dateTrunc(
				DatePart.DAY,
				dslHelper.getDateAtTimeZoneField(
					"eventdate", timeZoneDog.getTimeZoneId())));

		field = field.as("key");

		SelectJoinStep<Record> selectJoinStep = dslContext.select(
			field, getMetricFieldAliased(pageMetricType, timeRange)
		).from(
			getTableName(timeRange)
		);

		return queryExecutor.queryForList(
			rowMap -> {
				Metric metric = new Metric(pageMetricType);

				BigDecimal bigDecimal = (BigDecimal)rowMap.get(
					pageMetricType.getName());

				if (bigDecimal == null) {
					bigDecimal = BigDecimal.ZERO;
				}

				metric.setValue(bigDecimal.doubleValue());

				return new HistogramMetric(
					String.valueOf(
						DateUtil.toLocalDateTime(
							(Date)rowMap.get("key"), ZoneOffset.UTC)),
					metric);
			},
			selectJoinStep.where(
				_createWhereClauseCondition(experimentId, timeRange, variantId)
			).groupBy(
				field
			));
	}

	@Override
	public Optional<PageMetric> getExperimentPageMetric(
		Long experimentId, Set<PageMetricType> pageMetricTypes,
		TimeRange timeRange, String variantId) {

		SelectJoinStep<Record> selectJoinStep = dslContext.select(
			_getMetricFields(pageMetricTypes, timeRange)
		).from(
			getTableName(timeRange)
		);

		ZoneId zoneId = timeZoneDog.getZoneId();

		return queryExecutor.queryForObject(
			recordMap -> _toPageMetric(pageMetricTypes, recordMap),
			selectJoinStep.where(
				DSL.field(
					"experimentId", Long.class
				).eq(
					experimentId
				),
				DSL.field(
					"eventDate"
				).between(
					dslHelper.getDateParam(
						timeRange.getStartLocalDateTime(), zoneId.toString()),
					dslHelper.getDateParam(
						timeRange.getEndLocalDateTime(), zoneId.toString())
				),
				DSL.field(
					"variantId"
				).eq(
					variantId
				)));
	}

	@Override
	public Optional<PageMetric> getExperimentPageMetric(
		String canonicalUrl, Set<PageMetricType> pageMetricTypes,
		TimeRange timeRange) {

		SelectJoinStep<Record> selectJoinStep = dslContext.select(
			_getMetricFields(pageMetricTypes, timeRange)
		).from(
			getTableName(timeRange)
		);

		ZoneId zoneId = timeZoneDog.getZoneId();

		return queryExecutor.queryForObject(
			recordMap -> _toPageMetric(pageMetricTypes, recordMap),
			selectJoinStep.where(
				DSL.field(
					"canonicalUrl"
				).eq(
					canonicalUrl
				),
				DSL.field(
					"eventDate"
				).between(
					dslHelper.getDateParam(
						timeRange.getStartLocalDateTime(), zoneId.toString()),
					dslHelper.getDateParam(
						timeRange.getEndLocalDateTime(), zoneId.toString())
				)));
	}

	@Override
	public List<RecentPage> getRecentPages(
		@Nullable String displayLanguageId, String individualId,
		Pageable pageable, TimeRange timeRange) {

		Field<Date> eventDateField = DSL.field("eventDate", Date.class);

		SelectHavingStep selectHavingStep = _getRecentPagesSelectHavingStep(
			displayLanguageId, individualId,
			dslContext.select(
				DSL.min(
					eventDateField
				).as(
					"createDate"
				),
				DSL.sum(
					DSL.field("views", Long.class)
				).cast(
					BigDecimal.class
				).as(
					"counts"
				),
				DSL.field(
					"contentLanguageId", String.class
				).as(
					"displayLanguageId"
				),
				DSL.max(
					eventDateField
				).as(
					"lastModifiedDate"
				),
				DSL.field(
					"canonicalUrl", String.class
				).as(
					"url"
				)),
			timeRange);

		return queryExecutor.queryForList(
			RecentPage::new,
			selectHavingStep.orderBy(
				_getSortFields(pageable.getSort())
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public Long getRecentPagesCount(
		@Nullable String displayLanguageId, String individualId,
		TimeRange timeRange) {

		return queryExecutor.queryForLong(
			dslContext.with(
				"RecentPages"
			).as(
				_getRecentPagesSelectHavingStep(
					displayLanguageId, individualId,
					dslContext.select(
						DSL.field("contentLanguageId"),
						DSL.field("canonicalUrl")),
					timeRange)
			).selectCount(
			).from(
				"RecentPages"
			));
	}

	@Override
	public Long getUniqueSessionsCount(Long experimentId, TimeRange timeRange) {
		ZoneId zoneId = timeZoneDog.getZoneId();

		return queryExecutor.queryForLong(
			dslContext.select(
				DSL.countDistinct(DSL.field("sessionId"))
			).from(
				getTableName(timeRange)
			).where(
				DSL.field(
					"eventDate"
				).between(
					dslHelper.getDateParam(
						timeRange.getStartLocalDateTime(), zoneId.toString()),
					dslHelper.getDateParam(
						timeRange.getEndLocalDateTime(), zoneId.toString())
				),
				DSL.field(
					"experimentId", Long.class
				).eq(
					experimentId
				)
			));
	}

	@Override
	public Long getVariantUniqueVisitors(
		Long experimentId, TimeRange timeRange, String variantId) {

		ZoneId zoneId = timeZoneDog.getZoneId();

		return queryExecutor.queryForLong(
			dslContext.select(
				DSL.countDistinct(
					DSL.coalesce(
						DSL.field("Individual.id"),
						DSL.field("PageDaily.userId")))
			).from(
				getTableName(timeRange)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"PageDaily.userId"
				).eq(
					DSL.field("Identity.id")
				)
			).leftJoin(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).on(
				DSL.and(
					DSL.field(
						"Individual.id"
					).eq(
						DSL.field("Identity.individualId")
					),
					DSL.or(
						DSL.field(
							"Individual.suppressed"
						).isNull(),
						DSL.field(
							"Individual.suppressed"
						).notEqual(
							DSL.val(Boolean.TRUE)
						)))
			).where(
				DSL.field(
					"eventDate"
				).between(
					dslHelper.getDateParam(
						timeRange.getStartLocalDateTime(), zoneId.toString()),
					dslHelper.getDateParam(
						timeRange.getEndLocalDateTime(), zoneId.toString())
				),
				DSL.field(
					"experimentId", Long.class
				).eq(
					experimentId
				),
				DSL.field(
					"variantId"
				).eq(
					variantId
				)
			));
	}

	@Override
	protected PageMetric createAssetMetric() {
		return new PageMetric();
	}

	@Override
	protected String getAssetIdFieldName() {
		return "canonicalurl";
	}

	@Override
	protected SelectJoinStep<Record> getAssetMetricSelectJoinStep(
		SelectSelectStep<Record> selectSelectStep, TimeRange timeRange) {

		return selectSelectStep.from(
			DSL.table(
				getTableName(timeRange)
			).as(
				"metric"
			)
		).leftJoin(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			)
		).on(
			DSL.field(
				"Identity.id"
			).eq(
				DSL.field("metric.userId")
			)
		).leftJoin(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).on(
			DSL.and(
				DSL.field(
					"Individual.id"
				).eq(
					DSL.field("Identity.individualId")
				),
				DSL.or(
					DSL.field(
						"Individual.suppressed"
					).isNull(),
					DSL.field(
						"Individual.suppressed"
					).notEqual(
						DSL.val(Boolean.TRUE)
					)))
		);
	}

	@Override
	protected Map<String, BiConsumer<PageMetric, Metric>>
		getAssetMetricSetters() {

		return new HashMap<String, BiConsumer<PageMetric, Metric>>() {
			{
				put(
					PageMetricType.AVG_TIME_ON_PAGE.getName(),
					PageMetric::setAvgTimeOnPageMetric);
				put(
					PageMetricType.BOUNCE.getName(),
					PageMetric::setBounceMetric);
				put(
					PageMetricType.BOUNCE_RATE.getName(),
					PageMetric::setBounceRateMetric);
				put(
					PageMetricType.CTA_CLICKS.getName(),
					PageMetric::setCTAClicksMetric);
				put(
					PageMetricType.DIRECT_ACCESS.getName(),
					PageMetric::setDirectAccessMetric);
				put(
					PageMetricType.ENTRANCES.getName(),
					PageMetric::setEntrancesMetric);
				put(
					PageMetricType.EXIT_RATE.getName(),
					PageMetric::setExitRateMetric);
				put(
					PageMetricType.INDIRECT_ACCESS.getName(),
					PageMetric::setIndirectAccessMetric);
				put(PageMetricType.READS.getName(), PageMetric::setReadsMetric);
				put(
					PageMetricType.SESSIONS.getName(),
					PageMetric::setSessionsMetric);
				put(
					PageMetricType.TIME_ON_PAGE.getName(),
					PageMetric::setTimeOnPageMetric);
				put(PageMetricType.VIEWS.getName(), PageMetric::setViewsMetric);
				put(
					PageMetricType.VISITORS.getName(),
					PageMetric::setVisitorsMetric);
			}
		};
	}

	@Override
	protected String getAssetTitleFieldName() {
		return "title";
	}

	@Override
	protected Condition getDescriptionLikeCondition(String terms) {
		return DSL.lower(
			DSL.trim(
				DSL.replace(DSL.field("description", String.class), "\n", ""))
		).like(
			StringUtils.wrap(StringUtils.lowerCase(terms), "%")
		);
	}

	@Override
	protected Condition getKeywordSearchCondition(String keywords) {
		return DSL.or(
			DSL.lower(
				DSL.field(getAssetTitleFieldName(), String.class)
			).like(
				StringUtils.wrap(StringUtils.lowerCase(keywords), "%")
			),
			DSL.lower(
				DSL.field("canonicalUrl", String.class)
			).like(
				StringUtils.wrap(StringUtils.lowerCase(keywords), "%")
			));
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		if ((metricType == PageMetricType.AVG_TIME_ON_PAGE) ||
			(metricType == PageMetricType.BOUNCE_RATE) ||
			(metricType == PageMetricType.EXIT_RATE)) {

			return DSL.coalesce(
				DSL.sum(
					DSL.field(metricType.getFieldName(), Float.class)
				).div(
					DSL.nullif(DSL.countDistinct(DSL.field("sessionId")), 0)
				),
				BigDecimal.ZERO);
		}

		if (metricType == PageMetricType.SESSIONS) {
			return DSL.cast(
				DSL.countDistinct(DSL.field("sessionId")), BigDecimal.class);
		}

		if (metricType == PageMetricType.VISITORS) {
			return DSL.cast(
				DSL.countDistinct(
					DSL.coalesce(
						DSL.field("Individual.id"),
						DSL.field("metric.userId"))),
				BigDecimal.class);
		}

		Field<Long> longField = DSL.field(
			metricType.getFieldName(), Long.class);

		return DSL.sum(longField);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return PageMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return PageMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if (!dslHelper.isBigQueryDialect()) {
			return "BQPage";
		}

		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "PageHourly";
		}

		return "PageDaily";
	}

	private List<Condition> _createWhereClauseCondition(
		Long experimentId, TimeRange timeRange, @Nullable String variantId) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"experimentId", Long.class
			).eq(
				experimentId
			));

		ZoneId zoneId = timeZoneDog.getZoneId();

		conditions.add(
			DSL.field(
				"eventDate"
			).between(
				dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(), zoneId.toString()),
				dslHelper.getDateParam(
					timeRange.getEndLocalDateTime(), zoneId.toString())
			));

		if (variantId != null) {
			conditions.add(
				DSL.field(
					"variantId"
				).eq(
					variantId
				));
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		@Nullable String displayLanguageId, String individualId,
		TimeRange timeRange) {

		ZoneId zoneId = timeZoneDog.getZoneId();

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"eventDate"
			).between(
				dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(), zoneId.toString()),
				dslHelper.getDateParam(
					timeRange.getEndLocalDateTime(), zoneId.toString())
			));
		conditions.add(
			DSL.field(
				"Identity.individualId"
			).eq(
				individualId
			));

		if (StringUtils.isNotBlank(displayLanguageId)) {
			conditions.add(
				DSL.field(
					"contentLanguageId"
				).eq(
					displayLanguageId
				));
		}

		return conditions;
	}

	private List<Field<BigDecimal>> _getMetricFields(
		Set<PageMetricType> pageMetricTypes, TimeRange timeRange) {

		Stream<PageMetricType> stream = pageMetricTypes.stream();

		return stream.map(
			metricName -> getMetricFieldAliased(metricName, timeRange)
		).collect(
			Collectors.toList()
		);
	}

	private SelectHavingStep _getRecentPagesSelectHavingStep(
		String displayLanguageId, String individualId,
		SelectSelectStep selectSelectStep, TimeRange timeRange) {

		return selectSelectStep.from(
			getTableName(timeRange)
		).join(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			)
		).on(
			DSL.field(
				getTableName(timeRange) + ".userId"
			).eq(
				DSL.field("Identity.id")
			)
		).leftJoin(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).on(
			DSL.and(
				DSL.field(
					"Individual.id"
				).eq(
					DSL.field("Identity.individualId")
				),
				DSL.or(
					DSL.field(
						"Individual.suppressed"
					).isNull(),
					DSL.field(
						"Individual.suppressed"
					).notEqual(
						DSL.val(Boolean.TRUE)
					)))
		).where(
			_getConditions(displayLanguageId, individualId, timeRange)
		).groupBy(
			DSL.field("contentLanguageId"), DSL.field("canonicalUrl")
		);
	}

	private Collection<SortField<?>> _getSortFields(Sort sort) {
		Collection<SortField<?>> sortFields = new ArrayList<>();

		List<Sort.Order> sortOrders = new ArrayList<>();

		if (sort != null) {
			sortOrders = sort.toList();
		}

		if (sortOrders.isEmpty()) {
			sortFields.add(
				DSL.field(
					"counts"
				).desc());

			return sortFields;
		}

		for (Sort.Order sortOrder : sortOrders) {
			String fieldName = sortOrder.getProperty();

			Field<?> field = DSL.field(fieldName);

			if (sortOrder.getDirection() == Sort.Direction.ASC) {
				sortFields.add(field.asc());
			}
			else {
				sortFields.add(field.desc());
			}
		}

		return sortFields;
	}

	private PageMetric _toPageMetric(
		Set<PageMetricType> pageMetricTypes, Map<String, Object> recordMap) {

		PageMetric pageMetric = new PageMetric();

		Map<String, BiConsumer<PageMetric, Metric>> assetMetricSetters =
			getAssetMetricSetters();

		for (PageMetricType pageMetricType : pageMetricTypes) {
			Metric metric = new Metric(pageMetricType);

			BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
				pageMetricType.getName());

			metric.setValue(metricValueBigDecimal.doubleValue());

			BiConsumer<PageMetric, Metric> assetMetricSetter =
				assetMetricSetters.get(pageMetricType.getName());

			assetMetricSetter.accept(pageMetric, metric);
		}

		return pageMetric;
	}

}