/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.navigation.menu.item.layout.internal.model.listener;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.site.navigation.menu.item.layout.constants.SiteNavigationMenuItemTypeConstants;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import com.liferay.site.navigation.type.SiteNavigationMenuItemType;
import com.liferay.site.navigation.type.SiteNavigationMenuItemTypeRegistry;

import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = ModelListener.class)
public class LayoutModelListener extends BaseModelListener<Layout> {

	@Override
	public void onAfterCreate(Layout layout) throws ModelListenerException {
		if (ExportImportThreadLocal.isStagingInProcess()) {
			return;
		}

		UnicodeProperties typeSettingsProperties =
			layout.getTypeSettingsProperties();

		boolean visible = GetterUtil.getBoolean(
			typeSettingsProperties.getProperty("visible"), true);

		if (layout.isHidden() || !visible) {
			return;
		}

		long[] siteNavigationMenuIds = GetterUtil.getLongValues(
			StringUtil.split(
				layout.getTypeSettingsProperty("siteNavigationMenuId"),
				CharPool.COMMA));

		for (long siteNavigationMenuId : siteNavigationMenuIds) {
			if (siteNavigationMenuId > 0) {
				_addSiteNavigationMenuItem(siteNavigationMenuId, layout);
			}
		}
	}

	@Override
	public void onAfterRemove(Layout layout) throws ModelListenerException {
		List<SiteNavigationMenu> siteNavigationMenus =
			_siteNavigationMenuLocalService.getSiteNavigationMenus(
				layout.getGroupId());

		try {
			for (SiteNavigationMenu siteNavigationMenu : siteNavigationMenus) {
				_deleteSiteNavigationMenuItem(siteNavigationMenu, layout);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	private void _addSiteNavigationMenuItem(
		long siteNavigationMenuId, Layout layout) {

		if (ExportImportThreadLocal.isImportInProcess() ||
			ExportImportThreadLocal.isStagingInProcess() ||
			_menuItemExists(siteNavigationMenuId, layout)) {

			return;
		}

		SiteNavigationMenuItemType siteNavigationMenuItemType =
			_siteNavigationMenuItemTypeRegistry.getSiteNavigationMenuItemType(
				SiteNavigationMenuItemTypeConstants.LAYOUT);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			long parentSiteNavigationMenuItemId =
				_getParentSiteNavigationMenuItemId(
					layout.getParentPlid(), siteNavigationMenuId);

			_siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(
				layout.getUserId(), layout.getGroupId(), siteNavigationMenuId,
				parentSiteNavigationMenuItemId,
				SiteNavigationMenuItemTypeConstants.LAYOUT,
				siteNavigationMenuItemType.getTypeSettingsFromLayout(layout),
				serviceContext);
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	private void _deleteSiteNavigationMenuItem(
			SiteNavigationMenu siteNavigationMenu, Layout layout)
		throws PortalException {

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenu.getSiteNavigationMenuId());

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties = new UnicodeProperties();

			unicodeProperties.fastLoad(
				siteNavigationMenuItem.getTypeSettings());

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(layout.getUuid(), layoutUuid)) {
				_siteNavigationMenuItemLocalService.
					deleteSiteNavigationMenuItem(
						siteNavigationMenuItem.getSiteNavigationMenuItemId());
			}
		}
	}

	private long _getParentSiteNavigationMenuItemId(
		long parentPlid, long siteNavigationMenuItemId) {

		if (parentPlid == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			return 0;
		}

		Layout parentLayout = _layoutLocalService.fetchLayout(parentPlid);

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenuItemId);

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties = new UnicodeProperties();

			unicodeProperties.fastLoad(
				siteNavigationMenuItem.getTypeSettings());

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(parentLayout.getUuid(), layoutUuid)) {
				return siteNavigationMenuItem.getSiteNavigationMenuItemId();
			}
		}

		return 0;
	}

	private boolean _menuItemExists(long siteNavigationMenuId, Layout layout) {
		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			_siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenuId);

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			UnicodeProperties unicodeProperties = new UnicodeProperties();

			unicodeProperties.fastLoad(
				siteNavigationMenuItem.getTypeSettings());

			String layoutUuid = unicodeProperties.getProperty("layoutUuid");

			if (Objects.equals(layout.getUuid(), layoutUuid)) {
				return true;
			}
		}

		return false;
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private SiteNavigationMenuItemLocalService
		_siteNavigationMenuItemLocalService;

	@Reference
	private SiteNavigationMenuItemTypeRegistry
		_siteNavigationMenuItemTypeRegistry;

	@Reference
	private SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

}