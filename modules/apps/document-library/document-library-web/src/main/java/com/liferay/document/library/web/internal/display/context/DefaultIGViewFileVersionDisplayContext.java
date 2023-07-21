/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.display.context;

import com.liferay.document.library.web.internal.display.context.logic.DLPortletInstanceSettingsHelper;
import com.liferay.document.library.web.internal.display.context.logic.UIItemsBuilder;
import com.liferay.document.library.web.internal.display.context.util.IGRequestHelper;
import com.liferay.document.library.web.internal.util.DLTrashUtil;
import com.liferay.image.gallery.display.kernel.display.context.IGViewFileVersionDisplayContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Adolfo Pérez
 */
public class DefaultIGViewFileVersionDisplayContext
	implements IGViewFileVersionDisplayContext {

	public DefaultIGViewFileVersionDisplayContext(
			HttpServletRequest request, HttpServletResponse response,
			FileShortcut fileShortcut, ResourceBundle resourceBundle,
			DLTrashUtil dlTrashUtil)
		throws PortalException {

		this(
			request, response, fileShortcut.getFileVersion(), fileShortcut,
			resourceBundle, dlTrashUtil);
	}

	public DefaultIGViewFileVersionDisplayContext(
			HttpServletRequest request, HttpServletResponse response,
			FileVersion fileVersion, FileShortcut fileShortcut,
			ResourceBundle resourceBundle, DLTrashUtil dlTrashUtil)
		throws PortalException {

		_igRequestHelper = new IGRequestHelper(request);

		_dlPortletInstanceSettingsHelper = new DLPortletInstanceSettingsHelper(
			_igRequestHelper);

		if (fileShortcut == null) {
			_uiItemsBuilder = new UIItemsBuilder(
				request, fileVersion, resourceBundle, dlTrashUtil);
		}
		else {
			_uiItemsBuilder = new UIItemsBuilder(
				request, fileShortcut, resourceBundle, dlTrashUtil);
		}
	}

	public DefaultIGViewFileVersionDisplayContext(
			HttpServletRequest request, HttpServletResponse response,
			FileVersion fileVersion, ResourceBundle resourceBundle,
			DLTrashUtil dlTrashUtil)
		throws PortalException {

		this(request, response, fileVersion, null, resourceBundle, dlTrashUtil);
	}

	@Override
	public Menu getMenu() throws PortalException {
		Menu menu = new Menu();

		menu.setDirection("left-side");
		menu.setMarkupView("lexicon");
		menu.setMenuItems(getMenuItems());
		menu.setScroll(false);
		menu.setShowWhenSingleIcon(true);

		return menu;
	}

	@Override
	public List<MenuItem> getMenuItems() throws PortalException {
		List<MenuItem> menuItems = new ArrayList<>();

		if (_dlPortletInstanceSettingsHelper.isShowActions()) {
			_uiItemsBuilder.addDownloadMenuItem(menuItems);

			_uiItemsBuilder.addViewOriginalFileMenuItem(menuItems);

			_uiItemsBuilder.addEditMenuItem(menuItems);

			_uiItemsBuilder.addPermissionsMenuItem(menuItems);

			_uiItemsBuilder.addDeleteMenuItem(menuItems);
		}

		return menuItems;
	}

	@Override
	public UUID getUuid() {
		return _UUID;
	}

	private static final UUID _UUID = UUID.fromString(
		"C04528F9-C005-4E21-A926-F068750B99DB");

	private final DLPortletInstanceSettingsHelper
		_dlPortletInstanceSettingsHelper;
	private final IGRequestHelper _igRequestHelper;
	private final UIItemsBuilder _uiItemsBuilder;

}