/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.converter.helper;

import java.util.Collections;
import java.util.Map;

import org.jooq.Condition;

/**
 * @author Michael Bowerman
 * @author Rachael Koestartyo
 */
public class DefaultFilterStringConverterHelper
	implements FilterStringConverterHelper {

	@Override
	public Map<String, String> getFieldNameConversionMap() {
		return Collections.emptyMap();
	}

	@Override
	public Condition getLogicFunctionCondition(
			String fieldName, String operator, boolean processString,
			String valueString)
		throws Exception {

		return null;
	}

}