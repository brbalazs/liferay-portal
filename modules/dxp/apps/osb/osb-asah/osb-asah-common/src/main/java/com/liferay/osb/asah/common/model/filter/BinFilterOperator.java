/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 * @author Matthew Kong
 */
public class BinFilterOperator extends FilterOperator {

	public BinFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, 2, "bin", values);
	}

	@Override
	public Condition getCondition(Field field) {
		Number binSize = (Number)getValue(dataType, values.get(0));

		return DSL.floor(
			field.div(binSize)
		).multiply(
			binSize
		).eq(
			getValue(dataType, values.get(1))
		);
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return new ArrayList<EventAttributeDefinition.DataType>() {
			{
				add(EventAttributeDefinition.DataType.DURATION);
				add(EventAttributeDefinition.DataType.NUMBER);
			}
		};
	}

}