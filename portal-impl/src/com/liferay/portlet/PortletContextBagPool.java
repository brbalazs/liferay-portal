/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletContextBagPool {

	public static void clear() {
		_instance._portletContextBagPool.clear();
	}

	public static PortletContextBag get(String servletContextName) {
		return _instance._get(servletContextName);
	}

	public static void put(
		String servletContextName, PortletContextBag portletContextBag) {

		_instance._put(servletContextName, portletContextBag);
	}

	public static PortletContextBag remove(String servletContextName) {
		return _instance._remove(servletContextName);
	}

	private PortletContextBagPool() {
		_portletContextBagPool = new ConcurrentHashMap<>();
	}

	private PortletContextBag _get(String servletContextName) {
		return _portletContextBagPool.get(servletContextName);
	}

	private void _put(
		String servletContextName, PortletContextBag portletContextBag) {

		_portletContextBagPool.put(servletContextName, portletContextBag);
	}

	private PortletContextBag _remove(String servletContextName) {
		return _portletContextBagPool.remove(servletContextName);
	}

	private static final PortletContextBagPool _instance =
		new PortletContextBagPool();

	private final Map<String, PortletContextBag> _portletContextBagPool;

}