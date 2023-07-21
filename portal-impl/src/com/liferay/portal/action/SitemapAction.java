/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.action;

import com.liferay.layouts.admin.kernel.util.SitemapUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchLayoutSetException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.service.VirtualHostLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Jorge Ferrer
 */
public class SitemapAction extends Action {

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			String layoutUuid = ParamUtil.getString(request, "layoutUuid");
			long groupId = ParamUtil.getLong(request, "groupId");

			LayoutSet layoutSet = null;

			if (groupId > 0) {
				Group group = GroupLocalServiceUtil.getGroup(groupId);

				if (group.isStagingGroup()) {
					groupId = group.getLiveGroupId();
				}

				boolean privateLayout = ParamUtil.getBoolean(
					request, "privateLayout");

				layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
					groupId, privateLayout);
			}
			else {
				String host = PortalUtil.getHost(request);

				host = StringUtil.toLowerCase(host);
				host = host.trim();

				VirtualHost virtualHost =
					VirtualHostLocalServiceUtil.fetchVirtualHost(host);

				if ((virtualHost != null) &&
					(virtualHost.getLayoutSetId() != 0)) {

					layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
						virtualHost.getLayoutSetId());

					Group group = layoutSet.getGroup();

					if (group.isStagingGroup()) {
						GroupPermissionUtil.check(
							themeDisplay.getPermissionChecker(),
							group.getGroupId(), ActionKeys.VIEW_STAGING);
					}
				}
				else {
					String groupName =
						PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME;

					if (Validator.isNull(groupName)) {
						groupName = GroupConstants.GUEST;
					}

					Group group = GroupLocalServiceUtil.getGroup(
						themeDisplay.getCompanyId(), groupName);

					layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
						group.getGroupId(), false);
				}
			}

			String sitemap = SitemapUtil.getSitemap(
				layoutUuid, layoutSet.getGroupId(), layoutSet.isPrivateLayout(),
				themeDisplay);

			ServletResponseUtil.sendFile(
				request, response, null, sitemap.getBytes(StringPool.UTF8),
				ContentTypes.TEXT_XML_UTF8);
		}
		catch (NoSuchLayoutSetException nslse) {
			PortalUtil.sendError(
				HttpServletResponse.SC_NOT_FOUND, nslse, request, response);
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

	private static final Log _log = LogFactoryUtil.getLog(SitemapAction.class);

}