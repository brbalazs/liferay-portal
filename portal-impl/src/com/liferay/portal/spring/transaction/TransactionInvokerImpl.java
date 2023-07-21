/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvoker;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import java.util.concurrent.Callable;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
public class TransactionInvokerImpl implements TransactionInvoker {

	@Override
	public <T> T invoke(
			TransactionConfig transactionConfig, Callable<T> callable)
		throws Throwable {

		TransactionExecutor transactionExecutor =
			TransactionExecutorThreadLocal.getCurrentTransactionExecutor();

		if (transactionExecutor == null) {
			transactionExecutor = _transactionExecutor;
		}

		return (T)transactionExecutor.execute(
			new TransactionAttributeAdapter(
				TransactionAttributeBuilder.build(
					true, transactionConfig.getIsolation(),
					transactionConfig.getPropagation(),
					transactionConfig.isReadOnly(),
					transactionConfig.getTimeout(),
					transactionConfig.getRollbackForClasses(),
					transactionConfig.getRollbackForClassNames(),
					transactionConfig.getNoRollbackForClasses(),
					transactionConfig.getNoRollbackForClassNames())),
			new CallableMethodInvocation(callable));
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setPlatformTransactionManager(
		PlatformTransactionManager platformTransactionManager) {
	}

	public void setTransactionExecutor(
		TransactionExecutor transactionExecutor) {

		_transactionExecutor = transactionExecutor;
	}

	private static TransactionExecutor _transactionExecutor;

	private static class CallableMethodInvocation implements MethodInvocation {

		@Override
		public Object[] getArguments() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Method getMethod() {
			throw new UnsupportedOperationException();
		}

		@Override
		public AccessibleObject getStaticPart() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getThis() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object proceed() throws Throwable {
			return _callable.call();
		}

		private CallableMethodInvocation(Callable<?> callable) {
			_callable = callable;
		}

		private final Callable<?> _callable;

	}

}