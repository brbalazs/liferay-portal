/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.calendar.web.internal.upgrade.v1_0_1;

import com.liferay.calendar.model.CalendarBooking;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.upgrade.BaseUpgradePortletPreferences;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.PortletPreferences;

/**
 * @author Bryan Engler
 */
public class UpgradePortletPreferences extends BaseUpgradePortletPreferences {

	@Override
	protected String getUpdatePortletPreferencesWhereClause() {
		StringBundler sb = new StringBundler(5);

		sb.append("(preferences like '%classNameIds%");
		sb.append(
			PortalUtil.getClassNameId(
				"com.liferay.portlet.calendar.model.CalEvent"));
		sb.append("%') or (preferences like '%anyAssetType%");
		sb.append(
			PortalUtil.getClassNameId(
				"com.liferay.portlet.calendar.model.CalEvent"));
		sb.append("%')");

		return sb.toString();
	}

	@Override
	protected String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.fromXML(
				companyId, ownerId, ownerType, plid, portletId, xml);

		_replaceClassNameId(portletPreferences, "anyAssetType");
		_replaceClassNameId(portletPreferences, "classNameIds");

		return PortletPreferencesFactoryUtil.toXML(portletPreferences);
	}

	private void _replaceClassNameId(
			PortletPreferences portletPreferences, String name)
		throws Exception {

		String[] values = GetterUtil.getStringValues(
			portletPreferences.getValues(name, null));

		ArrayUtil.replace(
			values,
			String.valueOf("com.liferay.portlet.calendar.model.CalEvent"),
			String.valueOf(PortalUtil.getClassNameId(CalendarBooking.class)));

		portletPreferences.setValues(name, values);
	}

}