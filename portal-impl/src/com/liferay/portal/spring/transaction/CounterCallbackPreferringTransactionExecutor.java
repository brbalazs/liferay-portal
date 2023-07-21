/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;

/**
 * @author Shuyang Zhou
 */
public class CounterCallbackPreferringTransactionExecutor
	extends CallbackPreferringTransactionExecutor {

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #CounterCallbackPreferringTransactionExecutor(
	 *             PlatformTransactionManager)}
	 */
	@Deprecated
	public CounterCallbackPreferringTransactionExecutor() {
	}

	public CounterCallbackPreferringTransactionExecutor(
		PlatformTransactionManager platformTransactionManager) {

		super(platformTransactionManager);
	}

	@Override
	protected TransactionCallback<Object> createTransactionCallback(
		CallbackPreferringPlatformTransactionManager
			callbackPreferringPlatformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter,
		MethodInvocation methodInvocation) {

		return new CounterCallbackPreferringTransactionCallback(
			transactionAttributeAdapter, methodInvocation);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #createTransactionCallback(
	 *             CallbackPreferringPlatformTransactionManager,
	 *             TransactionAttributeAdapter, MethodInvocation)}
	 */
	@Deprecated
	@Override
	protected TransactionCallback<Object> createTransactionCallback(
		TransactionAttributeAdapter transactionAttributeAdapter,
		MethodInvocation methodInvocation) {

		return createTransactionCallback(
			null, transactionAttributeAdapter, methodInvocation);
	}

	private static class CounterCallbackPreferringTransactionCallback
		implements TransactionCallback<Object> {

		@Override
		public Object doInTransaction(TransactionStatus transactionStatus) {
			try {
				return _methodInvocation.proceed();
			}
			catch (Throwable throwable) {
				if (_transactionAttributeAdapter.rollbackOn(throwable)) {
					if (throwable instanceof RuntimeException) {
						throw (RuntimeException)throwable;
					}

					throw new ThrowableHolderException(throwable);
				}

				return new ThrowableHolder(throwable);
			}
		}

		private CounterCallbackPreferringTransactionCallback(
			TransactionAttributeAdapter transactionAttributeAdapter,
			MethodInvocation methodInvocation) {

			_transactionAttributeAdapter = transactionAttributeAdapter;
			_methodInvocation = methodInvocation;
		}

		private final MethodInvocation _methodInvocation;
		private final TransactionAttributeAdapter _transactionAttributeAdapter;

	}

}