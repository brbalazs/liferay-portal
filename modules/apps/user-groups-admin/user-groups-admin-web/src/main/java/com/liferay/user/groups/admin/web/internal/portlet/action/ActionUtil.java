/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.groups.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.UserGroupServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pei-Jung Lan
 */
public class ActionUtil {

	public static UserGroup getUserGroup(HttpServletRequest request)
		throws Exception {

		long userGroupId = ParamUtil.getLong(request, "userGroupId");

		UserGroup userGroup = null;

		if (userGroupId > 0) {
			userGroup = UserGroupServiceUtil.fetchUserGroup(userGroupId);
		}

		return userGroup;
	}

	public static UserGroup getUserGroup(PortletRequest portletRequest)
		throws Exception {

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			portletRequest);

		return getUserGroup(request);
	}

}