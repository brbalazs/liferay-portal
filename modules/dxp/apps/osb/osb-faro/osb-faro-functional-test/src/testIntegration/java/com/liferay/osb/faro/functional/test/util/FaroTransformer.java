/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.util;

import com.liferay.portal.kernel.util.StringUtil;

import cucumber.api.Transformer;

/**
 * @author Shinn Lok
 */
public class FaroTransformer extends Transformer<String> {

	@Override
	public String transform(String value) {
		return FaroTestDataUtil.parsePlaceholders(
			StringUtil.unquote(value.trim()));
	}

}