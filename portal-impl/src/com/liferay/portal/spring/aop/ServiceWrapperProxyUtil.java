/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Closeable;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author Shuyang Zhou
 */
public class ServiceWrapperProxyUtil {

	public static Closeable injectFieldProxy(
			Object springServiceProxy, String fieldName, Class<?> wrapperClass)
		throws Exception {

		if (!ProxyUtil.isProxyClass(springServiceProxy.getClass())) {
			throw new IllegalArgumentException(
				springServiceProxy + " is not a Spring service proxy");
		}

		AdvisedSupport advisedSupport = ServiceBeanAopProxy.getAdvisedSupport(
			springServiceProxy);

		final Object targetService = advisedSupport.getTarget();

		Class<?> clazz = targetService.getClass();

		Field field = null;

		while (clazz != null) {
			try {
				field = ReflectionUtil.getDeclaredField(clazz, fieldName);

				break;
			}
			catch (NoSuchFieldException nsfe) {
				clazz = clazz.getSuperclass();
			}
		}

		if (field == null) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"Unable to locate field ", fieldName, " in ",
					String.valueOf(targetService)));
		}

		final Field finalField = field;

		final Object previousValue = finalField.get(targetService);

		Constructor<?>[] constructors = wrapperClass.getDeclaredConstructors();

		Constructor<?> constructor = constructors[0];

		constructor.setAccessible(true);

		finalField.set(targetService, constructor.newInstance(previousValue));

		return new Closeable() {

			@Override
			public void close() throws IOException {
				try {
					finalField.set(targetService, previousValue);
				}
				catch (ReflectiveOperationException roe) {
					throw new IOException(roe);
				}
			}

		};
	}

}