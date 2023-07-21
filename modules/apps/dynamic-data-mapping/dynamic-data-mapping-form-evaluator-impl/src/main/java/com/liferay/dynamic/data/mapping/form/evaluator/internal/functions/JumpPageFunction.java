/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import java.util.Map;

/**
 * @author Inácio Nery
 */
public class JumpPageFunction implements DDMExpressionFunction {

	public JumpPageFunction(Map<Integer, Integer> pageFlow) {
		_pageFlow = pageFlow;
	}

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		Double fromPageIndex = (Double)parameters[0];
		Double toPageIndex = (Double)parameters[1];

		_pageFlow.put(fromPageIndex.intValue(), toPageIndex.intValue());

		return true;
	}

	private final Map<Integer, Integer> _pageFlow;

}