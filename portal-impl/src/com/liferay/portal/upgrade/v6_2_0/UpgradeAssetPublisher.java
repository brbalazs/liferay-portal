/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.upgrade.v6_2_0.BaseUpgradePortletPreferences;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.upgrade.v6_2_0.util.RSSUtil;

import javax.portlet.PortletPreferences;

/**
 * @author Eduardo García
 * @author Jorge Ferrer
 */
public class UpgradeAssetPublisher extends BaseUpgradePortletPreferences {

	@Override
	protected String[] getPortletIds() {
		return new String[] {"101_INSTANCE_%"};
	}

	@Override
	protected String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.fromXML(
				companyId, ownerId, ownerType, plid, portletId, xml);

		upgradeRss(portletPreferences);
		upgradeScopeIds(portletPreferences);

		return PortletPreferencesFactoryUtil.toXML(portletPreferences);
	}

	protected void upgradeRss(PortletPreferences portletPreferences)
		throws Exception {

		String rssFormat = GetterUtil.getString(
			portletPreferences.getValue("rssFormat", null));

		if (Validator.isNotNull(rssFormat)) {
			portletPreferences.setValue(
				"rssFeedType",
				RSSUtil.getFeedType(
					RSSUtil.getFormatType(rssFormat),
					RSSUtil.getFormatVersion(rssFormat)));
		}

		portletPreferences.reset("rssFormat");
	}

	protected void upgradeScopeIds(PortletPreferences portletPreferences)
		throws Exception {

		String defaultScope = GetterUtil.getString(
			portletPreferences.getValue("defaultScope", null));

		if (Validator.isNull(defaultScope)) {
			return;
		}

		if (defaultScope.equals("true")) {
			portletPreferences.setValues(
				"scopeIds", new String[] {"Group_default"});
		}
		else if (!defaultScope.equals("false")) {
			portletPreferences.setValues(
				"scopeIds", new String[] {defaultScope});
		}

		portletPreferences.reset("defaultScope");
	}

}