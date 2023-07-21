/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.util.GetterUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=join",
	service = DDMExpressionFunction.class
)
public class JoinFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		if (!(parameters[0] instanceof JSONArray)) {
			throw new IllegalArgumentException("JSONArray is expected");
		}

		JSONArray jsonArray = (JSONArray)parameters[0];

		StringBundler sb = new StringBundler((jsonArray.length() * 2) - 1);

		for (int i = 0; i < jsonArray.length(); i++) {
			sb.append(GetterUtil.getString(jsonArray.get(i)));

			if (i < (jsonArray.length() - 1)) {
				sb.append(CharPool.COMMA);
			}
		}

		return sb.toString();
	}

}