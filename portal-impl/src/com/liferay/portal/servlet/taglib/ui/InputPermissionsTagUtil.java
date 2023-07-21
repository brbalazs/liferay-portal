/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet.taglib.ui;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.taglib.util.PortalIncludeUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author     Brian Chan
 * @deprecated As of Newton (6.2.x), replaced by {@link
 *             com.liferay.taglib.ui.InputPermissionsTag}
 */
@Deprecated
public class InputPermissionsTagUtil {

	public static void doEndTag(
			String page, String formName, String modelName,
			PageContext pageContext)
		throws JspException {

		try {
			HttpServletRequest request =
				(HttpServletRequest)pageContext.getRequest();

			request.setAttribute(
				"liferay-ui:input-permissions:formName", formName);

			if (modelName != null) {
				List<String> supportedActions =
					ResourceActionsUtil.getModelResourceActions(modelName);
				List<String> groupDefaultActions =
					ResourceActionsUtil.getModelResourceGroupDefaultActions(
						modelName);
				List<String> guestDefaultActions =
					ResourceActionsUtil.getModelResourceGuestDefaultActions(
						modelName);
				List<String> guestUnsupportedActions =
					ResourceActionsUtil.getModelResourceGuestUnsupportedActions(
						modelName);

				request.setAttribute(
					"liferay-ui:input-permissions:groupDefaultActions",
					groupDefaultActions);
				request.setAttribute(
					"liferay-ui:input-permissions:guestDefaultActions",
					guestDefaultActions);
				request.setAttribute(
					"liferay-ui:input-permissions:guestUnsupportedActions",
					guestUnsupportedActions);
				request.setAttribute(
					"liferay-ui:input-permissions:modelName", modelName);
				request.setAttribute(
					"liferay-ui:input-permissions:supportedActions",
					supportedActions);
			}

			PortalIncludeUtil.include(pageContext, page);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new JspException(e);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InputPermissionsTagUtil.class);

}