/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.rule;

import com.liferay.portal.kernel.test.ReflectionTestUtil;

import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Matthew Tambara
 */
public abstract class AbstractTestRule<C, M> implements TestRule {

	@Override
	public Statement apply(Statement statement, Description description) {
		if (description.getMethodName() != null) {
			return createMethodStatement(statement, description);
		}

		return createClassStatement(statement, description);
	}

	protected abstract void afterClass(Description description, C c)
		throws Throwable;

	protected abstract void afterMethod(
			Description description, M m, Object target)
		throws Throwable;

	protected abstract C beforeClass(Description description) throws Throwable;

	protected abstract M beforeMethod(Description description, Object target)
		throws Throwable;

	protected Statement createClassStatement(
		Statement statement, Description description) {

		return new BaseTestRule.StatementWrapper(statement) {

			@Override
			public void evaluate() throws Throwable {
				C c = beforeClass(description);

				try {
					statement.evaluate();
				}
				finally {
					afterClass(description, c);
				}
			}

		};
	}

	protected Statement createMethodStatement(
		Statement statement, Description description) {

		return new BaseTestRule.StatementWrapper(statement) {

			@Override
			public void evaluate() throws Throwable {
				Object target = inspectTarget(statement);

				M m = beforeMethod(description, target);

				try {
					statement.evaluate();
				}
				finally {
					afterMethod(description, m, target);
				}
			}

			public Object inspectTarget(Statement statement) {
				while (statement instanceof BaseTestRule.StatementWrapper) {
					BaseTestRule.StatementWrapper statementWrapper =
						(BaseTestRule.StatementWrapper)statement;

					statement = statementWrapper.getStatement();
				}

				if (statement instanceof InvokeMethod ||
					statement instanceof RunAfters ||
					statement instanceof RunBefores) {

					return ReflectionTestUtil.getFieldValue(
						statement, "target");
				}
				else if (statement instanceof ExpectException) {
					return inspectTarget(
						ReflectionTestUtil.<Statement>getFieldValue(
							statement, "next"));
				}
				else if (statement instanceof FailOnTimeout) {
					return inspectTarget(
						ReflectionTestUtil.<Statement>getFieldValue(
							statement, "originalStatement"));
				}

				throw new IllegalStateException(
					"Unknow statement " + statement);
			}

		};
	}

}