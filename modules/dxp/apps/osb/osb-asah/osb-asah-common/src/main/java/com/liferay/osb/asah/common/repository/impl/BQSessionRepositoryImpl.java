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

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.model.AcquisitionType;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.SiteVisitorBehaviorMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.CustomBQSessionRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.BQSQLUtil;
import com.liferay.osb.asah.common.util.GetterUtil;

import java.math.BigDecimal;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.SelectFinalStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.WithStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

/**
 * @author Marcos Martins
 */
public class BQSessionRepositoryImpl
	extends BaseRepository implements CustomBQSessionRepository {

	public BQSessionRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countSessionFieldValues(
		String fieldName, FilterHelper filterHelper, String value) {

		Field<String> field = null;

		if (_nestedFieldNamesMap.containsKey(fieldName)) {
			field = DSL.field("value", String.class);
		}
		else {
			field = DSL.field(fieldName, String.class);
		}

		SelectJoinStep<Record1<Integer>> selectJoinStep = _dslContext.select(
			DSL.countDistinct(field)
		).from(
			DSL.table(
				"BQSession"
			).as(
				"Session"
			)
		);

		if (_nestedFieldNamesMap.containsKey(fieldName)) {
			selectJoinStep = selectJoinStep.crossJoin(
				DSL.unnest(
					DSL.field(
						_nestedFieldNamesMap.get(fieldName), String[].class)
				).as(
					"value"
				));
		}

		Condition condition = filterHelper.getCondition();

		if (!StringUtils.isEmpty(value)) {
			condition = condition.and(
				DSL.lower(
					field
				).like(
					DSL.lower(StringUtils.wrap(value, "%"))
				));
		}

		return _queryExecutor.queryForLong(selectJoinStep.where(condition));
	}

	@Override
	public List<BQSession> findAllById(Collection<String> sessionIds) {
		if (sessionIds.isEmpty()) {
			return Collections.emptyList();
		}

		Table<Record> sessionTable = DSL.table(
			"BQSession"
		).as(
			"Session"
		);

		SelectSelectStep<Record> selectSelectStep = _dslContext.select(
			sessionTable.asterisk());

		return _queryExecutor.queryForList(
			BQSession::new,
			selectSelectStep.from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).where(
				DSL.field(
					"id"
				).in(
					sessionIds
				)
			));
	}

	@Override
	public Map<String, BigDecimal> getAcquisitionsMetrics(
		AcquisitionType acquisitionType, Long channelId, Pageable pageable,
		TimeRange timeRange, ZoneId zoneId) {

		if (acquisitionType == AcquisitionType.REFERRER) {
			Field field = DSL.field("referrers");

			return _queryExecutor.queryForMap(
				GetterUtil::getString,
				_dslContext.with(
					"acquisitionsReferrers"
				).as(
					_dslContext.select(
						field
					).from(
						DSL.table(
							"BQSession"
						).as(
							"Session"
						)
					).where(
						DSL.and(
							_createWhereClauseCondition(
								channelId, timeRange, zoneId),
							field.isNotNull())
					)
				).with(
					"acquisitionsReferrersList"
				).as(
					_dslContext.select(
						DSL.asterisk()
					).from(
						DSL.table("acquisitionsReferrers"),
						DSL.table(
							"UNNEST(referrers)"
						).as(
							"field"
						)
					)
				).select(
					DSL.field("field"),
					DSL.count(
					).cast(
						BigDecimal.class
					)
				).from(
					"acquisitionsReferrersList"
				).groupBy(
					DSL.field("field")
				).orderBy(
					DSL.count(
					).desc()
				).limit(
					pageable.getPageSize()
				).offset(
					pageable.getOffset()
				),
				GetterUtil::getBigDecimal);
		}

		Field field = null;

		if (acquisitionType == AcquisitionType.CHANNEL) {
			field = DSL.field("acquisitionchannel");
		}
		else {
			field = DSL.field(
				"acquisitionsource || ' / ' || acquisitionmedium");
		}

		field = DSL.lower(field);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_dslContext.select(
				field.as("field"),
				DSL.count(
				).cast(
					BigDecimal.class
				)
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).where(
				DSL.and(
					_createWhereClauseCondition(channelId, timeRange, zoneId),
					field.isNotNull())
			).groupBy(
				DSL.field("field")
			).orderBy(
				DSL.count(
				).desc()
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			),
			GetterUtil::getBigDecimal);
	}

	@Override
	public long getAcquisitionsMetricsCount(
		AcquisitionType acquisitionType, Long channelId, TimeRange timeRange,
		ZoneId zoneId) {

		if (acquisitionType == AcquisitionType.REFERRER) {
			Field field = DSL.field("referrers");

			return _queryExecutor.queryForLong(
				_dslContext.with(
					"acquisitionsReferrers"
				).as(
					_dslContext.select(
						field
					).from(
						DSL.table(
							"BQSession"
						).as(
							"Session"
						)
					).where(
						DSL.and(
							_createWhereClauseCondition(
								channelId, timeRange, zoneId),
							field.isNotNull())
					)
				).with(
					"acquisitionsReferrersList"
				).as(
					_dslContext.select(
						DSL.asterisk()
					).from(
						DSL.table("acquisitionsReferrers"),
						DSL.table(
							"UNNEST(referrers)"
						).as(
							"field"
						)
					)
				).select(
					DSL.count(DSL.asterisk())
				).from(
					"acquisitionsReferrersList"
				));
		}

		Field field = null;

		if (acquisitionType == AcquisitionType.CHANNEL) {
			field = DSL.field("acquisitionchannel");
		}
		else {
			field = DSL.field(
				"acquisitionsource || ' / ' || acquisitionmedium");
		}

		return _queryExecutor.queryForLong(
			_dslContext.select(
				DSL.count(DSL.field(field))
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).where(
				DSL.and(
					_createWhereClauseCondition(channelId, timeRange, zoneId),
					field.isNotNull())
			));
	}

	public List<Map<String, Object>> getCohortHeatMapTuples(
		Long channelId, Interval interval, TimeRange timeRange, ZoneId zoneId) {

		WithStep withStep = _createCohortWithStep(
			channelId, interval, timeRange, zoneId);

		Field<Object> sessionDateField = DSL.field(
			"retentionTable.sessionDate");

		return _queryExecutor.queryForList(
			Function.identity(),
			withStep.select(
				_getCohortFields(interval)
			).from(
				DSL.table("retentionTable")
			).leftJoin(
				DSL.table("cohortSize")
			).on(
				sessionDateField.eq(DSL.field("cohortSize.sessionDate"))
			).and(
				DSL.field(
					"retentionTable.isKnown"
				).eq(
					DSL.field("cohortSize.isKnown")
				)
			).where(
				sessionDateField.isNotNull()
			).groupBy(
				sessionDateField
			).orderBy(
				sessionDateField
			));
	}

	@Override
	public Map<String, BigDecimal> getSessionsCountGroupedByBrowserName(
		Long channelId, TimeRange timeRange, ZoneId zoneId) {

		Field field = DSL.coalesce(
			DSL.field("browserName", String.class), DSL.val("Unknown")
		).as(
			"browserName"
		);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_dslContext.select(
				field, DSL.count()
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).where(
				_createWhereClauseCondition(channelId, timeRange, zoneId)
			).groupBy(
				field
			).orderBy(
				DSL.count(
				).desc()
			),
			GetterUtil::getBigDecimal);
	}

	@Override
	public List<Map<String, Object>> getSessionsCountGroupedByDeviceName(
		Long channelId, TimeRange timeRange, ZoneId zoneId) {

		Field<String> deviceTypeField = DSL.coalesce(
			DSL.field("deviceType", String.class), DSL.val("Unknown")
		).as(
			"deviceType"
		);
		Field<String> platformNameField = DSL.coalesce(
			DSL.field("platformName", String.class), DSL.val("Unknown")
		).as(
			"platformName"
		);

		return _queryExecutor.queryForList(
			Function.identity(),
			_dslContext.with(
				"SessionsByDevice"
			).as(
				_dslContext.select(
					deviceTypeField, platformNameField,
					DSL.count(
					).as(
						"count"
					)
				).from(
					DSL.table(
						"BQSession"
					).as(
						"Session"
					)
				).where(
					_createWhereClauseCondition(channelId, timeRange, zoneId)
				).groupBy(
					deviceTypeField, platformNameField
				)
			).select(
				DSL.asterisk(),
				DSL.sum(
					DSL.field("count", Long.class)
				).over(
				).partitionBy(
					deviceTypeField
				).as(
					"deviceTypeCount"
				)
			).from(
				"SessionsByDevice"
			).orderBy(
				DSL.field(
					"deviceTypeCount"
				).desc(),
				deviceTypeField,
				DSL.field(
					"count"
				).desc()
			));
	}

	@Override
	public Map<String, BigDecimal> getSessionsCountGroupedByGeolocation(
		Long channelId, TimeRange timeRange, ZoneId zoneId) {

		Field field = DSL.coalesce(
			DSL.field("country", String.class), DSL.val("Unknown")
		).as(
			"country"
		);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_dslContext.select(
				field, DSL.count()
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).where(
				_createWhereClauseCondition(channelId, timeRange, zoneId)
			).groupBy(
				field
			).orderBy(
				DSL.count(
				).desc()
			),
			GetterUtil::getBigDecimal);
	}

	@Override
	public List<SiteVisitorBehaviorMetric> getSiteVisitorBehaviorMetrics(
		Long channelId, boolean includePrevious, TimeRange timeRange,
		ZoneId zoneId) {

		Field<Boolean> previousField = DSL.when(
			DSL.field(
				"Session.sessionStart"
			).ge(
				_dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(), zoneId.toString())
			),
			false
		).otherwise(
			true
		).as(
			"previous"
		);

		return _queryExecutor.queryForList(
			SiteVisitorBehaviorMetric::new,
			(SelectFinalStep)_joinWithIdentityTable(
				_dslContext.select(
					previousField, _getKnownVisitorsField(true),
					_getUniqueVisitorsField(),
					DSL.avg(
						DSL.field("duration", Long.class)
					).as(
						"averagesessionduration"
					),
					DSL.sum(
						DSL.field("bounce", Long.class)
					).as(
						"bounces"
					),
					DSL.count(
					).as(
						"sessions"
					)
				).from(
					DSL.table(
						"BQSession"
					).as(
						"Session"
					)
				)
			).where(
				_createWhereClauseCondition(
					channelId, includePrevious, timeRange, zoneId)
			).groupBy(
				previousField
			));
	}

	@Override
	public List<SiteVisitorBehaviorMetric>
		getSiteVisitorBehaviorMetricsGroupedBySessionStart(
			Long channelId, boolean includePrevious, Interval interval,
			TimeRange timeRange, ZoneId zoneId) {

		Field sessionStartField = DSL.timestamp(
			_dslHelper.dateTrunc(
				DatePart.valueOf(interval.name()),
				_dslHelper.getDateAtTimeZoneField(
					"Session.sessionStart", zoneId.toString())));

		sessionStartField = sessionStartField.as("eventdate");

		return _queryExecutor.queryForList(
			SiteVisitorBehaviorMetric::new,
			_joinWithIdentityTable(
				_dslContext.select(
					sessionStartField, _getKnownVisitorsField(true),
					_getUniqueVisitorsField(),
					DSL.avg(
						DSL.field("duration", Long.class)
					).as(
						"averagesessionduration"
					),
					DSL.sum(
						DSL.field("bounce", Long.class)
					).as(
						"bounces"
					),
					DSL.count(
					).as(
						"sessions"
					)
				).from(
					DSL.table(
						"BQSession"
					).as(
						"Session"
					)
				)
			).where(
				_createWhereClauseCondition(
					channelId, includePrevious, timeRange, zoneId)
			).groupBy(
				sessionStartField
			));
	}

	@Override
	public List<Map<String, BigDecimal>> getVisitorsCountGroupedByDayAndTime(
		Long channelId, TimeRange timeRange, ZoneId zoneId) {

		Field dayOfWeekField = _dslHelper.getDayOfWeekField(
			_dslHelper.getDateAtTimeZoneField(
				"Session.sessionStart", zoneId.toString()));

		dayOfWeekField = dayOfWeekField.cast(BigDecimal.class);

		dayOfWeekField = dayOfWeekField.as("dayOfWeek");

		Field dateField = DSL.timestamp(
			_dslHelper.dateTrunc(
				DatePart.valueOf(String.valueOf(Interval.HOUR)),
				_dslHelper.getDateAtTimeZoneField(
					"Session.sessionStart", zoneId.toString())));

		Field hourOfDayField = DSL.extract(
			dateField, DatePart.HOUR
		).cast(
			BigDecimal.class
		);

		hourOfDayField = hourOfDayField.as("hourOfDay");

		Field uniqueVisitorsField = _getKnownVisitorsField(
			false
		).plus(
			DSL.countDistinct(
				DSL.when(
					DSL.field(
						"IndividualIdentity.individualId"
					).isNull(),
					DSL.field("Session.userid")))
		).cast(
			BigDecimal.class
		);

		uniqueVisitorsField = uniqueVisitorsField.as("visitors");

		return _queryExecutor.queryForList(
			Function.identity(),
			_joinWithIdentityTable(
				_dslContext.select(
					dayOfWeekField, hourOfDayField, uniqueVisitorsField
				).from(
					DSL.table(
						"BQSession"
					).as(
						"Session"
					)
				)
			).where(
				_createWhereClauseCondition(channelId, timeRange, zoneId)
			).groupBy(
				dayOfWeekField, hourOfDayField
			));
	}

	@Override
	public BQSession insert(BQSession bqSession) {
		_queryExecutor.queryExecute(BQSQLUtil.createInsertStatement(bqSession));

		return bqSession;
	}

	@Override
	public List<String> searchSessionFieldValues(
		String fieldName, FilterHelper filterHelper, Pageable pageable,
		String value) {

		Field<String> field = null;

		if (_nestedFieldNamesMap.containsKey(fieldName)) {
			field = DSL.field("value", String.class);
		}
		else {
			field = DSL.field(fieldName, String.class);
		}

		Condition condition = filterHelper.getCondition();

		if (!StringUtils.isEmpty(value)) {
			condition = condition.and(
				_dslHelper.containsSubstring(field.getName(), value));
		}

		SelectJoinStep<Record1<String>> selectJoinStep = _dslContext.select(
			field
		).from(
			DSL.table(
				"BQSession"
			).as(
				"Session"
			)
		);

		if (_nestedFieldNamesMap.containsKey(fieldName)) {
			selectJoinStep = selectJoinStep.crossJoin(
				DSL.table(
					"UNNEST(" + _nestedFieldNamesMap.get(fieldName) + ")"
				).as(
					"value"
				));
		}

		String key = field.getName();

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get(key)),
			selectJoinStep.where(
				condition
			).groupBy(
				field
			).orderBy(
				field.asc()
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private WithStep _createCohortWithStep(
		Long channelId, Interval interval, TimeRange timeRange, ZoneId zoneId) {

		DatePart datePart = DatePart.DAY;

		if (Interval.MONTH.equals(interval)) {
			datePart = DatePart.MONTH;
		}
		else if (Interval.WEEK.equals(interval)) {
			datePart = DatePart.WEEK;
		}

		return _dslContext.with(
			"firstSession"
		).as(
			_getFirstSessionSelectJoinStep(
				channelId, datePart, timeRange, zoneId)
		).with(
			"sessions"
		).as(
			_getSessionsSelectJoinStep(channelId, datePart, timeRange, zoneId)
		).with(
			"cohortSize"
		).as(
			_dslContext.select(
				DSL.field("sessionDate"), DSL.field("isKnown"),
				DSL.count(
				).as(
					"visitorsCount"
				)
			).from(
				"firstSession"
			).groupBy(
				DSL.field("sessionDate"), DSL.field("isKnown")
			).orderBy(
				DSL.field("sessionDate")
			)
		).with(
			"retentionTable"
		).as(
			_dslContext.select(
				DSL.field("firstSession.sessionDate"),
				DSL.field("sessions.intervalNumber"),
				DSL.field("firstSession.isKnown"),
				DSL.count(
				).as(
					"visitorsCount"
				)
			).from(
				DSL.table("sessions")
			).leftJoin(
				DSL.table("firstSession")
			).on(
				DSL.field(
					"sessions.userId"
				).eq(
					DSL.field("firstSession.userId")
				)
			).groupBy(
				DSL.field("firstSession.sessionDate"),
				DSL.field("sessions.intervalNumber"),
				DSL.field("firstSession.isKnown")
			).orderBy(
				DSL.field("firstSession.sessionDate")
			)
		);
	}

	private Condition _createWhereClauseCondition(
		Long channelId, boolean includePrevious, TimeRange timeRange,
		ZoneId zoneId) {

		TimeRange whereClauseTimeRange = timeRange;

		if (includePrevious) {
			whereClauseTimeRange = timeRange.getIncludePreviousTimeRange();
		}

		return _createWhereClauseCondition(
			channelId, whereClauseTimeRange, zoneId);
	}

	private Condition _createWhereClauseCondition(
		Long channelId, TimeRange timeRange, ZoneId zoneId) {

		return DSL.and(
			DSL.field(
				"Session.channelId"
			).eq(
				channelId
			),
			DSL.field(
				"Session.sessionStart"
			).between(
				_dslHelper.getDateParam(
					timeRange.getStartLocalDateTime(), zoneId.toString()),
				_dslHelper.getDateParam(
					timeRange.getEndLocalDateTime(), zoneId.toString())
			));
	}

	private Field<BigDecimal> _getCohortField(
		int fieldIndex, boolean known, boolean retention) {

		String cohortType = "Total";

		if (retention) {
			cohortType = "Retention";
		}

		Field<Boolean> isKnownField = _dslHelper.getCastBooleanField(
			_dslHelper.getCastStringField(DSL.field("retentionTable.isKnown")));

		Condition isKnownCondition = isKnownField.eq(false);

		String visitorType = "Unknown";

		if (known) {
			isKnownCondition = isKnownField.eq(true);
			visitorType = "Known";
		}

		Field<Double> conditionStep = DSL.when(
			DSL.field(
				"retentionTable.intervalNumber"
			).eq(
				fieldIndex
			).and(
				isKnownCondition
			),
			_dslHelper.getCastNumberField(
				_dslHelper.getCastStringField(
					DSL.field("retentionTable.visitorsCount")))
		).otherwise(
			0D
		);

		if (retention) {
			conditionStep = conditionStep.div(
				_dslHelper.getCastNumberField(
					_dslHelper.getCastStringField(
						DSL.field("cohortSize.visitorsCount"))));
		}

		String fieldName = String.format(
			"interval%d%s%s", fieldIndex, cohortType, visitorType);

		return DSL.sum(
			conditionStep
		).as(
			fieldName
		);
	}

	private List<Field<?>> _getCohortFields(Interval interval) {
		List<Field<?>> fields = new ArrayList<>();

		Field<String> sessionDateField = _dslHelper.getCastStringField(
			_dslHelper.formatDate(
				"%F",
				DSL.field(
					"retentionTable.sessionDate"
				).cast(
					Date.class
				)));

		fields.add(sessionDateField.as("cohortDate"));

		int maxIntervals = 7;

		if (Interval.DAY.equals(interval)) {
			maxIntervals = 8;
		}

		for (int i = 0; i < maxIntervals; i++) {
			fields.add(_getCohortField(i, true, true));
			fields.add(_getCohortField(i, false, true));
			fields.add(_getCohortField(i, true, false));
			fields.add(_getCohortField(i, false, false));
		}

		return fields;
	}

	private SelectJoinStep<? extends Record> _getFirstSessionSelectJoinStep(
		Long channelId, DatePart datePart, TimeRange timeRange, ZoneId zoneId) {

		SelectJoinStep<Record3<Object, Boolean, Date>> selectJoinStep =
			_dslContext.select(
				DSL.field("Session.userId"),
				DSL.when(
					DSL.field(
						"IndividualIdentity.individualId"
					).isNull(),
					false
				).otherwise(
					true
				).as(
					"isKnown"
				),
				DSL.min(
					_dslHelper.dateTrunc(
						datePart,
						_dslHelper.getDateAtTimeZoneField(
							"Session.sessionStart", zoneId.toString()))
				).as(
					"sessionDate"
				)
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			);

		SelectOnConditionStep<? extends Record> selectOnConditionStep =
			_joinWithIdentityTable(selectJoinStep);

		selectOnConditionStep.where(
			_createWhereClauseCondition(channelId, timeRange, zoneId)
		).groupBy(
			DSL.field("Session.userId"), DSL.field("isKnown")
		).orderBy(
			DSL.field("Session.userId"), DSL.field("isKnown")
		);

		return selectOnConditionStep;
	}

	private Field<Integer> _getKnownVisitorsField(boolean alias) {
		Field<Integer> field = DSL.countDistinct(
			DSL.field("IndividualIdentity.individualId"));

		if (alias) {
			return field.as("knownvisitors");
		}

		return field;
	}

	private SelectJoinStep<? extends Record> _getSessionsSelectJoinStep(
		Long channelId, DatePart datePart, TimeRange timeRange, ZoneId zoneId) {

		Field<OffsetDateTime> dateDiffField = _dslHelper.dateDiff(
			datePart,
			_dslHelper.dateTrunc(
				datePart,
				_dslHelper.getDateAtTimeZoneField(
					"Session.sessionStart", zoneId.toString())),
			DSL.field("firstSession.sessionDate", OffsetDateTime.class));

		SelectJoinStep<Record3<Object, Object, OffsetDateTime>> selectJoinStep =
			_dslContext.select(
				DSL.field("Session.userId"), DSL.field("firstSession.isKnown"),
				dateDiffField.as("intervalNumber")
			).from(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			);

		SelectOnConditionStep<? extends Record> selectOnConditionStep =
			_joinWithIdentityTable(selectJoinStep);

		selectOnConditionStep.leftJoin(
			DSL.table("firstSession")
		).on(
			DSL.field(
				"Session.userId"
			).eq(
				DSL.field("firstSession.userId")
			)
		).where(
			_createWhereClauseCondition(channelId, timeRange, zoneId)
		).groupBy(
			DSL.field("Session.userId"), DSL.field("firstSession.isKnown"),
			DSL.field("intervalNumber")
		).orderBy(
			DSL.field("Session.userId"), DSL.field("firstSession.isKnown")
		);

		return selectOnConditionStep;
	}

	private Field<Integer> _getUniqueVisitorsField() {
		return _getKnownVisitorsField(
			false
		).plus(
			DSL.countDistinct(
				DSL.when(
					DSL.field(
						"IndividualIdentity.individualId"
					).isNull(),
					DSL.field("Session.userid")))
		).as(
			"visitors"
		);
	}

	private SelectOnConditionStep<? extends Record> _joinWithIdentityTable(
		SelectJoinStep<? extends Record> selectJoinStep) {

		return selectJoinStep.leftJoin(
			_dslContext.select(
				DSL.field(
					"Identity.individualId"
				).as(
					"individualId"
				),
				DSL.field("Identity.id")
			).from(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).innerJoin(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).on(
				DSL.and(
					DSL.field(
						"Identity.individualId"
					).eq(
						DSL.field("Individual.id")
					),
					DSL.field(
						"Individual.suppressed"
					).notEqual(
						Boolean.TRUE
					)
				)
			).asTable(
				"IndividualIdentity"
			)
		).on(
			DSL.field(
				"IndividualIdentity.id"
			).eq(
				DSL.field("Session.userid")
			)
		);
	}

	private static final Map<String, String> _nestedFieldNamesMap =
		new HashMap<String, String>() {
			{
				put("referrer", "referrers");
				put("url", "urls");
			}
		};

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private QueryExecutor _queryExecutor;

}