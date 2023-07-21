/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.util.test;

import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portlet.PortletPreferencesImpl;

/**
 * @author Cristina González
 */
public class PortletPreferencesImplTestUtil {

	public static PortletPreferencesImpl toPortletPreferencesImpl(
			PortletPreferences portletPreferences)
		throws Exception {

		return (PortletPreferencesImpl)PortletPreferencesFactoryUtil.fromXML(
			TestPropsValues.getCompanyId(), portletPreferences.getOwnerId(),
			portletPreferences.getOwnerType(), portletPreferences.getPlid(),
			portletPreferences.getPortletId(),
			portletPreferences.getPreferences());
	}

	public static PortletPreferencesImpl toPortletPreferencesImpl(String xml)
		throws Exception {

		return (PortletPreferencesImpl)
			PortletPreferencesFactoryUtil.fromDefaultXML(xml);
	}

}