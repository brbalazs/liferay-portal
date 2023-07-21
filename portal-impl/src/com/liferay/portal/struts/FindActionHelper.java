/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Adolfo Pérez
 */
public interface FindActionHelper {

	public void execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception;

	public long getGroupId(long primaryKey) throws Exception;

	public String getPrimaryKeyParameterName();

	public PortletURL processPortletURL(
			HttpServletRequest request, PortletURL portletURL)
		throws Exception;

	public void setPrimaryKeyParameter(PortletURL portletURL, long primaryKey)
		throws Exception;

}