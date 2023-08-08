/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Sort;

/**
 * @author Marcellus Tavares
 */
public abstract class BaseRepository {

	protected String getSortFieldName(String fieldName) {
		if (fieldName.equalsIgnoreCase("activeIndividualCount")) {
			return "activeIndividualsCount";
		}
		else if (fieldName.equalsIgnoreCase("anonymousIndividualCount")) {
			return "anonymousIndividualsCount";
		}
		else if (fieldName.equalsIgnoreCase("dateCreated")) {
			return "createDate";
		}
		else if (fieldName.equalsIgnoreCase("individualCount")) {
			return "individualsCount";
		}
		else if (fieldName.equalsIgnoreCase("knownIndividualCount")) {
			return "knownIndividualsCount";
		}
		else if (fieldName.equalsIgnoreCase("dateModified")) {
			return "modifiedDate";
		}

		return fieldName;
	}

	protected Collection<SortField<?>> getSortFields(
		Map<String, String> fieldNames, Sort sort, Table<?> table) {

		Collection<SortField<?>> sortFields = new ArrayList<>();

		if (sort == null) {
			Field<Object> field = DSL.field("id");

			sortFields.add(field.asc());

			return sortFields;
		}

		if (fieldNames == null) {
			fieldNames = Collections.emptyMap();
		}

		for (Sort.Order order : sort.toList()) {
			String fieldName = order.getProperty();

			if (fieldNames.containsKey(fieldName)) {
				fieldName = fieldNames.get(fieldName);
			}

			fieldName = getSortFieldName(fieldName);

			if (table != null) {
				fieldName = table.getName() + "." + fieldName;
			}

			if (order.isIgnoreCase() && !fieldName.endsWith("Count") &&
				!fieldName.endsWith("Date")) {

				fieldName = String.format("LOWER(%s)", fieldName);
			}

			Field<?> field = DSL.field(fieldName);

			if (order.getDirection() == Sort.Direction.ASC) {
				sortFields.add(field.asc());
			}
			else {
				sortFields.add(field.desc());
			}
		}

		return sortFields;
	}

	protected Collection<SortField<?>> getSortFields(
		Sort sort, Table<?> table) {

		return getSortFields(null, sort, table);
	}

}