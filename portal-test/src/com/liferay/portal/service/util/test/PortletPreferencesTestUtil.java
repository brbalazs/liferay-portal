/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.util.test;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Cristina González
 */
public class PortletPreferencesTestUtil {

	public static PortletPreferences addGroupPortletPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		return addGroupPortletPreferences(layout, portlet, null);
	}

	public static PortletPreferences addGroupPortletPreferences(
			Layout layout, Portlet portlet, String defaultPreferences)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.addPortletPreferences(
			layout.getCompanyId(), layout.getGroupId(),
			PortletKeys.PREFS_OWNER_TYPE_GROUP, layout.getPlid(),
			portlet.getPortletId(), portlet, defaultPreferences);
	}

	public static PortletPreferences addLayoutPortletPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		return addLayoutPortletPreferences(layout, portlet, null);
	}

	public static PortletPreferences addLayoutPortletPreferences(
			Layout layout, Portlet portlet, String defaultPreferences)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.addPortletPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId(), portlet, defaultPreferences);
	}

	public static javax.portlet.PortletPreferences
			fetchLayoutJxPortletPreferences(Layout layout, Portlet portlet)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.fetchPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId());
	}

	public static String getPortletPreferencesXML() {
		return getPortletPreferencesXML(null, null);
	}

	public static String getPortletPreferencesXML(String name) {
		return getPortletPreferencesXML(name, null);
	}

	public static String getPortletPreferencesXML(
		String name, String[] values) {

		StringBundler sb = new StringBundler();

		sb.append("<portlet-preferences>");

		if ((name != null) || (values != null)) {
			sb.append("<preference>");

			if (name != null) {
				sb.append("<name>");
				sb.append(name);
				sb.append("</name>");
			}

			if (values != null) {
				for (String value : values) {
					sb.append("<value>");
					sb.append(value);
					sb.append("</value>");
				}
			}

			sb.append("</preference>");
		}

		sb.append("</portlet-preferences>");

		return sb.toString();
	}

	public static String getPortletPreferencesXML(String[] values) {
		return getPortletPreferencesXML(null, values);
	}

}