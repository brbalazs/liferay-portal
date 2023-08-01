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

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.repository.PageAssetMetricRepository;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.math.BigDecimal;

import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.ArrayList;
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
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

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
					DSL.field(
						"Individual.suppressed"
					).notEqual(
						Boolean.TRUE
					)
				)
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
				DSL.field(
					"Individual.suppressed"
				).notEqual(
					Boolean.TRUE
				)
			)
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
			DSL.field("description", String.class)
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

	private List<Field<BigDecimal>> _getMetricFields(
		Set<PageMetricType> pageMetricTypes, TimeRange timeRange) {

		Stream<PageMetricType> stream = pageMetricTypes.stream();

		return stream.map(
			metricName -> getMetricFieldAliased(metricName, timeRange)
		).collect(
			Collectors.toList()
		);
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