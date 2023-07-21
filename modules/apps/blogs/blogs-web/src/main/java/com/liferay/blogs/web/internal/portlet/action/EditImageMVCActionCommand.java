/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.web.internal.portlet.action;

import com.liferay.blogs.constants.BlogsConstants;
import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.blogs.exception.NoSuchEntryException;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
		"mvc.command.name=/blogs/edit_image"
	},
	service = MVCActionCommand.class
)
public class EditImageMVCActionCommand extends BaseMVCActionCommand {

	protected void deleteImages(ActionRequest actionRequest) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] deleteFileEntryIds = null;

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId > 0) {
			deleteFileEntryIds = new long[] {fileEntryId};
		}
		else {
			deleteFileEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "deleteFileEntryIds"), 0L);
		}

		Folder folder = _blogsEntryLocalService.addAttachmentsFolder(
			themeDisplay.getUserId(), themeDisplay.getScopeGroupId());

		for (long deleteFileEntryId : deleteFileEntryIds) {
			FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
				deleteFileEntryId);

			if (fileEntry.getFolderId() != folder.getFolderId()) {
				continue;
			}

			if ((fileEntry.getUserId() == themeDisplay.getUserId()) ||
				_portletResourcePermission.contains(
					themeDisplay.getPermissionChecker(),
					themeDisplay.getScopeGroup(), ActionKeys.UPDATE)) {

				PortletFileRepositoryUtil.deletePortletFileEntry(
					deleteFileEntryId);
			}
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.DELETE)) {
				deleteImages(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (e instanceof NoSuchEntryException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				actionResponse.setRenderParameter(
					"mvcPath", "/blogs/error.jsp");
			}
			else {
				throw e;
			}
		}
		catch (Throwable t) {
			_log.error(t, t);

			actionResponse.setRenderParameter("mvcPath", "/blogs/error.jsp");
		}
	}

	@Reference(unbind = "-")
	protected void setBlogsEntryLocalService(
		BlogsEntryLocalService blogsEntryLocalService) {

		_blogsEntryLocalService = blogsEntryLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditImageMVCActionCommand.class);

	private BlogsEntryLocalService _blogsEntryLocalService;

	@Reference(target = "(resource.name=" + BlogsConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}