/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.struts.FindAction;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
public class FindUserAction extends FindAction {

	@Override
	protected long getGroupId(long primaryKey) throws Exception {
		return 0;
	}

	@Override
	protected String getPrimaryKeyParameterName() {
		return "p_u_i_d";
	}

	@Override
	protected String getStrutsAction(
		HttpServletRequest request, String portletId) {

		return "/directory/view_user";
	}

	@Override
	protected String[] initPortletIds() {
		return new String[] {PortletKeys.DIRECTORY};
	}

}