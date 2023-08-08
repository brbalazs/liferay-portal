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
import org.jooq.impl.DSL;

/**
 * @author Leslie Wong
 */
public class NotContainsFilterOperator extends FilterOperator {

	public NotContainsFilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		List<String> values) {

		super(dataType, dslHelper, "notContains", values);
	}

	@Override
	public Condition getCondition(Field field) {
		String value = (String)getValue(dataType, values.get(0));

		return DSL.not(dslHelper.containsSubstring(field.getName(), value));
	}

	@Override
	protected List<EventAttributeDefinition.DataType> getSupportedDataTypes() {
		return Collections.singletonList(
			EventAttributeDefinition.DataType.STRING);
	}

}