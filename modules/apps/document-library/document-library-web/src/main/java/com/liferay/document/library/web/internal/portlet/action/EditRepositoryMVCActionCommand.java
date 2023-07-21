/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.portlet.action;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.exception.DuplicateFolderNameException;
import com.liferay.document.library.kernel.exception.DuplicateRepositoryNameException;
import com.liferay.document.library.kernel.exception.FolderNameException;
import com.liferay.document.library.kernel.exception.RepositoryNameException;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.portal.kernel.exception.InvalidRepositoryException;
import com.liferay.portal.kernel.exception.NoSuchRepositoryException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.RepositoryService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	property = {
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY,
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
		"javax.portlet.name=" + DLPortletKeys.MEDIA_GALLERY_DISPLAY,
		"mvc.command.name=/document_library/edit_repository"
	},
	service = MVCActionCommand.class
)
public class EditRepositoryMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateRepository(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				unmountRepository(actionRequest);
			}
		}
		catch (Exception e) {
			if (e instanceof NoSuchRepositoryException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				actionResponse.setRenderParameter(
					"mvcPath", "/document_library/error.jsp");
			}
			else if (e instanceof DuplicateFolderNameException ||
					 e instanceof DuplicateRepositoryNameException ||
					 e instanceof FolderNameException ||
					 e instanceof InvalidRepositoryException ||
					 e instanceof RepositoryNameException) {

				if (e instanceof InvalidRepositoryException) {
					_log.error(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass());
			}
			else {
				throw e;
			}
		}
	}

	@Reference(unbind = "-")
	protected void setRepositoryService(RepositoryService repositoryService) {
		_repositoryService = repositoryService;
	}

	protected void unmountRepository(ActionRequest actionRequest)
		throws Exception {

		long repositoryId = ParamUtil.getLong(actionRequest, "repositoryId");

		_repositoryService.deleteRepository(repositoryId);
	}

	protected void updateRepository(ActionRequest actionRequest)
		throws Exception {

		long repositoryId = ParamUtil.getLong(actionRequest, "repositoryId");

		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		if (repositoryId <= 0) {

			// Add repository

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			String className = ParamUtil.getString(actionRequest, "className");

			long classNameId = _portal.getClassNameId(className);

			long folderId = ParamUtil.getLong(actionRequest, "folderId");

			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
			UnicodeProperties typeSettingsProperties =
				PropertiesParamUtil.getProperties(actionRequest, "settings--");
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				DLFolder.class.getName(), actionRequest);

			_repositoryService.addRepository(
				themeDisplay.getScopeGroupId(), classNameId, folderId, name,
				description, portletDisplay.getId(), typeSettingsProperties,
				serviceContext);
		}
		else {

			// Update repository

			_repositoryService.updateRepository(
				repositoryId, name, description);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditRepositoryMVCActionCommand.class);

	@Reference
	private Portal _portal;

	private RepositoryService _repositoryService;

}