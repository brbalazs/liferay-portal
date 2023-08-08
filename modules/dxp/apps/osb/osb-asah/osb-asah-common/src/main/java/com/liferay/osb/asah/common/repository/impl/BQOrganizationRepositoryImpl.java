/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.common.repository.CustomBQOrganizationRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.repository.util.ConditionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Rachael Koestartyo
 */
public class BQOrganizationRepositoryImpl
	extends BaseRepository implements CustomBQOrganizationRepository {

	public BQOrganizationRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQOrganization")
			));
	}

	@Override
	public long countByDataSourceIdsAndName(
		List<Long> dataSourceIds, @Nullable String name) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQOrganization"
			).where(
				ConditionUtil.toConditions(
					dataSourceIds, name, new String[] {"name"})
			));
	}

	@Override
	public long countByName(@Nullable String name) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQOrganization"
			).where(
				_getCondition(null, name, null)
			));
	}

	@Override
	public long countOrganizationFieldValuesCustom(
		Long channelId, String fieldName, String filterString) {

		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		if (!bqFieldMappingOptional.isPresent()) {
			return 0;
		}

		return _queryExecutor.queryForLong(
			_getOrganizationFieldsSelectConditionStep(
				channelId, fieldName, filterString,
				_dslContext.select(
					DSL.countDistinct(
						DSL.field("ExpandoValue_" + fieldName + ".value")
					).as(
						"totalElements"
					))));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQOrganization")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public List<BQOrganization> findAll() {
		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQOrganization::new, selectSelectStep.from("BQOrganization"));
	}

	@Override
	public Optional<BQOrganization> findByDataSourceIdAndOrganizationId(
		Long dataSourceId, Long organizationId) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForObject(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				_getCondition(
					dataSourceId, null,
					Collections.singletonList(organizationId))
			));
	}

	@Override
	public List<BQOrganization> findByDataSourceIdAndOrganizationIdIn(
		Long dataSourceId, Collection<Long> organizationIds) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				_getCondition(dataSourceId, null, organizationIds)
			));
	}

	@Override
	public Optional<BQOrganization> findById(String bqOrganizationId) {
		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForObject(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				DSL.field(
					"id"
				).eq(
					bqOrganizationId
				)
			));
	}

	@Override
	public List<BQOrganization> findByIdIn(Collection<String> ids) {
		return _queryExecutor.queryForList(
			BQOrganization::new,
			_dslContext.selectFrom(
				"BQOrganization"
			).where(
				DSL.field(
					"id"
				).in(
					ids
				)
			));
	}

	@Override
	public List<BQOrganization> findByName(
		@Nullable String name, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				_getCondition(null, name, null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public BQOrganization insert(BQOrganization bqOrganization) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQOrganization")
			).columns(
				DSL.field("createDate", Date.class),
				DSL.field("dataSourceId", Long.class), DSL.field("id"),
				DSL.field("modifiedDate", Date.class), DSL.field("name"),
				DSL.field("organizationId", Long.class),
				DSL.field("parentOrganizationId", Long.class),
				DSL.field("treePath"), DSL.field("type")
			).values(
				bqOrganization.getCreateDate(),
				bqOrganization.getDataSourceId(), bqOrganization.getId(),
				bqOrganization.getModifiedDate(), bqOrganization.getName(),
				bqOrganization.getOrganizationId(),
				bqOrganization.getParentOrganizationId(),
				bqOrganization.getTreePath(), bqOrganization.getType()
			));

		return bqOrganization;
	}

	@Override
	public List<BQOrganization> searchBQOrganizations(
		FilterHelper filterHelper, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				filterHelper.getCondition()
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<BQOrganization> searchByDataSourceIdsAndName(
		List<Long> dataSourceIds, @Nullable String name, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQOrganization::new,
			selectSelectStep.from(
				"BQOrganization"
			).where(
				ConditionUtil.toConditions(
					dataSourceIds, name, new String[] {"name"})
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<String> searchOrganizationFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable) {

		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		if (!bqFieldMappingOptional.isPresent()) {
			return Collections.emptyList();
		}

		BQFieldMapping bqFieldMapping = bqFieldMappingOptional.get();

		if (bqFieldMapping.getRepeatable()) {
			return _getOrganizationFieldValuesCustomRepeatable(
				channelId, fieldName, filterString, pageable);
		}

		return _getOrganizationFieldValuesCustom(
			channelId, fieldName, filterString, pageable);
	}

	private List<Condition> _getCondition(
		Long dataSourceId, String name, Collection<Long> organizationIds) {

		List<Condition> conditions = new ArrayList<>();

		if (dataSourceId != null) {
			conditions.add(
				DSL.field(
					"dataSourceId"
				).eq(
					dataSourceId
				));
		}

		if (StringUtils.isNotBlank(name)) {
			conditions.add(DSL.condition("name like '%" + name + "%'"));
		}

		if ((organizationIds != null) && !organizationIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"organizationId"
				).in(
					organizationIds
				));
		}

		return conditions;
	}

	private <T extends Record> SelectConditionStep<T>
		_getOrganizationFieldsSelectConditionStep(
			Long channelId, String fieldName, String filterString,
			SelectSelectStep<T> selectSelectStep) {

		SelectJoinStep<T> selectJoinStep = selectSelectStep.from(
			DSL.table(
				"BQExpandoValue"
			).as(
				"ExpandoValue_" + fieldName
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
				DSL.field("ExpandoValue_" + fieldName + ".fieldName")
			)
		);

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"ExpandoValue_" + fieldName + ".classType"
			).eq(
				DSL.val("com.liferay.portal.kernel.model.Organization")
			));
		conditions.add(
			DSL.field(
				"ExpandoValue_" + fieldName + ".fieldName"
			).eq(
				fieldName
			));
		conditions.add(
			DSL.field(
				"FieldMapping.context"
			).eq(
				DSL.val("custom")
			));

		if (channelId != null) {
			selectJoinStep = selectJoinStep.leftJoin(
				DSL.table(
					"BQIdentityActivity"
				).as(
					"IdentityActivity"
				)
			).on(
				DSL.field(
					"ExpandoValue_" + fieldName + ".dataSourceId"
				).eq(
					DSL.field("IdentityActivity.dataSourceId")
				)
			);

			conditions.add(
				DSL.field(
					"IdentityActivity.channelId"
				).eq(
					channelId
				));
		}

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				filterString);

			conditions.add(filterExpression.getCondition());
		}

		return selectJoinStep.where(conditions);
	}

	private List<String> _getOrganizationFieldValuesCustom(
		Long channelId, String fieldName, String filterString,
		Pageable pageable) {

		SelectSelectStep<Record1<String>> selectSelectStep =
			_dslContext.selectDistinct(
				DSL.field(
					"ExpandoValue_" + fieldName + ".value", String.class
				).as(
					"organizationFieldValue"
				));

		SelectConditionStep<Record1<String>> selectConditionStep =
			_getOrganizationFieldsSelectConditionStep(
				channelId, fieldName, filterString, selectSelectStep);

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(
				recordMap.get("organizationFieldValue")),
			selectConditionStep.limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private List<String> _getOrganizationFieldValuesCustomRepeatable(
		Long channelId, String fieldName, String filterString,
		Pageable pageable) {

		SelectJoinStep selectJoinStep =
			_getOrganizationFieldValuesCustomRepeatableSelectJoinStep(
				channelId, fieldName, filterString,
				_dslContext.selectDistinct(
					DSL.field(
						"JSON_EXTRACT_SCALAR(fieldValueItem, '$')"
					).as(
						"organizationFieldValue"
					)));

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(
				recordMap.get("organizationFieldValue")),
			selectJoinStep.limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private SelectJoinStep
		_getOrganizationFieldValuesCustomRepeatableSelectJoinStep(
			Long channelId, String fieldName, String filterString,
			SelectSelectStep selectSelectStep) {

		SelectSelectStep jsonExtractSelectSelectStep = _dslContext.select(
			DSL.field(
				"JSON_EXTRACT_ARRAY(ExpandoValue_" + fieldName + ".value)"
			).as(
				"fieldValueArray"
			));

		SelectConditionStep selectConditionStep =
			_getOrganizationFieldsSelectConditionStep(
				channelId, fieldName, filterString,
				jsonExtractSelectSelectStep);

		return selectSelectStep.from(
			selectConditionStep.asTable("organizationFieldsValues")
		).crossJoin(
			DSL.table(
				"UNNEST(fieldValueArray)"
			).as(
				"fieldValueItem"
			)
		);
	}

	@Autowired
	private BQFieldMappingRepository _bqFieldMappingRepository;

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}