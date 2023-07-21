/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.increment;

import com.liferay.portal.internal.increment.BufferedIncreasableEntry;
import com.liferay.portal.internal.increment.BufferedIncrementProcessor;
import com.liferay.portal.internal.increment.BufferedIncrementProcessorUtil;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.increment.BufferedIncrement;
import com.liferay.portal.kernel.increment.Increment;
import com.liferay.portal.kernel.increment.IncrementFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.io.Serializable;

import java.lang.annotation.Annotation;

import java.util.concurrent.Callable;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author     Zsolt Berentey
 * @author     Shuyang Zhou
 */
public class BufferedIncrementAdvice
	extends AnnotationChainableMethodAdvice<BufferedIncrement> {

	@Override
	@SuppressWarnings("rawtypes")
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		BufferedIncrement bufferedIncrement = findAnnotation(methodInvocation);

		if (bufferedIncrement == _nullBufferedIncrement) {
			return null;
		}

		String configuration = bufferedIncrement.configuration();

		BufferedIncrementProcessor bufferedIncrementProcessor =
			BufferedIncrementProcessorUtil.getBufferedIncrementProcessor(
				configuration);

		if (bufferedIncrementProcessor == null) {
			return nullResult;
		}

		Object[] arguments = methodInvocation.getArguments();

		Object value = arguments[arguments.length - 1];

		CacheKeyGenerator cacheKeyGenerator =
			CacheKeyGeneratorUtil.getCacheKeyGenerator(
				BufferedIncrementAdvice.class.getName());

		for (int i = 0; i < (arguments.length - 1); i++) {
			cacheKeyGenerator.append(StringUtil.toHexString(arguments[i]));
		}

		Serializable batchKey = cacheKeyGenerator.finish();

		try {
			Increment<?> increment = IncrementFactory.createIncrement(
				bufferedIncrement.incrementClass(), value);

			BufferedIncreasableEntry bufferedIncreasableEntry =
				new BufferedIncreasableEntry(
					methodInvocation, batchKey, increment);

			TransactionCommitCallbackUtil.registerCallback(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						bufferedIncrementProcessor.process(
							bufferedIncreasableEntry);

						return null;
					}

				});
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to increment", e);
			}
		}

		return nullResult;
	}

	public void destroy() {
	}

	@Override
	public BufferedIncrement getNullAnnotation() {
		return _nullBufferedIncrement;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BufferedIncrementAdvice.class);

	private static final BufferedIncrement _nullBufferedIncrement =
		new BufferedIncrement() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return BufferedIncrement.class;
			}

			@Override
			public String configuration() {
				return "default";
			}

			@Override
			public Class<? extends Increment<?>> incrementClass() {
				return null;
			}

		};

}