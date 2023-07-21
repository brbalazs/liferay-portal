/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import java.lang.reflect.Method;

/**
 * @author Brian Wing Shun Chan
 * @author Wesley Gong
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.portal.kernel.transaction.TransactionInvokerUtil}
 */
@Deprecated
public class ServiceBeanMethodInvocationFactoryUtil {

	public static ServiceBeanMethodInvocationFactory
		getServiceBeanMethodInvocationFactory() {

		return _serviceBeanMethodInvocationFactory;
	}

	public static Object proceed(
			Object target, Class<?> targetClass, Method method,
			Object[] arguments, String[] methodInterceptorBeanIds)
		throws Exception {

		return getServiceBeanMethodInvocationFactory().proceed(
			target, targetClass, method, arguments, methodInterceptorBeanIds);
	}

	public void setServiceBeanMethodInvocationFactory(
		ServiceBeanMethodInvocationFactory serviceBeanMethodInvocationFactory) {

		_serviceBeanMethodInvocationFactory =
			serviceBeanMethodInvocationFactory;
	}

	private static ServiceBeanMethodInvocationFactory
		_serviceBeanMethodInvocationFactory;

}