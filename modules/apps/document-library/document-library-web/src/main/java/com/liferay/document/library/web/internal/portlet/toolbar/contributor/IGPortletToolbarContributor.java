/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.portlet.toolbar.contributor;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.portlet.toolbar.contributor.DLPortletToolbarContributor;
import com.liferay.document.library.web.internal.portlet.toolbar.contributor.helper.DLPortletToolbarContributorHelper;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.toolbar.contributor.BasePortletToolbarContributor;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DLPortletKeys.MEDIA_GALLERY_DISPLAY,
		"mvc.render.command.name=-",
		"mvc.render.command.name=/image_gallery_display/view"
	},
	service = PortletToolbarContributor.class
)
public class IGPortletToolbarContributor extends BasePortletToolbarContributor {

	protected void addPortletTitleAddFileEntryMenuItem(
		List<MenuItem> menuItems, Folder folder, ThemeDisplay themeDisplay,
		PortletRequest portletRequest) {

		DLPortletToolbarContributor dlPortletToolbarContributor =
			_dlPortletToolbarContributorRegistry.
				getDLPortletToolbarContributor();

		List<MenuItem> portletTitleAddDocumentMenuItems =
			dlPortletToolbarContributor.getPortletTitleAddDocumentMenuItems(
				folder, themeDisplay, portletRequest);

		menuItems.addAll(portletTitleAddDocumentMenuItems);
	}

	protected void addPortletTitleAddFolderMenuItem(
		List<MenuItem> menuItems, Folder folder, ThemeDisplay themeDisplay,
		PortletRequest portletRequest) {

		DLPortletToolbarContributor dlPortletToolbarContributor =
			_dlPortletToolbarContributorRegistry.
				getDLPortletToolbarContributor();

		MenuItem portletTitleAddFolderMenuItem =
			dlPortletToolbarContributor.getPortletTitleAddFolderMenuItem(
				themeDisplay, portletRequest, folder);

		if (portletTitleAddFolderMenuItem != null) {
			menuItems.add(portletTitleAddFolderMenuItem);
		}
	}

	protected void addPortletTitleAddMulpleFileEntriesMenuItem(
		List<MenuItem> menuItems, Folder folder, ThemeDisplay themeDisplay,
		PortletRequest portletRequest) {

		DLPortletToolbarContributor dlPortletToolbarContributor =
			_dlPortletToolbarContributorRegistry.
				getDLPortletToolbarContributor();

		MenuItem portletTitleAddMultipleDocumentsMenuItem =
			dlPortletToolbarContributor.
				getPortletTitleAddMultipleDocumentsMenuItem(
					themeDisplay, portletRequest, folder);

		if (portletTitleAddMultipleDocumentsMenuItem != null) {
			portletTitleAddMultipleDocumentsMenuItem.setLabel(
				LanguageUtil.get(
					_portal.getHttpServletRequest(portletRequest),
					"multiple-media"));

			menuItems.add(portletTitleAddMultipleDocumentsMenuItem);
		}
	}

	@Override
	protected List<MenuItem> getPortletTitleMenuItems(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!_dlPortletToolbarContributorHelper.isShowActionsEnabled(
				themeDisplay, portletRequest)) {

			return null;
		}

		List<MenuItem> menuItems = new ArrayList<>();

		Folder folder = _dlPortletToolbarContributorHelper.getFolder(
			themeDisplay, portletRequest);

		addPortletTitleAddFolderMenuItem(
			menuItems, folder, themeDisplay, portletRequest);

		addPortletTitleAddFileEntryMenuItem(
			menuItems, folder, themeDisplay, portletRequest);

		addPortletTitleAddMulpleFileEntriesMenuItem(
			menuItems, folder, themeDisplay, portletRequest);

		return menuItems;
	}

	@Reference
	private DLPortletToolbarContributorHelper
		_dlPortletToolbarContributorHelper;

	@Reference
	private DLPortletToolbarContributorRegistry
		_dlPortletToolbarContributorRegistry;

	@Reference
	private Portal _portal;

}