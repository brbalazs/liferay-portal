/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.spring.aop;

/**
 * @author Tina Tian
 */
public interface AdvisedSupport {

	public Class<?>[] getProxiedInterfaces();

	public Object getTarget();

	public void setTarget(Object target);

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setTarget(Object target, Class<?> targetClass);

}