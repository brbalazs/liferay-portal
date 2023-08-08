/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.util.StringUtil;

import java.time.LocalDate;

import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author Leslie Wong
 */
public abstract class FilterOperator {

	public abstract Condition getCondition(Field field);

	protected FilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		int expectedArgumentCount, String name, List<String> values) {

		_validate(dataType, expectedArgumentCount, name, values);

		this.dataType = dataType;
		this.dslHelper = dslHelper;
		this.values = values;
	}

	protected FilterOperator(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		String name, List<String> values) {

		_validate(dataType, 1, name, values);

		this.dataType = dataType;
		this.dslHelper = dslHelper;
		this.values = values;
	}

	protected abstract List<EventAttributeDefinition.DataType>
		getSupportedDataTypes();

	protected Object getValue(
		EventAttributeDefinition.DataType dataType, String value) {

		if (dataType.equals(EventAttributeDefinition.DataType.BOOLEAN)) {
			return Boolean.valueOf(value);
		}

		if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
			LocalDate localDate = LocalDate.parse(value);

			return DateUtil.toUTCDate(localDate.atStartOfDay());
		}

		if (dataType.equals(EventAttributeDefinition.DataType.DURATION)) {
			return Long.valueOf(value);
		}

		if (dataType.equals(EventAttributeDefinition.DataType.NUMBER)) {
			return Float.valueOf(value);
		}

		value = StringUtil.unquoteAndDecodeInnerQuotes(value);

		return value.replace("'", "\\'");
	}

	protected EventAttributeDefinition.DataType dataType;
	protected DSLHelper dslHelper;
	protected List<String> values;

	private void _validate(
		EventAttributeDefinition.DataType dataType, int expectedArgumentCount,
		String name, List<String> values) {

		if (expectedArgumentCount != values.size()) {
			throw new IllegalArgumentException(
				String.format(
					"Expected %d values for %s operation, got %d instead: %s",
					expectedArgumentCount, name, values.size(), values));
		}

		List<EventAttributeDefinition.DataType> supportedDataTypes =
			getSupportedDataTypes();

		if (!supportedDataTypes.contains(dataType)) {
			throw new IllegalArgumentException(
				"Filter operation " + name + " does not support data type " +
					dataType);
		}
	}

}