/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.converter.helper;

import java.util.Map;

import org.jooq.Condition;

/**
 * @author Michael Bowerman
 */
public interface FilterStringConverterHelper {

	public Map<String, String> getFieldNameConversionMap();

	public default String getFilterType() {
		return null;
	}

	public Condition getLogicFunctionCondition(
			String fieldName, String operator, boolean processString,
			String valueString)
		throws Exception;

	public default String getTableName() {
		return null;
	}

}