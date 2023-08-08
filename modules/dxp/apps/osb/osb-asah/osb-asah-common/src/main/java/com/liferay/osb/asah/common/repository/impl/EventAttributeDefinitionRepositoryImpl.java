/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.repository.CustomEventAttributeDefinitionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Leslie Wong
 */
public class EventAttributeDefinitionRepositoryImpl
	extends BaseRepository implements CustomEventAttributeDefinitionRepository {

	public EventAttributeDefinitionRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countEventAttributeDefinitions(
		@Nullable Long eventDefinitionId, @Nullable String keyword,
		@Nullable EventAttributeDefinition.Type type) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.select(
			DSL.countDistinct(DSL.field("name"))
		).from(
			_getEventAttributeDefinitionsTable(eventDefinitionId, keyword, type)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<EventAttributeDefinition> searchEventAttributeDefinitions(
		@Nullable Long eventDefinitionId, @Nullable String keyword,
		Pageable pageable, @Nullable EventAttributeDefinition.Type type) {

		Map<Long, EventAttributeDefinition> eventAttributeDefinitionsById =
			new LinkedHashMap<>();

		SelectSelectStep<Record> selectSelectStep = _dslContext.selectDistinct(
			DSL.table(
				"EventAttributeDefinition"
			).asterisk(),
			DSL.table(
				"EventDefinitionEventAttributeDefinition"
			).asterisk());

		Field<Object> idField = DSL.field("id");

		selectSelectStep.from(
			"EventAttributeDefinition"
		).join(
			"EventDefinitionEventAttributeDefinition"
		).on(
			idField.eq(
				DSL.field(
					"EventDefinitionEventAttributeDefinition." +
						"eventAttributeDefinitionId"))
		).where(
			_getTopDistinctEventAttributeDefinitionNamesCondition(
				eventDefinitionId, keyword, pageable, type)
		).orderBy(
			getSortFields(
				pageable.getSort(), DSL.table("EventAttributeDefinition"))
		).fetch(
		).forEach(
			record -> {
				Map<String, Object> recordMap = record.intoMap();

				Long id = (Long)recordMap.get("id");

				EventAttributeDefinition eventAttributeDefinition =
					eventAttributeDefinitionsById.computeIfAbsent(
						id,
						eventAttributeDefinitionId ->
							new EventAttributeDefinition(recordMap));

				eventAttributeDefinition.
					addEventDefinitionEventAttributeDefinition(
						new EventDefinitionEventAttributeDefinition(recordMap));
			}
		);

		return new ArrayList<>(eventAttributeDefinitionsById.values());
	}

	private List<Condition> _getConditions(
		Long eventDefinitionId, String keyword,
		EventAttributeDefinition.Type type) {

		List<Condition> conditions = new ArrayList<>();

		if (eventDefinitionId != null) {
			Field<Object> field = DSL.field("eventDefinitionId");

			conditions.add(field.eq(eventDefinitionId));
		}

		if (StringUtils.isNotEmpty(keyword)) {
			Field<Object> descriptionField = DSL.field("description");
			Field<Object> displayNameField = DSL.field("displayName");
			Field<Object> nameField = DSL.field("name");

			conditions.add(
				DSL.or(
					descriptionField.containsIgnoreCase(keyword),
					displayNameField.containsIgnoreCase(keyword),
					nameField.containsIgnoreCase(keyword)));
		}

		if ((type != null) && !type.equals(EventAttributeDefinition.Type.ALL)) {
			Field<Object> field = DSL.field("type");

			conditions.add(field.eq(type.toString()));
		}

		return conditions;
	}

	private Table<?> _getEventAttributeDefinitionsTable(
		Long eventDefinitionId, String keyword,
		EventAttributeDefinition.Type type) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.selectDistinct(
			DSL.table(
				"EventAttributeDefinition"
			).asterisk());

		Field<Object> field = DSL.field("id");

		return selectSelectStep.from(
			"EventAttributeDefinition"
		).join(
			"EventDefinitionEventAttributeDefinition"
		).on(
			field.eq(
				DSL.field(
					"EventDefinitionEventAttributeDefinition." +
						"eventAttributeDefinitionId"))
		).where(
			_getConditions(eventDefinitionId, keyword, type)
		).asTable(
			"eventAttributeDefinitionsTable"
		);
	}

	private Condition _getTopDistinctEventAttributeDefinitionNamesCondition(
		Long eventDefinitionId, String keyword, Pageable pageable,
		EventAttributeDefinition.Type type) {

		Field<Object> field = DSL.field("name");

		SelectSelectStep<Record1<Object>> selectSelectStep =
			_dslContext.selectDistinct(DSL.field("name"));

		Table<?> table = _getEventAttributeDefinitionsTable(
			eventDefinitionId, keyword, type);

		return field.in(
			selectSelectStep.from(
				table
			).groupBy(
				DSL.field(table.getName() + ".name")
			).orderBy(
				getSortFields(
					Collections.singletonMap("displayName", "name"),
					pageable.getSort(), table)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private final DSLContext _dslContext;

}