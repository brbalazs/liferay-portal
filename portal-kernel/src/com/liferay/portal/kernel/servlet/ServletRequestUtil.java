/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Brian Wing Shun Chan
 */
public class ServletRequestUtil {

	public static void logRequestWrappers(HttpServletRequest request) {
		HttpServletRequest tempRequest = request;

		while (true) {
			if (_log.isInfoEnabled()) {
				Class<?> clazz = tempRequest.getClass();

				_log.info("Request class " + clazz.getName());
			}

			if (tempRequest instanceof HttpServletRequestWrapper) {
				HttpServletRequestWrapper requestWrapper =
					(HttpServletRequestWrapper)tempRequest;

				tempRequest = (HttpServletRequest)requestWrapper.getRequest();
			}
			else {
				break;
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ServletRequestUtil.class);

}