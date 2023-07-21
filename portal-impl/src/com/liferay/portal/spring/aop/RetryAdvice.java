/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.RetryAcceptor;
import com.liferay.portal.kernel.spring.aop.Property;
import com.liferay.portal.kernel.spring.aop.Retry;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.util.PropsValues;

import java.lang.annotation.Annotation;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Matthew Tambara
 */
public class RetryAdvice extends AnnotationChainableMethodAdvice<Retry> {

	@Override
	public Retry getNullAnnotation() {
		return _nullRetry;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Retry retry = findAnnotation(methodInvocation);

		if (retry == _nullRetry) {
			return methodInvocation.proceed();
		}

		int retries = retry.retries();

		if (retries < 0) {
			retries = PropsValues.RETRY_ADVICE_MAX_RETRIES;
		}

		int totalRetries = retries;

		if (retries >= 0) {
			retries++;
		}

		Map<String, String> properties = new HashMap<>();

		for (Property property : retry.properties()) {
			properties.put(property.name(), property.value());
		}

		Class<? extends RetryAcceptor> clazz = retry.acceptor();

		RetryAcceptor retryAcceptor = clazz.newInstance();

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)methodInvocation;

		serviceBeanMethodInvocation.mark();

		Object returnValue = null;
		Throwable throwable = null;

		while ((retries < 0) || (retries-- > 0)) {
			try {
				returnValue = serviceBeanMethodInvocation.proceed();

				if (!retryAcceptor.acceptResult(returnValue, properties)) {
					return returnValue;
				}

				if (_log.isWarnEnabled() && (retries != 0)) {
					String number = String.valueOf(retries);

					if (retries < 0) {
						number = "unlimited";
					}

					_log.warn(
						StringBundler.concat(
							"Retry on ", String.valueOf(methodInvocation),
							" for ", number, " more times due to result ",
							String.valueOf(returnValue)));
				}
			}
			catch (Throwable t) {
				throwable = t;

				if (!retryAcceptor.acceptException(t, properties)) {
					throw t;
				}

				if (_log.isWarnEnabled() && (retries != 0)) {
					String number = String.valueOf(retries);

					if (retries < 0) {
						number = "unlimited";
					}

					_log.warn(
						StringBundler.concat(
							"Retry on ", String.valueOf(methodInvocation),
							" for ", number, " more times due to exception ",
							String.valueOf(throwable)),
						throwable);
				}
			}

			serviceBeanMethodInvocation.reset();
		}

		if (throwable != null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Give up retrying on ",
						String.valueOf(methodInvocation), " after ",
						String.valueOf(totalRetries),
						" retries and rethrow last retry's exception ",
						String.valueOf(throwable)),
					throwable);
			}

			throw throwable;
		}

		if (_log.isWarnEnabled()) {
			_log.warn(
				StringBundler.concat(
					"Give up retrying on ", String.valueOf(methodInvocation),
					" after ", String.valueOf(totalRetries),
					" retries and returning the last retry's result ",
					String.valueOf(returnValue)));
		}

		return returnValue;
	}

	private static final Log _log = LogFactoryUtil.getLog(RetryAdvice.class);

	private static final Retry _nullRetry = new Retry() {

		@Override
		public Class<? extends RetryAcceptor> acceptor() {
			return null;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return Retry.class;
		}

		@Override
		public Property[] properties() {
			return null;
		}

		@Override
		public int retries() {
			return 0;
		}

	};

}