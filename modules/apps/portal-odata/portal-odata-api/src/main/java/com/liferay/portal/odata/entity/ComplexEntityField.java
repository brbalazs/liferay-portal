/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.odata.entity;

import com.liferay.petra.string.StringBundler;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Models an complex entity field. A Entity field with a {@code
 * EntityField.Type.COMPLEX}
 *
 * @author Cristina González
 * @review
 */
public class ComplexEntityField extends EntityField {

	/**
	 * Creates a new {@code EntityField} with its name, type and the list of
	 * entityfields contained inside it.
	 *
	 * @param  name the entity field's name
	 * @param  entityFields the list of entity Fields
	 * @review
	 */
	public ComplexEntityField(String name, List<EntityField> entityFields) {
		super(
			name, EntityField.Type.COMPLEX, locale -> name, locale -> name,
			fieldValue -> String.valueOf(fieldValue));

		if (entityFields == null) {
			_entityFieldsMap = Collections.emptyMap();
		}
		else {
			Stream<EntityField> stream = entityFields.stream();

			_entityFieldsMap = stream.map(
				entityField -> new AbstractMap.SimpleEntry<>(
					entityField.getName(), entityField)
			).collect(
				Collectors.toMap(
					entry -> entry.getKey(), entry -> entry.getValue())
			);
		}
	}

	/**
	 * Returns a Map with all the entity fields of this entity fields.
	 *
	 * @return the entity field map
	 * @review
	 */
	public Map<String, EntityField> getEntityFieldsMap() {
		return _entityFieldsMap;
	}

	@Override
	public String toString() {
		Type type = getType();

		return StringBundler.concat(
			"{entityFields: ", _entityFieldsMap, ", name: ", getName(),
			", type: ", type.name(), "}");
	}

	private final Map<String, EntityField> _entityFieldsMap;

}