/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.petra.reflect.AnnotationLocator;
import com.liferay.portal.kernel.transaction.Transactional;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * @author Shuyang Zhou
 */
public class TransactionInterceptor implements MethodInterceptor {

	public TransactionAttribute getTransactionAttribute(
		MethodInvocation methodInvocation) {

		Object targetBean = methodInvocation.getThis();

		Class<?> targetClass = targetBean.getClass();

		Map<Method, TransactionAttribute> transactionAttributes =
			_transactionAttributes.get(targetClass);

		if (transactionAttributes == null) {
			transactionAttributes = new ConcurrentHashMap<>();

			Map<Method, TransactionAttribute> previousTransactionAttributes =
				_transactionAttributes.putIfAbsent(
					targetClass, transactionAttributes);

			if (previousTransactionAttributes != null) {
				transactionAttributes = previousTransactionAttributes;
			}
		}

		Method method = methodInvocation.getMethod();

		TransactionAttribute transactionAttribute = transactionAttributes.get(
			method);

		if (transactionAttribute == null) {
			Transactional transactional = AnnotationLocator.locate(
				method, targetClass, Transactional.class);

			transactionAttribute = TransactionAttributeBuilder.build(
				transactional);

			if (transactionAttribute == null) {
				transactionAttributes.put(method, _nullTransactionAttribute);
			}
			else {
				transactionAttributes.put(method, transactionAttribute);
			}

			return transactionAttribute;
		}

		if (transactionAttribute == _nullTransactionAttribute) {
			return null;
		}

		return transactionAttribute;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public TransactionAttributeSource getTransactionAttributeSource() {
		return transactionAttributeSource;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		TransactionAttribute transactionAttribute = getTransactionAttribute(
			methodInvocation);

		if (transactionAttribute == null) {
			return methodInvocation.proceed();
		}

		TransactionAttributeAdapter transactionAttributeAdapter =
			new TransactionAttributeAdapter(transactionAttribute);

		return transactionExecutor.execute(
			transactionAttributeAdapter, methodInvocation);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setPlatformTransactionManager(
		PlatformTransactionManager platformTransactionManager) {
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setTransactionAttributeSource(
		TransactionAttributeSource transactionAttributeSource) {
	}

	public void setTransactionExecutor(
		TransactionExecutor transactionExecutor) {

		this.transactionExecutor = transactionExecutor;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected PlatformTransactionManager platformTransactionManager;

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected TransactionAttributeSource transactionAttributeSource;

	protected TransactionExecutor transactionExecutor;

	private static final TransactionAttribute _nullTransactionAttribute =
		new DefaultTransactionAttribute();

	private final ConcurrentMap<Class<?>, Map<Method, TransactionAttribute>>
		_transactionAttributes = new ConcurrentHashMap<>();

}