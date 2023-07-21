/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.util.axis;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.LoggedExceptionInInitializerError;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;

import java.lang.reflect.Field;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.cache.MethodCache;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class AxisCleanUpFilter extends BaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		try {
			processFilter(
				AxisCleanUpFilter.class.getName(), request, response,
				filterChain);
		}
		finally {
			try {
				ThreadLocal<?> cacheThreadLocal =
					(ThreadLocal<?>)_CACHE_FIELD.get(null);

				if (cacheThreadLocal != null) {
					cacheThreadLocal.remove();
				}
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	private static final Field _CACHE_FIELD;

	private static final Log _log = LogFactoryUtil.getLog(
		AxisCleanUpFilter.class);

	static {
		try {
			_CACHE_FIELD = ReflectionUtil.getDeclaredField(
				MethodCache.class, "cache");
		}
		catch (Exception e) {
			throw new LoggedExceptionInInitializerError(e);
		}
	}

}