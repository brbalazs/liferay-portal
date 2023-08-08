/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.repository.CustomSegmentRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.MatcherUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Inácio Nery
 */
public class SegmentRepositoryImpl
	extends BaseRepository implements CustomSegmentRepository {

	public SegmentRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countByChannelId(Long channelId) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(Collections.singletonList(channelId), null)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public long countPreviewDisabledSegments(
		List<Long> dataSourceFieldMappingFieldNames, Long dataSourceId,
		FilterHelper filterHelper) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getPreviewDisabledSegmentsConditions(
				dataSourceFieldMappingFieldNames, dataSourceId, filterHelper)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public long countSegments(
		FilterHelper filterHelper,
		List<Map<String, Long>> segmentIdIdentityCounts) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			DSL.table("Segment")
		).where(
			_getConditions(
				filterHelper, _getSegmentIds(segmentIdIdentityCounts))
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public long countSegments(
		List<Long> channelIds, FilterHelper filterHelper) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(channelIds, filterHelper)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<Segment> findByChannelId(Long channelId, Pageable pageable) {
		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(Collections.singletonList(channelId), null)
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Transformation> getSegmentTransformations(
		String apply, FilterHelper filterHelper, Pageable pageable,
		@Nullable List<Long> segmentIds) {

		Matcher matcher = MatcherUtil.getMatcher(apply);

		if (!matcher.matches()) {
			throw new IllegalArgumentException(
				"Apply string " + apply + " does not match pattern " +
					MatcherUtil.getGroupByPattern());
		}

		String containsField = matcher.group("containsField");
		Field groupByField = DSL.field(matcher.group("groupByField"));

		Condition condition = filterHelper.getCondition();

		condition = condition.and(
			_getIncludeCondition(containsField, groupByField));

		if ((segmentIds != null) && !segmentIds.isEmpty()) {
			condition = condition.and(
				DSL.field(
					"id"
				).in(
					segmentIds
				));
		}

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.select(
			groupByField.as("terms"),
			DSL.count(
				DSL.field("id")
			).as(
				"totalelements"
			)
		).from(
			"Segment"
		).where(
			condition
		).groupBy(
			groupByField
		).orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Transformation(
				new Transformation.Term(
					Collections.singletonMap(
						groupByField.getName(), record.get("terms"))),
				(Integer)record.get("totalelements"))
		);
	}

	@Override
	public List<Segment> searchDynamicSegments(
		FilterHelper filterHelper, @Nullable Boolean includeAnonymousUsers,
		Pageable pageable, Set<Long> segmentIds) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"type"
			).eq(
				Segment.Type.DYNAMIC.toString()
			));

		if (StringUtils.isNotEmpty(filterHelper.getFilterString())) {
			conditions.add(filterHelper.getCondition());
		}

		conditions.add(
			DSL.not(
				DSL.and(
					DSL.field(
						"filter"
					).startsWith(
						"((dataSourceAccountPKs/accountPKs eq '"
					),
					DSL.field(
						"name"
					).startsWith(
						"Account: "
					),
					DSL.field(
						"status"
					).eq(
						"INACTIVE"
					))));

		if (includeAnonymousUsers != null) {
			if (!includeAnonymousUsers) {
				conditions.add(
					DSL.or(
						DSL.field(
							"includeAnonymousUsers"
						).isNull(),
						DSL.field(
							"includeAnonymousUsers"
						).eq(
							false
						)));
			}
			else {
				conditions.add(
					DSL.field(
						"includeAnonymousUsers"
					).eq(
						true
					));
			}
		}

		if (CollectionUtils.isNotEmpty(segmentIds)) {
			conditions.add(
				DSL.field(
					"id"
				).in(
					segmentIds
				));
		}

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			conditions
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchDynamicSegments(
		FilterHelper filterHelper, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			DSL.and(
				filterHelper.getCondition(),
				DSL.field(
					"type"
				).eq(
					Segment.Type.DYNAMIC.toString()
				))
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchPreviewDisabledSegments(
		List<Long> dataSourceFieldMappingFieldNames, Long dataSourceId,
		FilterHelper filterHelper, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getPreviewDisabledSegmentsConditions(
				dataSourceFieldMappingFieldNames, dataSourceId, filterHelper)
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchSegments(
		FilterHelper filterHelper,
		List<Map<String, Long>> segmentIdIdentityCounts, Pageable pageable) {

		if (segmentIdIdentityCounts.isEmpty()) {
			return Collections.emptyList();
		}

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(
				filterHelper, _getSegmentIds(segmentIdIdentityCounts))
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchSegments(
		List<Long> channelIds, FilterHelper filterHelper, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(channelIds, filterHelper)
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchSegments(
		Long dxpEntityId, DXPEntity.Type dxpEntityType, String state,
		Segment.Type type) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(dxpEntityId, dxpEntityType, state, type)
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	public List<Segment> searchSegments(
		String filterString, String state, String status, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"Segment"
		).where(
			_getConditions(filterString, state, status)
		).orderBy(
			getSortFields(
				_getSortFieldNameConversionMap(), pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new Segment(record.intoMap())
		);
	}

	@Override
	protected String getSortFieldName(String fieldName) {
		if (fieldName.equalsIgnoreCase("individualCount")) {
			return "identitiesCount";
		}

		return super.getSortFieldName(fieldName);
	}

	private List<Condition> _getConditions(
		FilterHelper filterHelper, List<Long> segmentIds) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"id"
			).in(
				segmentIds
			));

		if (StringUtils.isNotEmpty(filterHelper.getFilterString())) {
			conditions.add(filterHelper.getCondition());
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		List<Long> channelIds, FilterHelper filterHelper) {

		List<Condition> conditions = new ArrayList<>();

		if (!channelIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"channelId", Long.class
				).in(
					channelIds
				));
		}

		if (filterHelper != null) {
			conditions.add(filterHelper.getCondition());
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		Long dxpEntityId, DXPEntity.Type dxpEntityType, String state,
		Segment.Type type) {

		List<Condition> conditions = new ArrayList<>();

		if (dxpEntityType != null) {
			Field<Object> field = DSL.field(
				dxpEntityType.getIndividualSegmentFieldName());

			conditions.add(field.in(dxpEntityId));
		}

		if (StringUtils.isNotEmpty(state)) {
			Field<Object> field = DSL.field("state");

			conditions.add(field.notEqual(state));
		}

		if (type != null) {
			Field<Object> field = DSL.field("type");

			conditions.add(field.eq(type.toString()));
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		String filterString, String state, String status) {

		List<Condition> conditions = new ArrayList<>();

		if (StringUtils.isNotEmpty(filterString)) {
			conditions.add(
				DSL.condition(
					"{0} ~* {1}", DSL.field("filter"), DSL.val(filterString)));
		}

		if (StringUtils.isNotEmpty(state)) {
			Field<Object> field = DSL.field("state");

			conditions.add(field.eq(state));
		}

		if (StringUtils.isNotEmpty(status)) {
			Field<Object> field = DSL.field("status");

			conditions.add(field.eq(status));
		}

		return conditions;
	}

	private Condition _getIncludeCondition(String containsField, Field field) {
		if (containsField == null) {
			return DSL.noCondition();
		}

		return field.containsIgnoreCase(containsField);
	}

	private List<Condition> _getPreviewDisabledSegmentsConditions(
		List<Long> dataSourceFieldMappingFieldNames, Long dataSourceId,
		FilterHelper filterHelper) {

		List<Condition> andConditions = new ArrayList<>();

		andConditions.add(filterHelper.getCondition());

		List<Condition> orConditions = new ArrayList<>();

		orConditions.add(
			DSL.exists(
				DSL.selectOne(
				).from(
					"UNNEST(referencedDataSourceIds) AS referencedDataSourceId"
				).where(
					DSL.field(
						"referencedDataSourceId"
					).eq(
						dataSourceId
					)
				)));

		if (CollectionUtils.isEmpty(dataSourceFieldMappingFieldNames)) {
			orConditions.add(
				DSL.exists(
					DSL.selectOne(
					).from(
						"UNNEST(referencedFieldMappingFieldNames) AS " +
							"referencedFieldMappingFieldName"
					).where(
						DSL.field(
							"referencedFieldMappingFieldName"
						).in(
							dataSourceFieldMappingFieldNames
						)
					)));
		}

		andConditions.add(DSL.or(orConditions));

		return andConditions;
	}

	private List<Long> _getSegmentIds(
		List<Map<String, Long>> segmentIdIdentityCounts) {

		List<Long> segmentIds = Collections.emptyList();

		if (CollectionUtils.isNotEmpty(segmentIdIdentityCounts)) {
			Stream<Map<String, Long>> stream = segmentIdIdentityCounts.stream();

			segmentIds = stream.map(
				map -> map.get("segmentId")
			).collect(
				Collectors.toList()
			);
		}

		return segmentIds;
	}

	private Map<String, String> _getSortFieldNameConversionMap() {
		return new HashMap<String, String>() {
			{
				put("author/name", "authorName");
				put("fields/dateModified/value", "dateModified");
			}
		};
	}

	private final DSLContext _dslContext;

}