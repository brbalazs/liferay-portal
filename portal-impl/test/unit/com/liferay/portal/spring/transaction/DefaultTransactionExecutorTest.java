/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionLifecycleListener;
import com.liferay.portal.kernel.transaction.TransactionLifecycleManager;
import com.liferay.portal.kernel.transaction.TransactionStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
public class DefaultTransactionExecutorTest
	extends CounterTransactionExecutorTest {

	@Before
	public void setUp() {
		TransactionLifecycleManager.register(
			_recordTransactionLifecycleListener);
	}

	@After
	public void tearDown() {
		TransactionLifecycleManager.unregister(
			_recordTransactionLifecycleListener);
	}

	@Override
	public void testCommit() throws Throwable {
		super.testCommit();

		_recordTransactionLifecycleListener.verify(null);
	}

	@Override
	public void testCommitWithAppException() throws Throwable {
		super.testCommitWithAppException();

		_recordTransactionLifecycleListener.verify(null);
	}

	@Override
	public void testCommitWithAppExceptionWithCommitException()
		throws Throwable {

		super.testCommitWithAppExceptionWithCommitException();

		_recordTransactionLifecycleListener.verify(commitException);
	}

	@Override
	public void testCommitWithCommitException() throws Throwable {
		super.testCommitWithCommitException();

		_recordTransactionLifecycleListener.verify(commitException);
	}

	@Override
	public void testRollbackOnAppException() throws Throwable {
		super.testRollbackOnAppException();

		_recordTransactionLifecycleListener.verify(appException);
	}

	@Override
	public void testRollbackOnAppExceptionWithRollbackException()
		throws Throwable {

		super.testRollbackOnAppExceptionWithRollbackException();

		_recordTransactionLifecycleListener.verify(appException);
	}

	@Override
	protected void assertTransactionExecutorThreadLocal(
		TransactionHandler transactionHandler, boolean inTransaction) {

		if (inTransaction) {
			Assert.assertSame(
				transactionHandler,
				TransactionExecutorThreadLocal.getCurrentTransactionExecutor());
		}
		else {
			Assert.assertNull(
				TransactionExecutorThreadLocal.getCurrentTransactionExecutor());
		}
	}

	@Override
	protected TransactionExecutor createTransactionExecutor(
		PlatformTransactionManager platformTransactionManager) {

		if (platformTransactionManager == null) {
			@SuppressWarnings("deprecation")
			TransactionExecutor transactionExecutor =
				new DefaultTransactionExecutor();

			return transactionExecutor;
		}

		return new DefaultTransactionExecutor(platformTransactionManager);
	}

	private final RecordTransactionLifecycleListener
		_recordTransactionLifecycleListener =
			new RecordTransactionLifecycleListener();

	private static class RecordTransactionLifecycleListener
		implements TransactionLifecycleListener {

		@Override
		public void committed(
			TransactionAttribute transactionAttribute,
			TransactionStatus transactionStatus) {

			_committed = true;
		}

		@Override
		public void created(
			TransactionAttribute transactionAttribute,
			TransactionStatus transactionStatus) {
		}

		@Override
		public void rollbacked(
			TransactionAttribute transactionAttribute,
			TransactionStatus transactionStatus, Throwable throwable) {

			_throwable = throwable;
		}

		public void verify(Throwable throwable) {
			if (throwable == null) {
				Assert.assertTrue(_committed);
			}
			else {
				Assert.assertFalse(_committed);
				Assert.assertSame(throwable, _throwable);
			}
		}

		private boolean _committed;
		private Throwable _throwable;

	}

}