/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.spring.aop.Skip;

/**
 * @author Shuyang Zhou
 */
public class ServiceBeanMatcher implements BeanMatcher {

	public ServiceBeanMatcher() {
		this(false);
	}

	public ServiceBeanMatcher(boolean counterMatcher) {
		_counterMatcher = counterMatcher;
	}

	@Override
	public boolean match(Class<?> beanClass, String beanName) {
		if (_counterMatcher) {
			return beanName.equals(_COUNTER_SERVICE_BEAN_NAME);
		}

		if (!beanName.equals(_COUNTER_SERVICE_BEAN_NAME) &&
			beanName.endsWith(_SERVICE_SUFFIX) &&
			(beanClass.getAnnotation(Skip.class) == null)) {

			return true;
		}

		return false;
	}

	private static final String _COUNTER_SERVICE_BEAN_NAME =
		"com.liferay.counter.kernel.service.CounterLocalService";

	private static final String _SERVICE_SUFFIX = "Service";

	private final boolean _counterMatcher;

}