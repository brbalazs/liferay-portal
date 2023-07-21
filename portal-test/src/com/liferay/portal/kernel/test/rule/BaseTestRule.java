/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.rule;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.callback.TestCallback;

import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Shuyang Zhou
 */
public class BaseTestRule<C, M>
	implements ArquillianClassRuleHandler, TestRule {

	public BaseTestRule(TestCallback<C, M> testCallback) {
		_testCallback = testCallback;
	}

	@Override
	public final Statement apply(
		Statement statement, final Description description) {

		String methodName = description.getMethodName();

		if (methodName != null) {
			return new StatementWrapper(statement) {

				@Override
				public void evaluate() throws Throwable {
					Object target = inspectTarget(statement);

					M m = _testCallback.beforeMethod(description, target);

					try {
						statement.evaluate();
					}
					finally {
						_testCallback.afterMethod(description, m, target);
					}
				}

			};
		}

		return new StatementWrapper(statement) {

			@Override
			public void evaluate() throws Throwable {
				C c = _testCallback.beforeClass(description);

				try {
					statement.evaluate();
				}
				finally {
					_testCallback.afterClass(description, c);
				}
			}

		};
	}

	@Override
	public void handleAfterClass(boolean enable) {
	}

	@Override
	public void handleBeforeClass(boolean enable) {
	}

	public abstract static class StatementWrapper extends Statement {

		public StatementWrapper(Statement statement) {
			this.statement = statement;
		}

		public Statement getStatement() {
			return statement;
		}

		protected final Statement statement;

	}

	protected Object inspectTarget(Statement statement) {
		while (statement instanceof StatementWrapper) {
			StatementWrapper statementWrapper = (StatementWrapper)statement;

			statement = statementWrapper.getStatement();
		}

		if (statement instanceof InvokeMethod ||
			statement instanceof RunAfters || statement instanceof RunBefores) {

			return ReflectionTestUtil.getFieldValue(statement, "target");
		}
		else if (statement instanceof ExpectException) {
			return inspectTarget(
				ReflectionTestUtil.<Statement>getFieldValue(statement, "next"));
		}
		else if (statement instanceof FailOnTimeout) {
			return inspectTarget(
				ReflectionTestUtil.<Statement>getFieldValue(
					statement, "originalStatement"));
		}

		throw new IllegalStateException("Unknow statement " + statement);
	}

	private final TestCallback<C, M> _testCallback;

}