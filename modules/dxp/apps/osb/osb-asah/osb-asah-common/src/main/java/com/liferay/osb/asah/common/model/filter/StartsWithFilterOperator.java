/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author Leslie Wong
 */
public class StartsWithFilterOperator extends FilterOperator {

	public StartsWithFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, "startsWith", values);
	}

	@Override
	public Condition getCondition(Field field) {
		return field.startsWithIgnoreCase(getValue(dataType, values.get(0)));
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return Collections.singletonList(
			EventAttributeDefinition.DataType.STRING);
	}

}