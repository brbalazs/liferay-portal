/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public abstract class BaseStrutsPortletFindActionHelper
	extends BaseFindActionHelper implements StrutsPortletFindActionHelper {

	@Override
	public abstract String getStrutsAction(
		HttpServletRequest request, String portletId);

	@Override
	protected void addRequiredParameters(
		HttpServletRequest request, String portletId, PortletURL portletURL) {

		portletURL.setParameter(
			"struts_action", getStrutsAction(request, portletId));
	}

}