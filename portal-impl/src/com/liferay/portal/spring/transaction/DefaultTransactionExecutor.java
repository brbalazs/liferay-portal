/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.portal.kernel.transaction.TransactionLifecycleManager;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Michael C. Han
 * @author Shuyang Zhou
 */
public class DefaultTransactionExecutor
	implements TransactionExecutor, TransactionHandler {

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #DefaultTransactionExecutor(PlatformTransactionManager)}
	 */
	@Deprecated
	public DefaultTransactionExecutor() {
		_platformTransactionManager = null;
	}

	public DefaultTransactionExecutor(
		PlatformTransactionManager platformTransactionManager) {

		_platformTransactionManager = platformTransactionManager;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #commit(
	 *             TransactionAttributeAdapter, TransactionStatusAdapter)}
	 */
	@Deprecated
	@Override
	public void commit(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter) {

		_commit(
			platformTransactionManager, transactionAttributeAdapter,
			transactionStatusAdapter, null);
	}

	@Override
	public void commit(
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter) {

		_commit(
			_platformTransactionManager, transactionAttributeAdapter,
			transactionStatusAdapter, null);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #execute(
	 *             TransactionAttributeAdapter, MethodInvocation)}
	 */
	@Deprecated
	@Override
	public Object execute(
			PlatformTransactionManager platformTransactionManager,
			TransactionAttributeAdapter transactionAttributeAdapter,
			MethodInvocation methodInvocation)
		throws Throwable {

		return _execute(
			platformTransactionManager, transactionAttributeAdapter,
			methodInvocation);
	}

	@Override
	public Object execute(
			TransactionAttributeAdapter transactionAttributeAdapter,
			MethodInvocation methodInvocation)
		throws Throwable {

		return _execute(
			_platformTransactionManager, transactionAttributeAdapter,
			methodInvocation);
	}

	@Override
	public PlatformTransactionManager getPlatformTransactionManager() {
		return _platformTransactionManager;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #rollback(
	 *             Throwable, TransactionAttributeAdapter,
	 *             TransactionStatusAdapter)}
	 */
	@Deprecated
	@Override
	public void rollback(
			PlatformTransactionManager platformTransactionManager,
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable {

		throw _rollback(
			platformTransactionManager, throwable, transactionAttributeAdapter,
			transactionStatusAdapter);
	}

	@Override
	public void rollback(
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable {

		throw _rollback(
			_platformTransactionManager, throwable, transactionAttributeAdapter,
			transactionStatusAdapter);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #start(
	 *             TransactionAttributeAdapter)}
	 */
	@Deprecated
	@Override
	public TransactionStatusAdapter start(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter) {

		return _start(platformTransactionManager, transactionAttributeAdapter);
	}

	@Override
	public TransactionStatusAdapter start(
		TransactionAttributeAdapter transactionAttributeAdapter) {

		return _start(_platformTransactionManager, transactionAttributeAdapter);
	}

	private void _commit(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter,
		Throwable applicationThrowable) {

		Throwable throwable = null;

		try {
			platformTransactionManager.commit(
				transactionStatusAdapter.getTransactionStatus());
		}
		catch (Throwable t) {
			if (applicationThrowable != null) {
				t.addSuppressed(applicationThrowable);
			}

			throwable = t;

			throw t;
		}
		finally {
			if (throwable != null) {
				TransactionLifecycleManager.fireTransactionRollbackedEvent(
					transactionAttributeAdapter, transactionStatusAdapter,
					throwable);
			}
			else {
				TransactionLifecycleManager.fireTransactionCommittedEvent(
					transactionAttributeAdapter, transactionStatusAdapter);
			}

			TransactionExecutorThreadLocal.popTransactionExecutor();
		}
	}

	private Object _execute(
			PlatformTransactionManager platformTransactionManager,
			TransactionAttributeAdapter transactionAttributeAdapter,
			MethodInvocation methodInvocation)
		throws Throwable {

		TransactionStatusAdapter transactionStatusAdapter = _start(
			platformTransactionManager, transactionAttributeAdapter);

		Object returnValue = null;

		try {
			returnValue = methodInvocation.proceed();
		}
		catch (Throwable throwable) {
			throw _rollback(
				platformTransactionManager, throwable,
				transactionAttributeAdapter, transactionStatusAdapter);
		}

		_commit(
			platformTransactionManager, transactionAttributeAdapter,
			transactionStatusAdapter, null);

		return returnValue;
	}

	private Throwable _rollback(
			PlatformTransactionManager platformTransactionManager,
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable {

		if (transactionAttributeAdapter.rollbackOn(throwable)) {
			try {
				platformTransactionManager.rollback(
					transactionStatusAdapter.getTransactionStatus());
			}
			catch (Throwable t) {
				t.addSuppressed(throwable);

				throw t;
			}
			finally {
				TransactionLifecycleManager.fireTransactionRollbackedEvent(
					transactionAttributeAdapter, transactionStatusAdapter,
					throwable);

				TransactionExecutorThreadLocal.popTransactionExecutor();
			}
		}
		else {
			_commit(
				platformTransactionManager, transactionAttributeAdapter,
				transactionStatusAdapter, throwable);
		}

		return throwable;
	}

	private TransactionStatusAdapter _start(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter) {

		TransactionStatusAdapter transactionStatusAdapter =
			new TransactionStatusAdapter(
				platformTransactionManager.getTransaction(
					transactionAttributeAdapter));

		TransactionExecutorThreadLocal.pushTransactionExecutor(this);

		TransactionLifecycleManager.fireTransactionCreatedEvent(
			transactionAttributeAdapter, transactionStatusAdapter);

		return transactionStatusAdapter;
	}

	private final PlatformTransactionManager _platformTransactionManager;

}