/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.constants.DataConstants;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.Individual;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.math.BigDecimal;

import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public abstract class BaseAssetMetricRepository<T extends AssetMetric>
	implements AssetMetricRepository<T> {

	@Override
	public List<T> getAppearsOnMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Field<String> canonicalUrlField = DSL.field(
			"canonicalUrl", String.class);
		Field<BigDecimal> metricField = getMetricFieldAliased(
			metricType, timeRange);
		Field<String> pageTitleField = DSL.field("pageTitle", String.class);

		Map<String, BiConsumer<T, Metric>> assetMetricSetters =
			getAssetMetricSetters();

		return queryExecutor.queryForList(
			recordMap -> {
				T assetMetric = createAssetMetric();

				assetMetric.setAssetId((String)recordMap.get("canonicalUrl"));
				assetMetric.setAssetTitle((String)recordMap.get("pageTitle"));

				Metric metric = new Metric(metricType);

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					metricType.getName());

				if (metricValueBigDecimal != null) {
					metric.setValue(metricValueBigDecimal.doubleValue());
				}

				BiConsumer<T, Metric> metricSetterBiConsumer =
					assetMetricSetters.get(metricType.getName());

				metricSetterBiConsumer.accept(assetMetric, metric);

				return assetMetric;
			},
			dslContext.select(
				canonicalUrlField, metricField, pageTitleField
			).from(
				DSL.table(
					getTableName(timeRange)
				).as(
					"metric"
				)
			).where(
				_createWhereClauseCondition(
					assetId, assetTitle, channelId, timeRange)
			).groupBy(
				canonicalUrlField, pageTitleField
			).orderBy(
				metricField.desc()
			));
	}

	@Override
	public T getAssetMetric(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		Set<String> selectedMetrics, TimeRange timeRange) {

		Field<Boolean> previousField = DSL.when(
			DSL.field(
				"eventDate"
			).ge(
				dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(),
					timeZoneDog.getTimeZoneId())
			),
			false
		).otherwise(
			true
		).as(
			"previous"
		);

		SelectSelectStep<Record> selectSelectStep = dslContext.select(
			_getMetricFields(selectedMetrics, timeRange)
		).select(
			previousField
		);

		SelectJoinStep<Record> selectJoinStep = getAssetMetricSelectJoinStep(
			selectSelectStep, timeRange);

		List<Map<String, Object>> recordMaps = queryExecutor.queryForList(
			Function.identity(),
			selectJoinStep.where(
				_createWhereClauseCondition(
					assetId, assetTitle, channelId,
					timeRange.getIncludePreviousTimeRange())
			).groupBy(
				previousField
			));

		return _toMetric(assetId, assetTitle, recordMaps, selectedMetrics);
	}

	@Override
	public List<T> getAssetMetrics(
		@Nullable Long channelId, @Nullable String keywords,
		@Nullable String terms, Pageable pageable, Set<String> selectedMetrics,
		TimeRange timeRange) {

		List<Field<? extends Object>> fields = new ArrayList<>(
			_getMetricFields(selectedMetrics, timeRange));

		Field<String> assetIdField = DSL.field(
			getAssetIdFieldName(), String.class);
		Field<String> assetTitleField = DSL.field(
			getAssetTitleFieldName(), String.class);

		fields.add(assetIdField);

		fields.add(assetTitleField);

		SelectJoinStep<Record> selectJoinStep = getAssetMetricSelectJoinStep(
			dslContext.select(fields), timeRange);

		return queryExecutor.queryForList(
			rowMap -> _toMetric(rowMap, selectedMetrics),
			selectJoinStep.where(
				_createWhereClauseCondition(
					null, null, channelId, keywords, terms, timeRange)
			).groupBy(
				assetIdField, assetTitleField
			).orderBy(
				_getSortFields(pageable.getSort())
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public Long getAssetMetricsCount(
		@Nullable Long channelId, @Nullable String keywords,
		@Nullable String terms, TimeRange timeRange) {

		Field<String> assetIdField = DSL.field(
			getAssetIdFieldName(), String.class);
		Field<String> assetTitleField = DSL.field(
			getAssetTitleFieldName(), String.class);

		return queryExecutor.queryForLong(
			dslContext.selectCount(
			).from(
				DSL.select(
					assetIdField, assetTitleField
				).from(
					DSL.table(
						getTableName(timeRange)
					).as(
						"metric"
					)
				).where(
					_createWhereClauseCondition(
						null, null, channelId, keywords, terms, timeRange)
				).groupBy(
					assetIdField, assetTitleField
				)
			));
	}

	@Override
	public AudienceReport getAudienceReport(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Condition whereClauseCondition = _createWhereClauseCondition(
			assetId, assetTitle, channelId, timeRange);

		Optional<AudienceReport> audienceMetricOptional =
			queryExecutor.queryForObject(
				recordMap -> {
					AudienceReport audienceReport = new AudienceReport();

					audienceReport.setAnonymousIndividualsCount(
						_getLongValue("anonymousIndividualsCount", recordMap));
					audienceReport.setKnownIndividualsCount(
						_getLongValue("knownIndividualsCount", recordMap));
					audienceReport.setNonsegmentedIndividualsCount(
						_getLongValue(
							"nonsegmentedIndividualsCount", recordMap));
					audienceReport.setSegmentedIndividualsCount(
						_getLongValue("segmentedIndividualsCount", recordMap));

					return audienceReport;
				},
				dslContext.select(
					DSL.countDistinct(
						DSL.when(
							DSL.field(
								"individual.id"
							).isNull(),
							DSL.field("identity.id"))
					).as(
						"anonymousIndividualsCount"
					),
					DSL.countDistinct(
						DSL.field("individual.id")
					).as(
						"knownIndividualsCount"
					),
					DSL.countDistinct(
						DSL.when(
							DSL.field(
								"membership.segmentId"
							).isNull(),
							DSL.field("individual.id"))
					).as(
						"nonsegmentedIndividualsCount"
					),
					DSL.countDistinct(
						DSL.when(
							DSL.field(
								"membership.segmentId"
							).isNotNull(),
							DSL.field("individual.id"))
					).as(
						"segmentedIndividualsCount"
					)
				).from(
					DSL.table(
						getTableName(timeRange)
					).as(
						"metric"
					)
				).join(
					DSL.table(
						"BQIdentity"
					).as(
						"identity"
					)
				).on(
					DSL.field(
						"metric.userId"
					).eq(
						DSL.field("identity.id")
					)
				).leftJoin(
					DSL.table(
						"BQIndividual"
					).as(
						"individual"
					)
				).on(
					DSL.and(
						DSL.field(
							"identity.individualId"
						).eq(
							DSL.field("individual.id")
						),
						DSL.or(
							DSL.field(
								"individual.suppressed"
							).isNull(),
							DSL.field(
								"individual.suppressed"
							).notEqual(
								DSL.val(Boolean.TRUE)
							)))
				).leftJoin(
					DSL.table(
						"BQMembership"
					).as(
						"membership"
					)
				).on(
					DSL.field(
						"identity.individualId"
					).eq(
						DSL.field("membership.individualId")
					),
					DSL.field(
						"membership.channelId"
					).eq(
						channelId
					)
				).where(
					whereClauseCondition.and(
						DSL.field(
							metricType.getFieldName()
						).gt(
							0
						))
				));

		return audienceMetricOptional.get();
	}

	@Override
	public List<Metric> getBrowserMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Field<String> browserNameField = DSL.coalesce(
			DSL.field("browserName", String.class), DSL.val("Unknown")
		).as(
			"browserName"
		);
		Field<BigDecimal> metricField1 = getMetricFieldAliased(
			metricType, timeRange);
		Field<BigDecimal> metricField2 = getMetricField(metricType, timeRange);

		return queryExecutor.queryForList(
			recordMap -> {
				Metric metric = new Metric(metricType);

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					metricType.getName());

				if (metricValueBigDecimal == null) {
					metricValueBigDecimal = BigDecimal.ZERO;
				}

				metric.setValue(metricValueBigDecimal.doubleValue());

				metric.setValueKey((String)recordMap.get("browserName"));

				return metric;
			},
			dslContext.select(
				browserNameField, metricField1
			).from(
				DSL.table(
					getTableName(timeRange)
				).as(
					"metric"
				)
			).where(
				_createWhereClauseCondition(
					assetId, assetTitle, channelId, timeRange)
			).groupBy(
				browserNameField
			).having(
				metricField2.greaterThan(BigDecimal.ZERO)
			).orderBy(
				metricField1.desc()
			));
	}

	@Override
	public List<Metric> getDeviceMetrics(
		String assetId, @Null String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Field<String> deviceTypeField = DSL.coalesce(
			DSL.field("deviceType", String.class), DSL.val("Unknown")
		).as(
			"deviceType"
		);
		Field<BigDecimal> metricField1 = getMetricFieldAliased(
			metricType, timeRange);
		Field<BigDecimal> metricField2 = getMetricField(metricType, timeRange);
		Field<String> platformNameField = DSL.coalesce(
			DSL.field("platformName", String.class), DSL.val("Unknown")
		).as(
			"platformName"
		);

		Map<String, Metric> metrics = new LinkedHashMap<>();

		queryExecutor.queryForList(
			recordMap -> {
				Metric deviceTypeMetric = new Metric(metricType);

				String deviceType = (String)recordMap.get("deviceType");

				deviceTypeMetric.setValueKey(deviceType);

				metrics.putIfAbsent(deviceType, deviceTypeMetric);

				deviceTypeMetric = metrics.get(deviceType);

				Metric platformNameMetric = new Metric(metricType);

				String platformName = (String)recordMap.get("platformName");

				platformNameMetric.setValueKey(platformName);

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					metricType.getName());

				if (metricValueBigDecimal == null) {
					metricValueBigDecimal = BigDecimal.ZERO;
				}

				platformNameMetric.setValue(
					metricValueBigDecimal.doubleValue());

				deviceTypeMetric.addMetric(platformNameMetric);

				deviceTypeMetric.setValue(
					deviceTypeMetric.getValue() +
						metricValueBigDecimal.doubleValue());

				return null;
			},
			dslContext.with(
				"MetricsByDevice"
			).as(
				dslContext.select(
					deviceTypeField, metricField1, platformNameField
				).from(
					DSL.table(
						getTableName(timeRange)
					).as(
						"metric"
					)
				).where(
					_createWhereClauseCondition(
						assetId, assetTitle, channelId, timeRange)
				).groupBy(
					deviceTypeField, platformNameField
				).having(
					metricField2.greaterThan(BigDecimal.ZERO)
				)
			).select(
				DSL.asterisk(),
				DSL.sum(
					metricField1
				).over(
				).partitionBy(
					deviceTypeField
				).as(
					"deviceTypeCount"
				)
			).from(
				"MetricsByDevice"
			).orderBy(
				DSL.field(
					"deviceTypeCount"
				).desc(),
				deviceTypeField, metricField1.desc()
			));

		return new ArrayList<>(metrics.values());
	}

	@Override
	public List<Metric> getGeolocationMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Field<String> countryField = DSL.coalesce(
			DSL.field("country", String.class), DSL.val("Unknown")
		).as(
			"country"
		);
		Field<BigDecimal> metricField1 = getMetricFieldAliased(
			metricType, timeRange);
		Field<BigDecimal> metricField2 = getMetricField(metricType, timeRange);

		return queryExecutor.queryForList(
			recordMap -> {
				Metric metric = new Metric(metricType);

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					metricType.getName());

				metric.setValue(metricValueBigDecimal.doubleValue());

				String country = (String)recordMap.get("country");

				if (StringUtils.isBlank(country)) {
					country = DataConstants.UNKNOWN;
				}

				metric.setValueKey(country);

				return metric;
			},
			dslContext.select(
				countryField, metricField1
			).from(
				DSL.table(
					getTableName(timeRange)
				).as(
					"metric"
				)
			).where(
				_createWhereClauseCondition(
					assetId, assetTitle, channelId, timeRange)
			).groupBy(
				countryField
			).having(
				metricField2.greaterThan(BigDecimal.ZERO)
			).orderBy(
				metricField1.desc()
			));
	}

	@Override
	public List<HistogramMetric> getHistogramMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		boolean includePrevious, Interval interval, MetricType metricType,
		TimeRange timeRange) {

		Field field = DSL.timestamp(
			dslHelper.dateTrunc(
				DatePart.valueOf(interval.name()),
				dslHelper.getDateAtTimeZoneField(
					"eventdate", timeZoneDog.getTimeZoneId())));

		field = field.as("key");

		SelectJoinStep<Record> selectJoinStep = getAssetMetricSelectJoinStep(
			dslContext.select(
				field, getMetricFieldAliased(metricType, timeRange)),
			timeRange);

		return queryExecutor.queryForList(
			rowMap -> {
				Metric metric = new Metric(metricType);

				BigDecimal bigDecimal = (BigDecimal)rowMap.get(
					metricType.getName());

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
				_createWhereClauseCondition(
					assetId, assetTitle, channelId, includePrevious, timeRange)
			).groupBy(
				field
			));
	}

	@Override
	public List<Individual> getKnownIndividuals(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, Pageable pageable, @Nullable String keywords,
		TimeRange timeRange) {

		Table<Record> individualTable = DSL.table(
			"BQIndividual"
		).as(
			"individual"
		);

		Condition whereClauseCondition = _createWhereClauseCondition(
			assetId, assetTitle, channelId, timeRange);

		if (StringUtils.isNotBlank(keywords)) {
			whereClauseCondition = whereClauseCondition.and(
				_createIndividualKeywordsWhereClauseCondition(keywords));
		}

		return queryExecutor.queryForList(
			recordMap -> new Individual(
				(String)recordMap.get("emailAddress"),
				(String)recordMap.get("id"),
				recordMap.get("firstName") + " " + recordMap.get("lastName")),
			dslContext.select(
				DSL.field(
					"individual.emailAddress"
				).as(
					"emailAddress"
				),
				DSL.field(
					"individual.firstName"
				).as(
					"firstName"
				),
				DSL.field(
					"individual.id"
				).as(
					"id"
				),
				DSL.field(
					"individual.lastName"
				).as(
					"lastName"
				)
			).from(
				DSL.table(
					getTableName(timeRange)
				).as(
					"metric"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"identity"
				)
			).on(
				DSL.field(
					"identity.id"
				).eq(
					DSL.field("metric.userId")
				)
			).join(
				individualTable
			).on(
				DSL.and(
					DSL.field(
						"individual.id"
					).eq(
						DSL.field("identity.individualId")
					),
					DSL.or(
						DSL.field(
							"individual.suppressed"
						).isNull(),
						DSL.field(
							"individual.suppressed"
						).notEqual(
							DSL.val(Boolean.TRUE)
						)))
			).where(
				whereClauseCondition.and(
					DSL.field(
						metricType.getFieldName()
					).gt(
						0
					))
			).groupBy(
				DSL.field("emailAddress"), DSL.field("firstName"),
				DSL.field("id"), DSL.field("lastName")
			).orderBy(
				_getSortFields(pageable.getSort())
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public long getKnownIndividualsCount(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, @Nullable String keywords, TimeRange timeRange) {

		Table<Record> individualTable = DSL.table(
			"BQIndividual"
		).as(
			"individual"
		);

		Condition whereClauseCondition = _createWhereClauseCondition(
			assetId, assetTitle, channelId, timeRange);

		if (StringUtils.isNotBlank(keywords)) {
			whereClauseCondition = whereClauseCondition.and(
				_createIndividualKeywordsWhereClauseCondition(keywords));
		}

		return queryExecutor.queryForLong(
			dslContext.with(
				"KnownIndividuals"
			).as(
				dslContext.select(
					DSL.field(
						"individual.emailAddress"
					).as(
						"emailAddress"
					),
					DSL.field(
						"individual.firstName"
					).as(
						"firstName"
					),
					DSL.field(
						"individual.id"
					).as(
						"id"
					),
					DSL.field(
						"individual.lastName"
					).as(
						"lastName"
					)
				).from(
					DSL.table(
						getTableName(timeRange)
					).as(
						"metric"
					)
				).join(
					DSL.table(
						"BQIdentity"
					).as(
						"identity"
					)
				).on(
					DSL.field(
						"identity.id"
					).eq(
						DSL.field("metric.userId")
					)
				).join(
					individualTable
				).on(
					DSL.and(
						DSL.field(
							"individual.id"
						).eq(
							DSL.field("identity.individualId")
						),
						DSL.or(
							DSL.field(
								"individual.suppressed"
							).isNull(),
							DSL.field(
								"individual.suppressed", Boolean.class
							).notEqual(
								DSL.val(Boolean.TRUE)
							)))
				).where(
					whereClauseCondition.and(
						DSL.field(
							metricType.getFieldName()
						).gt(
							0
						))
				).groupBy(
					DSL.field("emailAddress"), DSL.field("firstName"),
					DSL.field("id"), DSL.field("lastName")
				)
			).selectCount(
			).from(
				"KnownIndividuals"
			));
	}

	@Override
	public List<Metric> getSegmentMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange) {

		Condition whereClauseCondition = _createWhereClauseCondition(
			assetId, assetTitle, channelId, timeRange);

		return queryExecutor.queryForList(
			recordMap -> {
				Metric metric = new Metric(metricType);

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					"individualsCount");

				metric.setValueKey(String.valueOf(recordMap.get("segmentId")));

				if (metricValueBigDecimal != null) {
					metric.setValue(metricValueBigDecimal.doubleValue());
				}

				return metric;
			},
			dslContext.select(
				DSL.countDistinct(
					DSL.field("membership.individualId")
				).as(
					"individualsCount"
				),
				DSL.field(
					"membership.segmentId"
				).as(
					"segmentId"
				)
			).from(
				DSL.table(
					getTableName(timeRange)
				).as(
					"metric"
				)
			).join(
				DSL.table(
					"BQIdentity"
				).as(
					"identity"
				)
			).on(
				DSL.field(
					"metric.userId"
				).eq(
					DSL.field("identity.id")
				)
			).join(
				DSL.table(
					"BQMembership"
				).as(
					"membership"
				)
			).on(
				DSL.field(
					"identity.individualId"
				).eq(
					DSL.field("membership.individualId")
				),
				DSL.field(
					"membership.channelId"
				).eq(
					channelId
				)
			).where(
				whereClauseCondition.and(
					DSL.field(
						metricType.getFieldName()
					).gt(
						0
					))
			).groupBy(
				DSL.field("membership.segmentId")
			).orderBy(
				DSL.field(
					"individualsCount"
				).desc()
			));
	}

	protected abstract T createAssetMetric();

	protected String getAssetIdFieldName() {
		return "assetId";
	}

	protected SelectJoinStep<Record> getAssetMetricSelectJoinStep(
		SelectSelectStep<Record> selectSelectStep, TimeRange timeRange) {

		return selectSelectStep.from(
			DSL.table(
				getTableName(timeRange)
			).as(
				"metric"
			));
	}

	protected abstract Map<String, BiConsumer<T, Metric>>
		getAssetMetricSetters();

	protected String getAssetTitleFieldName() {
		return "assetTitle";
	}

	protected Condition getDescriptionLikeCondition(String terms) {
		return DSL.noCondition();
	}

	protected Condition getKeywordSearchCondition(String keywords) {
		return DSL.lower(
			DSL.field(getAssetTitleFieldName(), String.class)
		).like(
			StringUtils.wrap(StringUtils.lowerCase(keywords), "%")
		);
	}

	protected abstract Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange);

	protected Field<BigDecimal> getMetricFieldAliased(
		MetricType metricType, TimeRange timeRange) {

		Field<BigDecimal> metricField = getMetricField(metricType, timeRange);

		return metricField.as(metricType.getName());
	}

	protected abstract MetricType getMetricType(String metricTypeName);

	protected abstract MetricType[] getMetricTypes();

	protected abstract String getTableName(TimeRange timeRange);

	@Autowired
	protected DSLContext dslContext;

	@Autowired
	protected DSLHelper dslHelper;

	@Autowired
	protected QueryExecutor queryExecutor;

	@Autowired
	protected TimeZoneDog timeZoneDog;

	private Condition _createIndividualKeywordsWhereClauseCondition(
		String keywords) {

		String wrappedKeywords = StringUtils.wrap(
			StringUtils.lowerCase(keywords), "%");

		return DSL.or(
			DSL.lower(
				DSL.field("individual.firstname", String.class)
			).like(
				wrappedKeywords
			),
			DSL.lower(
				DSL.field("individual.lastname", String.class)
			).like(
				wrappedKeywords
			),
			DSL.lower(
				DSL.field("individual.emailaddress", String.class)
			).like(
				wrappedKeywords
			));
	}

	private Condition _createWhereClauseCondition(
		@Nullable String assetId, @Nullable String assetTitle,
		@Nullable Long channelId, boolean includePrevious,
		TimeRange timeRange) {

		if (includePrevious) {
			timeRange = timeRange.getIncludePreviousTimeRange();
		}

		return _createWhereClauseCondition(
			assetId, assetTitle, channelId, null, null, timeRange);
	}

	private Condition _createWhereClauseCondition(
		@Nullable String assetId, @Nullable String assetTitle,
		@Nullable Long channelId, @Nullable String keywords,
		@Nullable String terms, TimeRange timeRange) {

		String timeZoneId = timeZoneDog.getTimeZoneId();

		List<Condition> conditions = new ArrayList<>();

		if (StringUtils.isNotBlank(assetId)) {
			conditions.add(
				DSL.field(
					getAssetIdFieldName()
				).eq(
					assetId
				));
		}

		if (StringUtils.isNotBlank(assetTitle)) {
			conditions.add(
				DSL.field(
					getAssetTitleFieldName()
				).eq(
					assetTitle
				));
		}

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"metric.channelId"
				).eq(
					channelId
				));
		}

		conditions.add(
			DSL.field(
				"eventDate"
			).between(
				dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(), timeZoneId),
				dslHelper.getDateParam(
					timeRange.getEndLocalDateTime(), timeZoneId)
			));

		if (StringUtils.isNotBlank(keywords)) {
			conditions.add(getKeywordSearchCondition(keywords));
		}

		if (StringUtils.isNotBlank(terms)) {
			conditions.add(
				DSL.or(
					DSL.lower(
						DSL.trim(
							DSL.replace(
								DSL.field(
									getAssetTitleFieldName(), String.class),
								"\n", ""))
					).like(
						StringUtils.wrap(
							StringUtils.lowerCase(
								StringUtils.trim(
									StringUtils.replace(terms, "\n", ""))),
							"%")
					),
					getDescriptionLikeCondition(terms)));
		}

		return DSL.and(conditions);
	}

	private Condition _createWhereClauseCondition(
		@Nullable String assetId, @Nullable String assetTitle,
		@Nullable Long channelId, TimeRange timeRange) {

		return _createWhereClauseCondition(
			assetId, assetTitle, channelId, null, null, timeRange);
	}

	private Long _getLongValue(
		String fieldName, Map<String, Object> recordMap) {

		BigDecimal count = (BigDecimal)recordMap.get(fieldName);

		return count.longValue();
	}

	private List<Field<BigDecimal>> _getMetricFields(
		Set<String> metricNames, TimeRange timeRange) {

		return Stream.of(
			getMetricTypes()
		).filter(
			assetMetricType -> metricNames.contains(assetMetricType.getName())
		).map(
			metricName -> getMetricFieldAliased(metricName, timeRange)
		).collect(
			Collectors.toList()
		);
	}

	private Collection<SortField<?>> _getSortFields(Sort sort) {
		Collection<SortField<?>> sortFields = new ArrayList<>();

		for (Sort.Order order : sort.toList()) {
			String fieldName = order.getProperty();

			Field<?> field = DSL.field(fieldName);

			if (order.getDirection() == Sort.Direction.ASC) {
				SortField<?> sortField = field.asc();

				sortFields.add(sortField.nullsFirst());
			}
			else {
				SortField<?> sortField = field.desc();

				sortFields.add(sortField.nullsLast());
			}
		}

		return sortFields;
	}

	private T _toMetric(
		Map<String, Object> recordMap, Set<String> selectedMetrics) {

		T assetMetric = createAssetMetric();

		assetMetric.setAssetId((String)recordMap.get(getAssetIdFieldName()));
		assetMetric.setAssetTitle(
			(String)recordMap.get(getAssetTitleFieldName()));

		Map<String, BiConsumer<T, Metric>> assetMetricSetters =
			getAssetMetricSetters();

		for (String selectedMetric : selectedMetrics) {
			MetricType metricType = getMetricType(selectedMetric);

			Metric metric = new Metric(metricType);

			BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
				metricType.getName());

			if (metricValueBigDecimal != null) {
				metric.setValue(metricValueBigDecimal.doubleValue());
			}

			BiConsumer<T, Metric> metricSetterBiConsumer =
				assetMetricSetters.get(selectedMetric);

			metricSetterBiConsumer.accept(assetMetric, metric);
		}

		return assetMetric;
	}

	private T _toMetric(
		String assetId, String assetTitle, List<Map<String, Object>> recordMaps,
		Set<String> selectedMetrics) {

		T assetMetric = createAssetMetric();

		assetMetric.setAssetId(assetId);
		assetMetric.setAssetTitle(assetTitle);

		Map<String, BiConsumer<T, Metric>> assetMetricSetters =
			getAssetMetricSetters();

		Map<String, Metric> metrics = new HashMap<>();

		for (String selectedMetric : selectedMetrics) {
			for (Map<String, Object> recordMap : recordMaps) {
				Metric metric = metrics.computeIfAbsent(
					selectedMetric,
					metricTypeName -> {
						MetricType metricType = getMetricType(metricTypeName);

						return new Metric(metricType);
					});

				MetricType metricType = metric.getMetricType();

				BigDecimal metricValueBigDecimal = (BigDecimal)recordMap.get(
					metricType.getName());

				if (metricValueBigDecimal != null) {
					boolean previous = (boolean)recordMap.get("previous");

					if (previous) {
						metric.setPreviousValue(
							metricValueBigDecimal.doubleValue());
					}
					else {
						metric.setValue(metricValueBigDecimal.doubleValue());
					}
				}
			}
		}

		for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
			BiConsumer<T, Metric> metricSetterBiConsumer =
				assetMetricSetters.get(entry.getKey());

			metricSetterBiConsumer.accept(assetMetric, entry.getValue());
		}

		return assetMetric;
	}

}