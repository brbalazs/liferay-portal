/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorTest {

	@Test
	public void testDisabledRuleShouldNotRunConditionEvaluation()
		throws Exception {

		DDMFormRule ddmFormRule = new DDMFormRule(
			"eval()", Arrays.asList("true"));

		ddmFormRule.setEnabled(false);

		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry =
			new DDMExpressionFunctionRegistry();

		EvalFunction evalFunction = new EvalFunction();

		ddmExpressionFunctionRegistry.registerDDMExpressionFunction(
			"eval", evalFunction);

		DDMFormRuleEvaluator ddmFormRuleEvaluator = new DDMFormRuleEvaluator(
			ddmFormRule, _ddmExpressionFactory, ddmExpressionFunctionRegistry);

		ddmFormRuleEvaluator.evaluate();

		Assert.assertFalse(evalFunction.hasExecuted());
	}

	@Test
	public void testEnableRuleShouldRunConditionEvaluation() throws Exception {
		DDMFormRule ddmFormRule = new DDMFormRule(
			"eval()", Arrays.asList("true"));

		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry =
			new DDMExpressionFunctionRegistry();

		EvalFunction evalFunction = new EvalFunction();

		ddmExpressionFunctionRegistry.registerDDMExpressionFunction(
			"eval", evalFunction);

		DDMFormRuleEvaluator ddmFormRuleEvaluator = new DDMFormRuleEvaluator(
			ddmFormRule, _ddmExpressionFactory, ddmExpressionFunctionRegistry);

		ddmFormRuleEvaluator.evaluate();

		Assert.assertTrue(evalFunction.hasExecuted());
	}

	@Test
	public void testFalseConditionShouldNotRunActionEvaluation()
		throws Exception {

		DDMFormRule ddmFormRule = new DDMFormRule(
			"false", Arrays.asList("eval()"));

		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry =
			new DDMExpressionFunctionRegistry();

		EvalFunction evalFunction = new EvalFunction();

		ddmExpressionFunctionRegistry.registerDDMExpressionFunction(
			"eval", evalFunction);

		DDMFormRuleEvaluator ddmFormRuleEvaluator = new DDMFormRuleEvaluator(
			ddmFormRule, _ddmExpressionFactory, ddmExpressionFunctionRegistry);

		ddmFormRuleEvaluator.evaluate();

		Assert.assertFalse(evalFunction.hasExecuted());
	}

	@Test
	public void testTrueConditionShouldRunActionEvaluation() throws Exception {
		DDMFormRule ddmFormRule = new DDMFormRule(
			"true", Arrays.asList("eval()"));

		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry =
			new DDMExpressionFunctionRegistry();

		EvalFunction evalFunction = new EvalFunction();

		ddmExpressionFunctionRegistry.registerDDMExpressionFunction(
			"eval", evalFunction);

		DDMFormRuleEvaluator ddmFormRuleEvaluator = new DDMFormRuleEvaluator(
			ddmFormRule, _ddmExpressionFactory, ddmExpressionFunctionRegistry);

		ddmFormRuleEvaluator.evaluate();

		Assert.assertTrue(evalFunction.hasExecuted());
	}

	private final DDMExpressionFactory _ddmExpressionFactory =
		new DDMExpressionFactoryImpl();

	private static class EvalFunction implements DDMExpressionFunction {

		public Object evaluate(Object... parameters) {
			_executed = true;

			return true;
		}

		public boolean hasExecuted() {
			return _executed;
		}

		private boolean _executed;

	}

}