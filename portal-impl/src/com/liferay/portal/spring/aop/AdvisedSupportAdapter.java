/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.spring.aop.AdvisedSupport;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;

/**
 * @author Tina Tian
 */
public class AdvisedSupportAdapter implements AdvisedSupport {

	public AdvisedSupportAdapter(
		org.springframework.aop.framework.AdvisedSupport advisedSupport) {

		_advisedSupport = advisedSupport;
	}

	@Override
	public Class<?>[] getProxiedInterfaces() {
		return AopProxyUtils.completeProxiedInterfaces(_advisedSupport);
	}

	@Override
	public Object getTarget() {
		TargetSource targetSource = _advisedSupport.getTargetSource();

		try {
			return targetSource.getTarget();
		}
		catch (Exception e) {
			return ReflectionUtil.throwException(e);
		}
	}

	@Override
	public void setTarget(Object target) {
		_advisedSupport.setTarget(target);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public void setTarget(Object target, final Class<?> targetClass) {
		setTarget(target);
	}

	private final org.springframework.aop.framework.AdvisedSupport
		_advisedSupport;

}