/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.messaging.async;

import com.liferay.portal.internal.messaging.async.AsyncInvokeThreadLocal;
import com.liferay.portal.internal.messaging.async.AsyncProcessCallable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.async.Async;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Map;
import java.util.concurrent.Callable;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class AsyncAdvice extends AnnotationChainableMethodAdvice<Async> {

	@Override
	public Object before(final MethodInvocation methodInvocation)
		throws Throwable {

		if (AsyncInvokeThreadLocal.isEnabled()) {
			return null;
		}

		Async async = findAnnotation(methodInvocation);

		if (async == _nullAsync) {
			return null;
		}

		Method method = methodInvocation.getMethod();

		if (method.getReturnType() != void.class) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Async annotation on method " + method.getName() +
						" does not return void");
			}

			return null;
		}

		String destinationName = null;

		if ((_destinationNames != null) && !_destinationNames.isEmpty()) {
			Object thisObject = methodInvocation.getThis();

			destinationName = _destinationNames.get(thisObject.getClass());
		}

		if (destinationName == null) {
			destinationName = _defaultDestinationName;
		}

		final String callbackDestinationName = destinationName;

		TransactionCommitCallbackUtil.registerCallback(
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					MessageBusUtil.sendMessage(
						callbackDestinationName,
						new AsyncProcessCallable(methodInvocation));

					return null;
				}

			});

		return nullResult;
	}

	public String getDefaultDestinationName() {
		return _defaultDestinationName;
	}

	@Override
	public Async getNullAnnotation() {
		return _nullAsync;
	}

	public void setDefaultDestinationName(String defaultDestinationName) {
		_defaultDestinationName = defaultDestinationName;
	}

	public void setDestinationNames(Map<Class<?>, String> destinationNames) {
		_destinationNames = destinationNames;
	}

	private static final Log _log = LogFactoryUtil.getLog(AsyncAdvice.class);

	private static final Async _nullAsync = new Async() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return Async.class;
		}

	};

	private String _defaultDestinationName;
	private Map<Class<?>, String> _destinationNames;

}