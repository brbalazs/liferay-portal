/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.repository.CustomEventDefinitionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
public class EventDefinitionRepositoryImpl
	extends BaseRepository implements CustomEventDefinitionRepository {

	public EventDefinitionRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countEventDefinitions(
		@Nullable Boolean blocked,
		@Nullable EventDefinition.BlockedReasonType blockedReasonType,
		@Nullable Boolean hidden, @Nullable String keyword,
		@Nullable EventDefinition.Type type) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"EventDefinition"
		).where(
			_getConditions(blocked, blockedReasonType, hidden, keyword, type)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<EventDefinition> findByNameIn(Collection<String> names) {
		if (names.isEmpty()) {
			return Collections.emptyList();
		}

		Table<Record> eventDefinitionTable = DSL.table("EventDefinition");

		SelectSelectStep<Record> selectSelectStep = _dslContext.select(
			eventDefinitionTable.asterisk());

		return selectSelectStep.from(
			"EventDefinition"
		).where(
			DSL.field(
				"name"
			).in(
				names
			)
		).fetch(
			record -> new EventDefinition(record.intoMap())
		);
	}

	@Override
	public List<String> getEventDefinitionApplicationIds(boolean hidden) {
		return _dslContext.selectDistinct(
			DSL.field("applicationId")
		).from(
			"EventDefinition"
		).where(
			_getConditions(null, null, hidden, null, null)
		).fetch(
			record -> String.valueOf(record.get("applicationId"))
		);
	}

	@Override
	public List<String> getEventDefinitionNames(boolean hidden) {
		return _dslContext.select(
			DSL.field("name")
		).from(
			"EventDefinition"
		).where(
			_getConditions(null, null, hidden, null, null)
		).fetch(
			record -> String.valueOf(record.get("name"))
		);
	}

	@Override
	public List<EventDefinition> searchEventDefinitions(
		@Nullable Boolean blocked,
		@Nullable EventDefinition.BlockedReasonType blockedReasonType,
		@Nullable Boolean hidden, @Nullable String keyword, Pageable pageable,
		@Nullable EventDefinition.Type type) {

		return _dslContext.select(
			DSL.field("blocked"), DSL.field("description"),
			DSL.field("displayname"), DSL.field("hidden"), DSL.field("id"),
			DSL.field("name"), DSL.field("type"),
			DSL.field("blockedlastseendate"), DSL.field("blockedlastseenurl")
		).from(
			"EventDefinition"
		).where(
			_getConditions(blocked, blockedReasonType, hidden, keyword, type)
		).orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new EventDefinition(record.intoMap())
		);
	}

	private List<Condition> _getConditions(
		Boolean blocked, EventDefinition.BlockedReasonType blockedReasonType,
		Boolean hidden, String keyword, EventDefinition.Type type) {

		List<Condition> conditions = new ArrayList<>();

		if (blocked != null) {
			Field<Object> field = DSL.field("blocked");

			conditions.add(field.eq(blocked));
		}

		if (blockedReasonType != null) {
			Field<Object> field = DSL.field("blockedReasonType");

			conditions.add(field.eq(blockedReasonType.toString()));
		}

		if (hidden != null) {
			Field<Object> field = DSL.field("EventDefinition.hidden");

			conditions.add(field.eq(hidden));
		}

		if ((type != null) && !type.equals(EventDefinition.Type.ALL)) {
			Field<Object> field = DSL.field("type");

			conditions.add(field.in(type.toString()));
		}

		if (StringUtils.isNotEmpty(keyword)) {
			Field<Object> nameField = DSL.field("name");

			Condition condition = nameField.containsIgnoreCase(keyword);

			if ((blocked == null) || !blocked) {
				Field<Object> descriptionField = DSL.field("description");
				Field<Object> displayNameField = DSL.field("displayName");

				condition = DSL.or(
					condition, descriptionField.containsIgnoreCase(keyword),
					displayNameField.containsIgnoreCase(keyword));
			}

			conditions.add(condition);
		}

		return conditions;
	}

	private final DSLContext _dslContext;

}