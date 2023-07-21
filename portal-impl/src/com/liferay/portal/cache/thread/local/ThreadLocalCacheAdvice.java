/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cache.thread.local;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.thread.local.Lifecycle;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCachable;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCache;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class ThreadLocalCacheAdvice
	extends AnnotationChainableMethodAdvice<ThreadLocalCachable> {

	@Override
	public ThreadLocalCachable getNullAnnotation() {
		return _nullThreadLocalCacheable;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		ThreadLocalCachable threadLocalCachable = findAnnotation(
			methodInvocation);

		if (threadLocalCachable == _nullThreadLocalCacheable) {
			return methodInvocation.proceed();
		}

		ThreadLocalCache<Object> threadLocalCache =
			ThreadLocalCacheManager.getThreadLocalCache(
				threadLocalCachable.scope(), methodInvocation.getMethod());

		String cacheKey = _getCacheKey(methodInvocation.getArguments());

		Object value = threadLocalCache.get(cacheKey);

		if (value != null) {
			if (value == nullResult) {
				return null;
			}

			return value;
		}

		Object result = methodInvocation.proceed();

		if (result == null) {
			threadLocalCache.put(cacheKey, nullResult);
		}
		else {
			threadLocalCache.put(cacheKey, result);
		}

		return result;
	}

	private String _getCacheKey(Object[] arguments) {
		if (arguments.length == 1) {
			return StringUtil.toHexString(arguments[0]);
		}

		StringBundler sb = new StringBundler((arguments.length * 2) - 1);

		for (int i = 0; i < arguments.length; i++) {
			sb.append(StringUtil.toHexString(arguments[i]));

			if ((i + 1) < arguments.length) {
				sb.append(StringPool.POUND);
			}
		}

		return sb.toString();
	}

	private static final ThreadLocalCachable _nullThreadLocalCacheable =
		new ThreadLocalCachable() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return ThreadLocalCachable.class;
			}

			@Override
			public Lifecycle scope() {
				return null;
			}

		};

}