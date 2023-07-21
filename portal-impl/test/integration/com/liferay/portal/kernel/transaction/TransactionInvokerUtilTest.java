/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.transaction;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.ClassNameUtil;
import com.liferay.portal.kernel.util.PwdGenerator;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.concurrent.Callable;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class TransactionInvokerUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCommit() throws Throwable {
		final long classNameId = CounterLocalServiceUtil.increment();
		final String classNameValue = PwdGenerator.getPassword();

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						ClassName className = ClassNameUtil.create(classNameId);

						className.setValue(classNameValue);

						ClassNameUtil.update(className);

						return null;
					}

				});

			ClassName className = ClassNameLocalServiceUtil.fetchClassName(
				classNameId);

			Assert.assertNotNull(className);
			Assert.assertEquals(classNameValue, className.getClassName());
		}
		finally {
			ClassNameLocalServiceUtil.deleteClassName(classNameId);
		}
	}

	@Test
	public void testRollback() throws Throwable {
		final long classNameId = CounterLocalServiceUtil.increment();
		final Exception exception = new Exception();

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						ClassName className = ClassNameUtil.create(classNameId);

						className.setValue(PwdGenerator.getPassword());

						ClassNameUtil.update(className);

						throw exception;
					}

				});

			Assert.fail();
		}
		catch (Throwable throwable) {
			Assert.assertSame(exception, throwable);

			ClassName className = ClassNameLocalServiceUtil.fetchClassName(
				classNameId);

			Assert.assertNull(className);
		}
		finally {
			try {
				ClassNameLocalServiceUtil.deleteClassName(classNameId);
			}
			catch (Exception e) {
			}
		}
	}

	private static final TransactionConfig _transactionConfig;

	static {
		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);
		builder.setRollbackForClasses(Exception.class);

		_transactionConfig = builder.build();
	}

}