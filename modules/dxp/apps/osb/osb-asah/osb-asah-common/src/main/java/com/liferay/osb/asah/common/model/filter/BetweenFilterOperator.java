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
public class BetweenFilterOperator extends FilterOperator {

	public BetweenFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, 2, "between", values);
	}

	@Override
	public Condition getCondition(Field field) {
		if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
			return DSL.and(
				field.ge(
					dslHelper.getDateValue(
						(Date)getValue(dataType, values.get(0)))),
				field.le(
					dslHelper.getDateValue(
						(Date)getValue(dataType, values.get(1)))));
		}

		return DSL.and(
			field.ge(getValue(dataType, values.get(0))),
			field.le(getValue(dataType, values.get(1))));
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return new ArrayList<EventAttributeDefinition.DataType>() {
			{
				add(EventAttributeDefinition.DataType.DATE);
				add(EventAttributeDefinition.DataType.DURATION);
				add(EventAttributeDefinition.DataType.NUMBER);
			}
		};
	}

}