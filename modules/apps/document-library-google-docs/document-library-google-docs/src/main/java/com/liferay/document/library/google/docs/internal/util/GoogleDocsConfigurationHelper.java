/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.google.docs.internal.util;

import com.liferay.portal.kernel.util.PrefsPropsUtil;

import javax.portlet.PortletPreferences;

/**
 * @author Iván Zaera
 */
public class GoogleDocsConfigurationHelper {

	public GoogleDocsConfigurationHelper(long companyId) {
		_portletPreferences = PrefsPropsUtil.getPreferences(companyId);
	}

	public String getGoogleAppsAPIKey() {
		return _portletPreferences.getValue("googleAppsAPIKey", "");
	}

	public String getGoogleClientId() {
		return _portletPreferences.getValue("googleClientId", "");
	}

	private final PortletPreferences _portletPreferences;

}