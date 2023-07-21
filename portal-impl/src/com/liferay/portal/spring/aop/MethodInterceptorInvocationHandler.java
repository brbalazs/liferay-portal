/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class MethodInterceptorInvocationHandler implements InvocationHandler {

	public MethodInterceptorInvocationHandler(
		Object target, List<MethodInterceptor> methodInterceptors) {

		if (target == null) {
			throw new NullPointerException("Target is null");
		}

		_target = target;

		if (methodInterceptors == null) {
			throw new NullPointerException("Method interceptors is null");
		}

		if (methodInterceptors.isEmpty()) {
			throw new IllegalArgumentException("Method interceptors is empty");
		}

		for (int i = 0; i < methodInterceptors.size(); i++) {
			if (methodInterceptors.get(i) == null) {
				throw new IllegalArgumentException(
					"Method interceptor " + i + " is null");
			}
		}

		_methodInterceptors = methodInterceptors;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			new ServiceBeanMethodInvocation(_target, method, arguments);

		serviceBeanMethodInvocation.setMethodInterceptors(_methodInterceptors);

		return serviceBeanMethodInvocation.proceed();
	}

	private final List<MethodInterceptor> _methodInterceptors;
	private final Object _target;

}