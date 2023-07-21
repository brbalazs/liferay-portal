/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.persistence.ClassNameUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.model.impl.ClassNameImpl;
import com.liferay.portal.spring.hibernate.PortletTransactionManager;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * @author Shuyang Zhou
 */
public class TransactionInterceptorTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testFailOnCommit() {
		CacheRegistryUtil.clear();

		long classNameId = CounterLocalServiceUtil.increment();

		ClassName className = ClassNameUtil.create(classNameId);

		HibernateTransactionManager hibernateTransactionManager =
			(HibernateTransactionManager)
				InfrastructureUtil.getTransactionManager();

		MockPlatformTransactionManager platformTransactionManagerWrapper =
			new MockPlatformTransactionManager(hibernateTransactionManager);

		TransactionInterceptor transactionInterceptor =
			(TransactionInterceptor)PortalBeanLocatorUtil.locate(
				"transactionAdvice");

		TransactionExecutor transactionExecutor =
			transactionInterceptor.transactionExecutor;

		PlatformTransactionManager platformTransactionManager =
			ReflectionTestUtil.getAndSetFieldValue(
				transactionExecutor, "_platformTransactionManager",
				platformTransactionManagerWrapper);

		try {
			ClassNameLocalServiceUtil.addClassName(className);

			Assert.fail();
		}
		catch (RuntimeException re) {
			Assert.assertEquals(
				"MockPlatformTransactionManager", re.getMessage());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				transactionExecutor, "_platformTransactionManager",
				platformTransactionManager);
		}

		ClassName cachedClassName = (ClassName)EntityCacheUtil.getResult(
			true, ClassNameImpl.class, classNameId);

		Assert.assertNull(cachedClassName);
	}

	private static class MockPlatformTransactionManager
		extends PortletTransactionManager {

		public MockPlatformTransactionManager(
			HibernateTransactionManager hibernateTransactionManager) {

			super(
				hibernateTransactionManager,
				hibernateTransactionManager.getSessionFactory());

			_platformTransactionManager = hibernateTransactionManager;
		}

		@Override
		public void commit(TransactionStatus transactionStatus)
			throws TransactionException {

			_platformTransactionManager.rollback(transactionStatus);

			throw new RuntimeException("MockPlatformTransactionManager");
		}

		@Override
		public TransactionStatus getTransaction(
				TransactionDefinition transactionDefinition)
			throws TransactionException {

			return _platformTransactionManager.getTransaction(
				transactionDefinition);
		}

		@Override
		public void rollback(TransactionStatus transactionStatus)
			throws TransactionException {

			_platformTransactionManager.rollback(transactionStatus);
		}

		private final PlatformTransactionManager _platformTransactionManager;

	}

}