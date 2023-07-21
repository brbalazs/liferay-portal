/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.web.internal.portlet.action;

import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.blogs.exception.NoSuchEntryException;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.web.constants.BlogsWebKeys;
import com.liferay.blogs.web.internal.BlogsItemSelectorHelper;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_AGGREGATOR,
		"mvc.command.name=/blogs/edit_entry"
	},
	service = MVCRenderCommand.class
)
public class EditEntryMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			BlogsEntry entry = ActionUtil.getEntry(renderRequest);

			if (entry != null) {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)renderRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				_blogsEntryModelResourcePermission.check(
					themeDisplay.getPermissionChecker(), entry,
					ActionKeys.UPDATE);
			}

			HttpServletRequest request = _portal.getHttpServletRequest(
				renderRequest);

			request.setAttribute(WebKeys.BLOGS_ENTRY, entry);

			renderRequest.setAttribute(
				BlogsWebKeys.BLOGS_ITEM_SELECTOR_HELPER,
				_blogsItemSelectorHelper);
		}
		catch (Exception e) {
			if (e instanceof NoSuchEntryException ||
				e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass());

				return "/blogs/error.jsp";
			}

			throw new PortletException(e);
		}

		return "/blogs/edit_entry.jsp";
	}

	@Reference(unbind = "-")
	public void setItemSelectorHelper(
		BlogsItemSelectorHelper blogsItemSelectorHelper) {

		_blogsItemSelectorHelper = blogsItemSelectorHelper;
	}

	@Reference(target = "(model.class.name=com.liferay.blogs.model.BlogsEntry)")
	private volatile ModelResourcePermission<BlogsEntry>
		_blogsEntryModelResourcePermission;

	private BlogsItemSelectorHelper _blogsItemSelectorHelper;

	@Reference
	private Portal _portal;

}