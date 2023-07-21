/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util.test;

import com.liferay.portal.util.PrefsPropsUtil;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

/**
 * @author Adolfo Pérez
 */
public class PrefsPropsTemporarySwapper implements AutoCloseable {

	public PrefsPropsTemporarySwapper(
			String firstKey, Object firstValue, Object... keysAndValues)
		throws Exception {

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			0, false);

		_setTemporaryValue(
			portletPreferences, firstKey, String.valueOf(firstValue));

		for (int i = 0; i < keysAndValues.length; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			String value = String.valueOf(keysAndValues[i + 1]);

			_setTemporaryValue(portletPreferences, key, value);
		}

		portletPreferences.store();
	}

	@Override
	public void close() throws Exception {
		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			0, false);

		for (Map.Entry<String, String> entry : _oldValues.entrySet()) {
			portletPreferences.setValue(entry.getKey(), entry.getValue());
		}

		portletPreferences.store();
	}

	private void _setTemporaryValue(
			PortletPreferences portletPreferences, String key, String value)
		throws ReadOnlyException {

		_oldValues.put(key, PrefsPropsUtil.getString(key));

		portletPreferences.setValue(key, value);
	}

	private final Map<String, String> _oldValues = new HashMap<>();

}