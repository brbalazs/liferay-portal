/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 * @author Leslie Wong
 */
public class EqualsFilterOperator extends FilterOperator {

	public EqualsFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, "eq", values);
	}

	@Override
	public Condition getCondition(Field field) {
		String value = values.get(0);

		if (value == null) {
			return field.isNull();
		}

		if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
			return field.eq(
				dslHelper.getDateValue((Date)getValue(dataType, value)));
		}

		if (dataType.equals(EventAttributeDefinition.DataType.STRING)) {
			Field<String> stringField = DSL.field(
				field.toString(), String.class);

			return stringField.equalIgnoreCase(
				(String)getValue(dataType, value));
		}

		return field.eq(getValue(dataType, value));
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return new ArrayList<EventAttributeDefinition.DataType>() {
			{
				add(EventAttributeDefinition.DataType.BOOLEAN);
				add(EventAttributeDefinition.DataType.DATE);
				add(EventAttributeDefinition.DataType.DURATION);
				add(EventAttributeDefinition.DataType.NUMBER);
				add(EventAttributeDefinition.DataType.STRING);
			}
		};
	}

}