/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.concurrent.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Preston Crary
 */
public class MappedMethodCallableInvocationHandler
	implements InvocationHandler {

	public MappedMethodCallableInvocationHandler(
		Object instance, boolean removeOnCall) {

		_instance = instance;
		_removeOnCall = removeOnCall;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		Callable<?> beforeCallable = null;

		if (_removeOnCall) {
			beforeCallable = _beforeCallables.remove(method);
		}
		else {
			beforeCallable = _beforeCallables.get(method);
		}

		if (beforeCallable != null) {
			beforeCallable.call();
		}

		try {
			return method.invoke(_instance, args);
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
		finally {
			Callable<?> afterCallable = null;

			if (_removeOnCall) {
				afterCallable = _afterCallables.remove(method);
			}
			else {
				afterCallable = _afterCallables.get(method);
			}

			if (afterCallable != null) {
				afterCallable.call();
			}
		}
	}

	public void putAfterCallable(Method method, Callable<?> callable) {
		_afterCallables.put(method, callable);
	}

	public void putBeforeCallable(Method method, Callable<?> callable) {
		_beforeCallables.put(method, callable);
	}

	private final Map<Method, Callable<?>> _afterCallables = new HashMap<>();
	private final Map<Method, Callable<?>> _beforeCallables = new HashMap<>();
	private final Object _instance;
	private final boolean _removeOnCall;

}