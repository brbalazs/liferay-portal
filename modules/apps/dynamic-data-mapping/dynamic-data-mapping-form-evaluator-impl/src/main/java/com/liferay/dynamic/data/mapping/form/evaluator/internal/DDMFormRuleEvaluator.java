/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluator {

	public DDMFormRuleEvaluator(
		DDMFormRule ddmFormRule, DDMExpressionFactory ddmExpressionFactory,
		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry) {

		_ddmFormRule = ddmFormRule;
		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmExpressionFunctionRegistry = ddmExpressionFunctionRegistry;
	}

	public void evaluate() {
		if (!_ddmFormRule.isEnabled()) {
			return;
		}

		boolean conditionEvaluationResult = evaluateCondition(
			_ddmFormRule.getCondition());

		if (!conditionEvaluationResult) {
			return;
		}

		for (String action : _ddmFormRule.getActions()) {
			executeAction(action);
		}
	}

	protected boolean evaluateCondition(String condition) {
		try {
			return evaluateDDMExpression(condition);
		}
		catch (DDMExpressionException ddmee) {
			if (_log.isDebugEnabled()) {
				_log.debug(ddmee, ddmee);
			}

			return false;
		}
	}

	protected boolean evaluateDDMExpression(String ddmExpressionString)
		throws DDMExpressionException {

		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createBooleanDDMExpression(
				ddmExpressionString);

		_ddmExpressionFunctionRegistry.applyDDMExpressionFunctions(
			ddmExpression);

		return ddmExpression.evaluate();
	}

	protected void executeAction(String action) {
		try {
			evaluateDDMExpression(action);
		}
		catch (DDMExpressionException ddmee) {
			if (_log.isDebugEnabled()) {
				_log.debug(ddmee, ddmee);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormRuleEvaluator.class);

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMExpressionFunctionRegistry _ddmExpressionFunctionRegistry;
	private final DDMFormRule _ddmFormRule;

}