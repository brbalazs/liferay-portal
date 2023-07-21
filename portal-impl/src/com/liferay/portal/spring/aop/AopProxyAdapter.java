/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.spring.aop.AopProxy;

import org.springframework.util.ClassUtils;

/**
 * @author Tina Tian
 */
public class AopProxyAdapter
	implements org.springframework.aop.framework.AopProxy {

	public AopProxyAdapter(AopProxy aopProxy) {
		_aopProxy = aopProxy;
	}

	@Override
	public Object getProxy() {
		return _aopProxy.getProxy(ClassUtils.getDefaultClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return _aopProxy.getProxy(classLoader);
	}

	private final AopProxy _aopProxy;

}