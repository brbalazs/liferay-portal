/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.service.VirtualHostLocalServiceUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.RobotsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author David Truong
 */
public class RobotsAction extends Action {

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {
			String host = GetterUtil.getString(PortalUtil.getHost(request));

			LayoutSet layoutSet = null;

			VirtualHost virtualHost =
				VirtualHostLocalServiceUtil.fetchVirtualHost(host);

			if ((virtualHost != null) && (virtualHost.getLayoutSetId() > 0)) {
				layoutSet = LayoutSetLocalServiceUtil.fetchLayoutSet(host);
			}
			else {
				Company company = PortalUtil.getCompany(request);

				if (host.equals(company.getVirtualHostname()) &&
					Validator.isNotNull(
						PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME)) {

					Group defaultGroup = GroupLocalServiceUtil.getGroup(
						company.getCompanyId(),
						PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME);

					layoutSet = defaultGroup.getPublicLayoutSet();
				}
			}

			String robots = RobotsUtil.getRobots(layoutSet, request.isSecure());

			ServletResponseUtil.sendFile(
				request, response, null, robots.getBytes(StringPool.UTF8),
				ContentTypes.TEXT_PLAIN_UTF8);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			PortalUtil.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, request,
				response);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(RobotsAction.class);

}