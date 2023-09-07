/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.BQIndividual;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.model.Distribution;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.common.repository.CustomBQIndividualRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.util.BQSQLUtil;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.SelectConditionStep;
import org.jooq.SelectFinalStep;
import org.jooq.SelectForStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.SelectHavingConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnStep;
import org.jooq.SelectSeekStep1;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Robson Pastor
 */
public class BQIndividualRepositoryImpl
	extends BaseRepository implements CustomBQIndividualRepository {

	public BQIndividualRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long datasourceId, @Nullable String interestName,
		@Nullable Long notSegmentId, String query, @Nullable Long segmentId) {

		SelectJoinStep<Record1<Integer>> selectJoinStep = _getSelectJoinStep(
			interestName, segmentId,
			_dslContext.select(DSL.countDistinct(DSL.field("Individual.id"))));

		Condition condition = DSL.and(
			_getQueryCondition(query),
			DSL.or(
				DSL.field(
					"Individual.suppressed"
				).isNull(),
				DSL.field(
					"Individual.suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));

		if (channelId != null) {
			condition = condition.and(
				DSL.field(
					"IdentityActivity.channelId"
				).eq(
					channelId
				));
		}

		if (!StringUtils.isEmpty(interestName)) {
			condition = condition.and(
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).eq(
					_dslContext.select(
						DSL.max(DSL.field("recordedDate"))
					).from(
						DSL.table("BQIdentityInterestScore")
					)
				));
		}

		return _queryExecutor.queryForLong(selectJoinStep.where(condition));
	}

	@Override
	public long countBQIndividuals(
		@Nullable Long channelId, @Nullable String filterString,
		@Nullable Boolean includeAnonymousUsers, @Nullable String query,
		@Nullable Long segmentId) {

		FilterExpression filterExpression = new FilterExpression(
			channelId, filterString, true);

		Set<String> referencedTableNames = new HashSet<>();

		if (!StringUtils.isBlank(filterString)) {
			referencedTableNames = new HashSet<>(
				filterExpression.getReferencedTableNames());
		}

		if (StringUtils.isNotBlank(filterString) ||
			StringUtils.isNotBlank(query)) {

			referencedTableNames.add("Individual");
		}

		SelectJoinStep<Record1<Integer>> selectJoinStep = _dslContext.select(
			DSL.countDistinct(
				DSL.coalesce(
					DSL.field("Identity.individualId"),
					DSL.field("Identity.id")))
		).from(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			)
		);

		selectJoinStep = _getSelectJoinStep(
			channelId, includeAnonymousUsers, segmentId, referencedTableNames,
			selectJoinStep);

		return _queryExecutor.queryForLong(
			selectJoinStep.where(
				_getConditions(channelId, filterExpression, query)));
	}

	@Override
	public long countBQIndividualsCreatedSince(Date startDate) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).where(
				DSL.field(
					"Individual.createDate"
				).ge(
					_dslHelper.getDateParam(startDate)
				)
			));
	}

	@Override
	public long countBQIndividualsModifiedLast30Days(Long channelId) {
		LocalDateTime localDateTime = LocalDateTime.now(
			_timeZoneDog.getZoneId());

		localDateTime = localDateTime.minusDays(30);

		return _queryExecutor.queryForLong(
			_dslContext.select(
				DSL.countDistinct(DSL.field("Individual.id"))
			).from(
				DSL.table(
					"BQIdentityActivity"
				).as(
					"IdentityActivity"
				)
			).join(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).on(
				DSL.and(
					DSL.field(
						"IdentityActivity.individualId"
					).eq(
						DSL.field("Individual.id")
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
				DSL.and(
					DSL.field(
						"IdentityActivity.channelId", Long.class
					).eq(
						channelId
					)),
				DSL.field(
					"Individual.modifiedDate"
				).greaterOrEqual(
					DSL.field(
						"TIMESTAMP '" + DateUtil.toUTCString(localDateTime) +
							"'")
				)
			));
	}

	@Override
	public long countIndividualFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString) {

		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		if (!bqFieldMappingOptional.isPresent()) {
			return 0;
		}

		BQFieldMapping bqFieldMapping = bqFieldMappingOptional.get();

		if (bqFieldMapping.getRepeatable()) {
			return _countIndividualFieldValuesCustomRepeatable(
				channelId, fieldName, filterString);
		}

		return _countIndividualFieldValuesCustom(
			channelId, fieldName, filterString);
	}

	@Override
	public long countIndividualFieldValuesDemographics(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString) {

		Field<String> fieldValueField = DSL.field(
			_fieldNameConversionMap.getOrDefault(
				fieldName, StringUtils.lowerCase(fieldName)),
			String.class);

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.select(DSL.countDistinct(fieldValueField));

		SelectJoinStep<Record1<Integer>> selectJoinStep;

		if (channelId != null) {
			selectJoinStep = _getSelectJoinStep(null, null, selectSelectStep);
		}
		else {
			selectJoinStep = selectSelectStep.from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				));
		}

		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityActivity.channelId", Long.class
				).eq(
					channelId
				));
		}

		conditions.add(
			DSL.or(
				DSL.field(
					"Individual.suppressed"
				).isNull(),
				DSL.field(
					"Individual.suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));
		conditions.add(fieldValueField.isNotNull());
		conditions.add(fieldValueField.notEqual(""));

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				channelId, filterString);

			conditions.add(filterExpression.getCondition());
		}

		return _queryExecutor.queryForLong(selectJoinStep.where(conditions));
	}

	@Override
	public void deleteAll() {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQIndividual")
			).where(
				DSL.trueCondition()
			));
	}

	@Override
	public Optional<Individual> findByChannelIdAndId(
		@Nullable Long channelId, String id) {

		SelectJoinStep
			<Record11
				<Object, Object, Object, Object, Object, Object, Object, Object,
				 Object, Object, Object>> selectJoinStep1 = _getSelectJoinStep(
					null, null, _getIndividualSelectJoinStep());

		List<Condition> conditions = new ArrayList<>();

		Field<Object> individualIdField = DSL.field("individual.id");

		conditions.add(individualIdField.eq(id));

		conditions.add(
			DSL.or(
				DSL.field(
					"individual.suppressed"
				).isNull(),
				DSL.field(
					"individual.suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityActivity.channelId"
				).eq(
					channelId
				));
		}

		SelectSeekStep1
			<Record11
				<Object, Object, Object, Object, Object, Object, Object, Object,
				 Object, Object, Object>,
			 Object> selectSeekStep1 = selectJoinStep1.where(
				conditions
			).groupBy(
				individualIdField, DSL.field("individual.createdate"),
				DSL.field("individual.emailaddress"),
				DSL.field("individual.firstname"),
				DSL.field("individual.lastname"),
				DSL.field("individual.jobtitle"),
				DSL.field("individual.middlename"),
				DSL.field("individual.modifieddate"),
				DSL.field("individual.screenname")
			).orderBy(
				individualIdField.asc()
			);

		return _queryExecutor.queryForObject(
			record -> {
				Map<String, Object> map = new HashedMap<>(record);

				BigDecimal activitiesCount = new BigDecimal(
					String.valueOf(map.get("activitiescount")));

				return new Individual(
					activitiesCount.longValue(), new BQIndividual(map),
					(Date)map.get("lastactivitydate"));
			},
			(SelectJoinStep)_getIndividualSelectOnConditionStep(
				selectSeekStep1,
				Collections.singletonList(
					DSL.field(
						"individuals.id"
					).asc())));
	}

	@Override
	public Optional<BQIndividual> findByEmailAddress(String emailAddresses) {
		return _queryExecutor.queryForObject(
			BQIndividual::new,
			_dslContext.select(
			).from(
				DSL.table("BQIndividual")
			).where(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddresses
				)
			));
	}

	@Override
	public List<Distribution> getIndividualDistributions(
		@Nullable Long channelId, String fieldName, String fieldType,
		@Nullable Long individualSegmentId, Pageable pageable) {

		Field field = DSL.field(fieldName);
		Field selectField = null;

		if (fieldType.equalsIgnoreCase("text")) {
			selectField = DSL.lower(field);
		}
		else {
			selectField = DSL.date(field);
		}

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.or(
				DSL.field(
					"suppressed"
				).isNull(),
				DSL.field(
					"suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));

		if (channelId != null) {
			conditions.add(_getChannelIdCondition(channelId));
		}

		if (individualSegmentId != null) {
			conditions.add(
				_getIndividualSegmentIdCondition(individualSegmentId));
		}

		conditions.add(field.isNotNull());

		if (fieldType.equalsIgnoreCase("text")) {
			conditions.add(field.notEqual(""));
		}

		SelectSelectStep<Record> modifiedDateSelectSelectStep =
			_dslContext.select();

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal count = new BigDecimal(
					String.valueOf(record.get("count")));

				Object groupByFieldValue = record.get("field");

				if (fieldType.equalsIgnoreCase("date")) {
					Date date = (Date)groupByFieldValue;

					groupByFieldValue = DateUtil.toUTCString(date, "YYY-MM-dd");
				}

				return new Distribution(
					count.intValue(),
					Collections.singletonList(groupByFieldValue));
			},
			modifiedDateSelectSelectStep.select(
				selectField.as("field"),
				DSL.count(
				).as(
					"count"
				)
			).from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).where(
				conditions
			).groupBy(
				DSL.field("field")
			).orderBy(
				getSortFields(
					Collections.singletonMap("name", "field"),
					pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public BQIndividual insert(BQIndividual bqIndividual) {
		_queryExecutor.queryExecute(
			BQSQLUtil.createInsertStatement(bqIndividual));

		return bqIndividual;
	}

	@Override
	public List<Individual> searchBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String interestName,
		@Nullable Long notSegmentId, Pageable pageable, @Nullable String query,
		@Nullable Long segmentId) {

		SelectJoinStep
			<Record11
				<Object, Object, Object, Object, Object, Object, Object, Object,
				 Object, Object, Object>> selectJoinStep = _getSelectJoinStep(
					interestName, segmentId, _getIndividualSelectJoinStep());

		Condition condition = _getQueryCondition(query);

		if (channelId != null) {
			condition = condition.and(
				DSL.field(
					"IdentityActivity.channelId", Long.class
				).eq(
					channelId
				));
		}

		if (dataSourceId != null) {
			condition = condition.and(
				DSL.field(
					"IdentityActivity.dataSourceId", Long.class
				).eq(
					dataSourceId
				));
		}

		condition = condition.and(
			DSL.field(
				"IdentityActivity.eventId"
			).in(
				_eventDefinitionRepository.getEventDefinitionNames(false)
			));

		if (!StringUtils.isEmpty(interestName)) {
			condition = condition.and(
				DSL.field(
					"IdentityInterestScore.recordedDate"
				).eq(
					_dslContext.select(
						DSL.max(DSL.field("recordedDate"))
					).from(
						DSL.table("BQIdentityInterestScore")
					)
				));
		}

		if (notSegmentId != null) {
			condition = condition.and(
				DSL.field(
					"Individual.id"
				).notIn(
					DSL.select(
						DSL.field("notMembership.individualId")
					).from(
						DSL.table(
							"BQMembership"
						).as(
							"notMembership"
						)
					).where(
						DSL.field(
							"notMembership.segmentId"
						).eq(
							notSegmentId
						)
					)
				));
		}

		condition = condition.and(
			DSL.or(
				DSL.field(
					"Individual.suppressed"
				).isNull(),
				DSL.field(
					"Individual.suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));

		Collection<SortField<?>> sortFields = getSortFields(
			_fieldNameConversionMap, pageable.getSort(), null);

		SelectForUpdateStep
			<Record11
				<Object, Object, Object, Object, Object, Object, Object, Object,
				 Object, Object, Object>> selectFinalStep =
					selectJoinStep.where(
						condition
					).groupBy(
						DSL.field("Individual.birthday"),
						DSL.field("Individual.createDate"),
						DSL.field("Individual.emailAddress"),
						DSL.field("Individual.firstName"),
						DSL.field("Individual.id"),
						DSL.field("Individual.lastName"),
						DSL.field("Individual.jobTitle"),
						DSL.field("Individual.middleName"),
						DSL.field("Individual.modifiedDate"),
						DSL.field("Individual.screenName")
					).orderBy(
						sortFields
					).limit(
						pageable.getPageSize()
					).offset(
						pageable.getOffset()
					);

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal activitiesCount = new BigDecimal(
					String.valueOf(record.get("activitiescount")));

				return new Individual(
					activitiesCount.longValue(), new BQIndividual(record), null,
					(Date)record.get("lastactivitydate"));
			},
			_getIndividualSelectOnConditionStep(selectFinalStep, sortFields));
	}

	@Override
	public List<Individual> searchBQIndividuals(
		@Nullable Long channelId, String filterString, Pageable pageable,
		@Nullable String query, @Nullable Long segmentId) {

		FilterExpression filterExpression = new FilterExpression(
			channelId, filterString, true);

		Set<String> referencedTableNames = new HashSet<>(
			filterExpression.getReferencedTableNames());

		referencedTableNames.add("Individual");

		List<Field> fields = Arrays.asList(
			DSL.field("Individual.birthday"),
			DSL.field("Individual.createDate"),
			DSL.field("Individual.emailAddress"),
			DSL.field("Individual.firstName"), DSL.field("Individual.id"),
			DSL.field("Individual.jobTitle"),
			DSL.field("Individual.languageId"),
			DSL.field("Individual.lastName"),
			DSL.field("Individual.middleName"),
			DSL.field("Individual.modifiedDate"),
			DSL.field("Individual.screenName"));

		SelectJoinStep<Record> selectJoinStep = _dslContext.select(
			fields
		).from(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			)
		);

		selectJoinStep = _getSelectJoinStep(
			channelId, null, segmentId, referencedTableNames, selectJoinStep);

		return _queryExecutor.queryForList(
			record -> {
				BQIndividual bqIndividual = new BQIndividual(record);

				List<BQIndividual.Field> bqIndividualFields = new ArrayList<>();

				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "emailAddress",
						(String)record.get("emailAddress")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "firstName", (String)record.get("firstName")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "jobTitle", (String)record.get("jobTitle")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "languageId", (String)record.get("languageId")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "lastName", (String)record.get("lastName")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "modifiedDate",
						DateUtil.toUTCString(
							(Date)record.get("modifiedDate"))));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "middleName", (String)record.get("middleName")));
				bqIndividualFields.add(
					new BQIndividual.Field(
						0L, "screenName", (String)record.get("screenName")));

				if (!Objects.isNull(record.get("birthday"))) {
					bqIndividualFields.add(
						new BQIndividual.Field(
							0L, "birthday",
							DateUtil.toUTCString(
								(Date)record.get("birthday"))));
				}

				bqIndividual.setFields(bqIndividualFields);

				return new Individual(0L, bqIndividual, null, null);
			},
			selectJoinStep.where(
				_getConditions(channelId, filterExpression, query)
			).groupBy(
				fields
			).orderBy(
				getSortFields(
					_fieldNameConversionMap, pageable.getSort(),
					DSL.table("Individual"))
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<Long> searchIndividualDataSourceIds(String id) {
		return _queryExecutor.queryForList(
			recordMap -> {
				BigDecimal dataSourceIdBigDecimal = (BigDecimal)recordMap.get(
					"dataSourceId");

				return dataSourceIdBigDecimal.longValue();
			},
			_dslContext.selectDistinct(
				DSL.field("dataSourceId")
			).from(
				"BQIdentityActivity"
			).where(
				DSL.field(
					"individualId"
				).eq(
					id
				)
			));
	}

	@Override
	public List<String> searchIndividualFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable) {

		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		if (!bqFieldMappingOptional.isPresent()) {
			return Collections.emptyList();
		}

		BQFieldMapping bqFieldMapping = bqFieldMappingOptional.get();

		if (bqFieldMapping.getRepeatable()) {
			return _getIndividualFieldValuesCustomRepeatable(
				channelId, fieldName, filterString, pageable);
		}

		return _getIndividualFieldValuesCustom(
			channelId, fieldName, filterString, pageable);
	}

	@Override
	public List<String> searchIndividualFieldValuesDemographics(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable) {

		Field<String> fieldValueField = DSL.field(
			_fieldNameConversionMap.getOrDefault(
				fieldName, StringUtils.lowerCase(fieldName)),
			String.class);

		SelectSelectStep<Record1<String>> selectSelectStep =
			_dslContext.selectDistinct(fieldValueField.as("fieldValue"));

		SelectJoinStep<Record1<String>> selectJoinStep;

		if (channelId != null) {
			selectJoinStep = _getSelectJoinStep(null, null, selectSelectStep);
		}
		else {
			selectJoinStep = selectSelectStep.from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				));
		}

		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityActivity.channelId", Long.class
				).eq(
					channelId
				));
		}

		conditions.add(
			DSL.or(
				DSL.field(
					"Individual.suppressed"
				).isNull(),
				DSL.field(
					"Individual.suppressed"
				).notEqual(
					DSL.val(Boolean.TRUE)
				)));
		conditions.add(fieldValueField.isNotNull());
		conditions.add(fieldValueField.notEqual(""));

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				channelId, filterString);

			conditions.add(filterExpression.getCondition());
		}

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get("fieldValue")),
			selectJoinStep.where(
				conditions
			).orderBy(
				DSL.field("fieldValue")
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public void updateSuppressed(String id, Boolean suppressed) {
		_queryExecutor.queryExecute(
			_dslContext.update(
				DSL.table("BQIndividual")
			).set(
				DSL.field("suppressed", Boolean.class), suppressed
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	private long _countIndividualFieldValuesCustom(
		Long channelId, String fieldName, String filterString) {

		return _queryExecutor.queryForLong(
			_getIndividualFieldsSelectConditionStep(
				channelId, fieldName, filterString,
				_dslContext.select(
					DSL.countDistinct(
						DSL.field("IndividualFields_" + fieldName + ".value")
					).as(
						"totalElements"
					))));
	}

	private long _countIndividualFieldValuesCustomRepeatable(
		Long channelId, String fieldName, String filterString) {

		return _queryExecutor.queryForLong(
			_dslContext.select(
				DSL.countDistinct(
					DSL.field("individualFieldValue")
				).as(
					"totalElements"
				)
			).from(
				_getIndividualFieldValuesCustomRepeatableSelectJoinStep(
					channelId, fieldName, filterString,
					_dslContext.selectDistinct(
						DSL.field(
							"JSON_EXTRACT_SCALAR(fieldValueItem, '$')"
						).as(
							"individualFieldValue"
						)))
			));
	}

	private Condition _getChannelIdCondition(Long channelId) {
		return DSL.exists(
			DSL.selectOne(
			).from(
				DSL.table(
					"BQIdentityActivity"
				).as(
					"IdentityActivity"
				)
			).where(
				DSL.and(
					DSL.field(
						"IdentityActivity.channelId"
					).eq(
						channelId
					),
					DSL.field(
						"IdentityActivity.individualId"
					).eq(
						DSL.field("Individual.id")
					))
			));
	}

	private List<Condition> _getConditions(
		Long channelId, FilterExpression filterExpression, String query) {

		List<Condition> conditions = new ArrayList<>();

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityActivity.channelId", Long.class
				).eq(
					channelId
				));
		}

		conditions.add(
			_getQueryCondition(
				query, new String[] {"emailAddress", "firstName", "lastName"}));
		conditions.add(filterExpression.getCondition());

		return conditions;
	}

	private <T extends Record> SelectConditionStep<T>
		_getIndividualFieldsSelectConditionStep(
			Long channelId, String fieldName, String filterString,
			SelectSelectStep<T> selectSelectStep) {

		SelectJoinStep<T> selectJoinStep;

		if (channelId != null) {
			selectJoinStep = _getSelectJoinStep(null, null, selectSelectStep);
		}
		else {
			selectJoinStep = selectSelectStep.from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				));
		}

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"FieldMapping.context"
			).eq(
				DSL.val("custom")
			));
		conditions.add(
			DSL.or(
				DSL.field(
					"suppressed"
				).isNull(),
				DSL.field(
					"suppressed"
				).notEqual(
					Boolean.TRUE
				)));
		conditions.add(
			DSL.field(
				"IndividualFields_" + fieldName + ".name"
			).eq(
				fieldName
			));

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				channelId, filterString,
				FilterExpression.FilterType.INDIVIDUALS);

			conditions.add(filterExpression.getCondition());
		}

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityActivity.channelId"
				).eq(
					channelId
				));
		}

		return selectJoinStep.crossJoin(
			DSL.table(
				"UNNEST(Individual.fields)"
			).as(
				"IndividualFields_" + fieldName
			)
		).join(
			DSL.table(
				"BQFieldMapping"
			).as(
				"FieldMapping"
			)
		).on(
			DSL.field(
				"FieldMapping.fieldName"
			).eq(
				DSL.field("IndividualFields_" + fieldName + ".name")
			)
		).where(
			conditions
		);
	}

	private List<String> _getIndividualFieldValuesCustom(
		Long channelId, String fieldName, String filterString,
		Pageable pageable) {

		SelectSelectStep<Record1<String>> selectSelectStep =
			_dslContext.selectDistinct(
				DSL.field(
					"IndividualFields_" + fieldName + ".value", String.class
				).as(
					"individualFieldValue"
				));

		SelectConditionStep<Record1<String>> selectConditionStep =
			_getIndividualFieldsSelectConditionStep(
				channelId, fieldName, filterString, selectSelectStep);

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get("individualFieldValue")),
			selectConditionStep.limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private List<String> _getIndividualFieldValuesCustomRepeatable(
		Long channelId, String fieldName, String filterString,
		Pageable pageable) {

		SelectHavingConditionStep selectHavingConditionStep =
			_getIndividualFieldValuesCustomRepeatableSelectJoinStep(
				channelId, fieldName, filterString,
				_dslContext.selectDistinct(
					DSL.field(
						"JSON_EXTRACT_SCALAR(fieldValueItem, '$')"
					).as(
						"individualFieldValue"
					)));

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get("individualFieldValue")),
			selectHavingConditionStep.orderBy(
				DSL.field("individualFieldValue")
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private SelectHavingConditionStep
		_getIndividualFieldValuesCustomRepeatableSelectJoinStep(
			Long channelId, String fieldName, String filterString,
			SelectSelectStep selectSelectStep) {

		SelectSelectStep jsonExtractSelectSelectStep = _dslContext.select(
			DSL.field(
				"JSON_EXTRACT_ARRAY(IndividualFields_" + fieldName + ".value)"
			).as(
				"fieldValueArray"
			));

		SelectConditionStep selectConditionStep =
			_getIndividualFieldsSelectConditionStep(
				channelId, fieldName, filterString,
				jsonExtractSelectSelectStep);

		return selectSelectStep.from(
			selectConditionStep.asTable("individualFieldsValues")
		).crossJoin(
			DSL.table(
				"UNNEST(fieldValueArray)"
			).as(
				"fieldValueItem"
			)
		).groupBy(
			DSL.field("individualFieldValue")
		).having(
			DSL.field(
				"individualFieldValue"
			).ne(
				""
			)
		);
	}

	private Condition _getIndividualSegmentIdCondition(
		Long individualSegmentId) {

		return DSL.exists(
			DSL.selectOne(
			).from(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).where(
				DSL.and(
					DSL.field(
						"Membership.segmentId"
					).eq(
						individualSegmentId
					),
					DSL.field(
						"Membership.individualId"
					).eq(
						DSL.field("Individual.id")
					))
			));
	}

	private SelectSelectStep
		<Record11
			<Object, Object, Object, Object, Object, Object, Object, Object,
			 Object, Object, Object>> _getIndividualSelectJoinStep() {

		return _dslContext.select(
			DSL.coalesce(
				DSL.field(
					"SAFE_CAST({0} as INT64)",
					DSL.sum(
						DSL.field(
							"IdentityActivity.activitiescount", Long.class))),
				0L
			).as(
				"activitiescount"
			),
			DSL.field(
				"Individual.createdate"
			).as(
				"createdate"
			),
			DSL.field(
				"Individual.emailaddress"
			).as(
				"emailaddress"
			),
			DSL.field(
				"Individual.id"
			).as(
				"id"
			),
			DSL.field("firstname"),
			DSL.max(
				DSL.field("IdentityActivity.lastactivitydate")
			).as(
				"lastactivitydate"
			),
			DSL.field("lastname"), DSL.field("jobtitle"),
			DSL.field("middlename"),
			DSL.field(
				"Individual.modifieddate"
			).as(
				"modifieddate"
			),
			DSL.field("screenname"));
	}

	private SelectFinalStep<?> _getIndividualSelectOnConditionStep(
		SelectForStep<?> selectSeekStep, Collection<SortField<?>> sortFields) {

		List<Field> fields = Arrays.asList(
			DSL.field("activitiescount"),
			DSL.field(
				"individuals.createdate"
			).as(
				"createdate"
			),
			DSL.field(
				"individuals.emailaddress"
			).as(
				"emailaddress"
			),
			DSL.field(
				"individuals.id"
			).as(
				"id"
			),
			DSL.field(
				"individuals.firstname"
			).as(
				"firstname"
			),
			DSL.field(
				"individuals.lastactivitydate"
			).as(
				"lastactivitydate"
			),
			DSL.field(
				"individuals.lastname"
			).as(
				"lastname"
			),
			DSL.field(
				"individuals.jobtitle"
			).as(
				"jobtitle"
			),
			DSL.field(
				"individuals.middlename"
			).as(
				"middlename"
			),
			DSL.field(
				"individuals.modifieddate"
			).as(
				"modifieddate"
			),
			DSL.field(
				"individuals.screenname"
			).as(
				"screenname"
			),
			DSL.field(
				"Individual.fields"
			).as(
				"fields"
			));

		return _dslContext.with(
			"individuals"
		).as(
			selectSeekStep
		).select(
			fields
		).from(
			"individuals"
		).innerJoin(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).on(
			DSL.field(
				"Individual.id"
			).eq(
				DSL.field("individuals.id")
			)
		).orderBy(
			sortFields
		);
	}

	private <R extends Record> Table<R> _getIndividualTable(
		Condition condition, String interestName, Long segmentId,
		SelectSelectStep<R> selectSelectStep) {

		SelectJoinStep<R> selectJoinStep = _getSelectJoinStep(
			interestName, segmentId, selectSelectStep);

		return selectJoinStep.where(
			DSL.and(
				condition,
				DSL.or(
					DSL.field(
						"suppressed"
					).isNull(),
					DSL.field(
						"suppressed"
					).notEqual(
						Boolean.TRUE
					)))
		).asTable();
	}

	private Condition _getQueryCondition(String query) {
		return _getQueryCondition(query, _SEARCH_COLUMNS);
	}

	private Condition _getQueryCondition(String query, String[] searchColumns) {
		if (StringUtils.isEmpty(query)) {
			return DSL.noCondition();
		}

		List<Condition> conditions = new ArrayList<>();

		for (String word : StringUtils.split(query)) {
			List<Condition> wordConditions = new ArrayList<>();

			for (String column : searchColumns) {
				wordConditions.add(
					DSL.lower(
						DSL.field(column, String.class)
					).like(
						DSL.lower(StringUtils.wrap(word, "%"))
					));
			}

			conditions.add(DSL.or(wordConditions));
		}

		return DSL.and(conditions);
	}

	private <R extends Record> SelectJoinStep<R> _getSelectJoinStep(
		Long channelId, Boolean includeAnonymousUsers, Long segmentId,
		Set<String> referencedTableNames, SelectJoinStep<R> selectJoinStep) {

		if ((channelId != null) ||
			referencedTableNames.contains("IdentityActivity")) {

			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQIdentityActivity"
				).as(
					"IdentityActivity"
				)
			).on(
				DSL.field(
					"IdentityActivity.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			);
		}

		if (referencedTableNames.contains("Event")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQEvent"
				).as(
					"Event"
				)
			).on(
				DSL.field(
					"Event.userId"
				).eq(
					DSL.field("Identity.id")
				)
			);
		}

		if (referencedTableNames.contains("Individual")) {
			SelectOnStep selectOnStep = null;

			if (BooleanUtils.isTrue(includeAnonymousUsers)) {
				selectOnStep = selectJoinStep.leftJoin(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					));
			}
			else {
				selectOnStep = selectJoinStep.join(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					));
			}

			selectJoinStep = selectOnStep.on(
				DSL.and(
					DSL.field(
						"Identity.individualId"
					).eq(
						DSL.field("Individual.id")
					),
					DSL.or(
						DSL.field(
							"suppressed"
						).isNull(),
						DSL.field(
							"suppressed"
						).notEqual(
							Boolean.TRUE
						))));

			if (referencedTableNames.contains("ExpandoValue")) {
				Stream<String> stream = referencedTableNames.stream();

				Set<String> fields = stream.filter(
					s -> s.startsWith("IndividualFields_")
				).collect(
					Collectors.toSet()
				);

				for (String field : fields) {
					selectJoinStep = selectJoinStep.crossJoin(
						"UNNEST(Individual.fields) AS " + field);
				}
			}
		}

		if (referencedTableNames.contains("Membership")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).on(
				DSL.field(
					"Identity.id"
				).eq(
					DSL.field("Membership.identityId")
				)
			);
		}

		if (referencedTableNames.contains("Session")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).on(
				DSL.field(
					"Identity.id"
				).eq(
					DSL.field("Session.userId")
				)
			);

			if (referencedTableNames.contains("SessionReferrers")) {
				selectJoinStep = selectJoinStep.crossJoin(
					DSL.table("UNNEST(Session.referrers) AS SessionReferrer"));
			}

			if (referencedTableNames.contains("SessionUrls")) {
				selectJoinStep = selectJoinStep.crossJoin(
					DSL.table("UNNEST(Session.urls) AS SessionUrl"));
			}
		}

		if (referencedTableNames.contains("User")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQUser"
				).as(
					"User"
				)
			).on(
				DSL.field(
					"Individual.emailAddress"
				).eq(
					DSL.field("User.emailAddress")
				)
			);
		}

		if (segmentId != null) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).on(
				DSL.field(
					"Individual.id"
				).eq(
					DSL.field("Membership.individualId")
				),
				DSL.field(
					"Membership.segmentId"
				).eq(
					segmentId
				)
			);
		}

		return selectJoinStep;
	}

	private <R extends Record> SelectJoinStep<R> _getSelectJoinStep(
		String interestName, Long segmentId,
		SelectSelectStep<R> selectSelectStep) {

		SelectJoinStep<R> selectJoinStep = selectSelectStep.from(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).leftJoin(
			DSL.table(
				"BQIdentityActivity"
			).as(
				"IdentityActivity"
			)
		).on(
			DSL.field(
				"Individual.id"
			).eq(
				DSL.field("IdentityActivity.individualId")
			)
		);

		if (!StringUtils.isEmpty(interestName)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"IdentityInterestScore"
				)
			).on(
				DSL.field(
					"IdentityActivity.channelId"
				).eq(
					DSL.field("IdentityInterestScore.channelId")
				),
				DSL.field(
					"IdentityActivity.identityId"
				).eq(
					DSL.field("IdentityInterestScore.identityId")
				),
				DSL.field(
					"IdentityInterestScore.interested"
				).eq(
					DSL.val(Boolean.TRUE)
				),
				DSL.field(
					"IdentityInterestScore.keyword"
				).eq(
					DSL.val(interestName)
				)
			);
		}

		if (segmentId != null) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).on(
				DSL.field(
					"Individual.id"
				).eq(
					DSL.field("Membership.individualId")
				),
				DSL.field(
					"Membership.segmentId"
				).eq(
					segmentId
				)
			);
		}

		return selectJoinStep;
	}

	private static final String[] _SEARCH_COLUMNS = {
		"emailAddress", "firstName", "jobTitle", "lastName", "middleName"
	};

	@Autowired
	private BQFieldMappingRepository _bqFieldMappingRepository;

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

	private final Map<String, String> _fieldNameConversionMap =
		new HashMap<String, String>() {
			{
				put("additionalName", "middlename");
				put("address", "addresses");
				put("birthdate", "birthday");
				put("demographics/familyName/value", "lastname");
				put("demographics/givenName/value", "firstname");
				put("demographics/jobTitle/value", "jobtitle");
				put("familyName", "lastname");
				put("givenName", "firstname");
			}
		};

	@Autowired
	private QueryExecutor _queryExecutor;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}