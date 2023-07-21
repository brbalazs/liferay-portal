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

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class AnnotationTransactionAttributeSource
	implements TransactionAttributeSource {

	/**
	 * @see TransactionInterceptor#getTransactionAttribute(MethodInvocation)
	 */
	@Override
	public TransactionAttribute getTransactionAttribute(
		Method method, Class<?> targetClass) {

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

		TransactionAttribute transactionAttribute = transactionAttributes.get(
			method);

		if (transactionAttribute != null) {
			if (transactionAttribute == _nullTransactionAttribute) {
				return null;
			}

			return transactionAttribute;
		}

		Transactional transactional = AnnotationLocator.locate(
			method, targetClass, Transactional.class);

		transactionAttribute = TransactionAttributeBuilder.build(transactional);

		if (transactionAttribute == null) {
			transactionAttributes.put(method, _nullTransactionAttribute);
		}
		else {
			transactionAttributes.put(method, transactionAttribute);
		}

		return transactionAttribute;
	}

	private static final TransactionAttribute _nullTransactionAttribute =
		new DefaultTransactionAttribute();

	private final ConcurrentMap<Class<?>, Map<Method, TransactionAttribute>>
		_transactionAttributes = new ConcurrentHashMap<>();

}