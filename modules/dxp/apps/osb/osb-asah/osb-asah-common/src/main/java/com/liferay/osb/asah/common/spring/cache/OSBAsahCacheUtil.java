/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;

/**
 * @author Alejo Ceballos
 */
public class OSBAsahCacheUtil {

	public static Class<?> extractTargetClass(Object target) {
		try {
			Class<?>[] proxiedUserInterfacesClasses =
				AopProxyUtils.proxiedUserInterfaces(target);

			Class<?> candidateTargetClass = proxiedUserInterfacesClasses[0];

			String candidateTargetClassName = candidateTargetClass.getName();

			if (candidateTargetClassName.contains("com.liferay.osb.asah")) {
				return candidateTargetClass;
			}
		}
		catch (IllegalArgumentException illegalArgumentException) {
			if (_log.isDebugEnabled()) {
				Class<?> targetClass = target.getClass();

				_log.debug(
					targetClass.getName() + ": " +
						illegalArgumentException.getMessage());
			}
		}

		return AopUtils.getTargetClass(target);
	}

	private static final Log _log = LogFactory.getLog(OSBAsahCacheUtil.class);

}