/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.exception.SystemException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class PortalClassInvoker {

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #invoke(MethodKey, Object...)}
	 */
	@Deprecated
	public static Object invoke(
			boolean newInstance, MethodKey methodKey, Object... arguments)
		throws Exception {

		return invoke(methodKey, arguments);
	}

	public static Object invoke(MethodKey methodKey, Object... arguments)
		throws Exception {

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		try {
			currentThread.setContextClassLoader(
				PortalClassLoaderUtil.getClassLoader());

			MethodHandler methodHandler = new MethodHandler(
				methodKey, arguments);

			return methodHandler.invoke();
		}
		catch (InvocationTargetException ite) {
			Throwable cause = ite.getCause();

			if (cause instanceof Error) {
				throw new SystemException(ite);
			}

			throw (Exception)cause;
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

}