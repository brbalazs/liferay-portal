/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 * @author Matthew Kong
 */
public class DateGroupingFilterOperator extends FilterOperator {

	public DateGroupingFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, 2, "dateGrouping", values);
	}

	@Override
	public Condition getCondition(Field field) {
		DateGrouping dateGrouping = DateGrouping.valueOf(values.get(0));

		if (dateGrouping.equals(DateGrouping.DAY)) {
			field = dslHelper.concat(
				DSL.extract(field, DatePart.YEAR), DSL.val("-"),
				DSL.extract(field, DatePart.MONTH), DSL.val("-"),
				DSL.extract(field, DatePart.DAY));
		}
		else if (dateGrouping.equals(DateGrouping.MONTH)) {
			field = dslHelper.concat(
				DSL.extract(field, DatePart.YEAR), DSL.val("-"),
				DSL.extract(field, DatePart.MONTH));
		}
		else if (dateGrouping.equals(DateGrouping.YEAR)) {
			field = DSL.extract(field, DatePart.YEAR);
		}

		return field.eq(
			getValue(EventAttributeDefinition.DataType.STRING, values.get(1)));
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return new ArrayList<EventAttributeDefinition.DataType>() {
			{
				add(EventAttributeDefinition.DataType.DATE);
			}
		};
	}

}