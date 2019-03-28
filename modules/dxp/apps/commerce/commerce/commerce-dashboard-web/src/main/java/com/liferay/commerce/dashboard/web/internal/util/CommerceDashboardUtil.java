/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.dashboard.web.internal.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Collections;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

/**
 * @author Andrea Di Giorgi
 */
public class CommerceDashboardUtil {

	public static <K, V> Map<K, V> getSessionMap(
		PortletRequest portletRequest, String name) {

		Map<K, V> map = (Map<K, V>)_getSessionValue(portletRequest, name);

		if (map == null) {
			map = Collections.emptyMap();
		}

		return map;
	}

	public static int getSessionValue(
		PortletRequest portletRequest, String name, int defaultValue) {

		return GetterUtil.getInteger(
			_getSessionValue(portletRequest, name), defaultValue);
	}

	public static long getSessionValue(
		PortletRequest portletRequest, String name, long defaultValue) {

		return GetterUtil.getLong(
			_getSessionValue(portletRequest, name), defaultValue);
	}

	public static void setSessionInteger(
		PortletRequest portletRequest, String name) {

		int value = ParamUtil.getInteger(portletRequest, name);

		setSessionValue(portletRequest, name, value);
	}

	public static void setSessionValue(
		PortletRequest portletRequest, String name, Object value) {

		PortletSession portletSession = portletRequest.getPortletSession();

		portletSession.setAttribute(
			name, value, PortletSession.APPLICATION_SCOPE);
	}

	private static final Object _getSessionValue(
		PortletRequest portletRequest, String name) {

		PortletSession portletSession = portletRequest.getPortletSession();

		return portletSession.getAttribute(
			name, PortletSession.APPLICATION_SCOPE);
	}

}