/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.catalog.web.internal.display.context;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyService;
import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuItem;
import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alec Sloan
 * @author Alessio Antonio Rendina
 */
public class CommerceCatalogDisplayContext {

	public CommerceCatalogDisplayContext(
			HttpServletRequest httpServletRequest,
			CommerceCatalogService commerceCatalogService,
			ModelResourcePermission<CommerceCatalog>
				commerceCatalogModelResourcePermission,
			CommerceCurrencyService commerceCurrencyService, Portal portal)
		throws PortalException {

		_commerceCatalogService = commerceCatalogService;
		_commerceCatalogModelResourcePermission =
			commerceCatalogModelResourcePermission;
		_commerceCurrencyService = commerceCurrencyService;
		_portal = portal;

		cpRequestHelper = new CPRequestHelper(httpServletRequest);
	}

	public ClayCreationMenu getClayCreationMenu() {
		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (hasAddCatalogPermission()) {
			clayCreationMenu.addClayCreationMenuItems(
				new ClayCreationMenuItem(
					getCreateCommerceCatalogActionURL(),
					LanguageUtil.get(
						cpRequestHelper.getRequest(), "add-catalog"),
					ClayCreationMenuItem.CLAY_CREATION_MENU_ITEM_TARGET_MODAL));
		}

		return clayCreationMenu;
	}

	public CommerceCatalog getCommerceCatalog() throws PortalException {
		long commerceCatalogId = ParamUtil.getLong(
			cpRequestHelper.getRequest(), "commerceCatalogId");

		if (commerceCatalogId == 0) {
			return null;
		}

		return _commerceCatalogService.fetchCommerceCatalog(commerceCatalogId);
	}

	public List<CommerceCurrency> getCommerceCurrencies()
		throws PortalException {

		return _commerceCurrencyService.getCommerceCurrencies(
			cpRequestHelper.getCompanyId(), true, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	public String getCreateCommerceCatalogActionURL() {
		PortletURL portletURL = _portal.getControlPanelPortletURL(
			cpRequestHelper.getRequest(), CPPortletKeys.COMMERCE_CATALOGS,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcRenderCommandName", "addCommerceCatalog");

		return portletURL.toString();
	}

	public List<DropdownItem> getDropdownItems() {
		return Collections.emptyList();
	}

	public List<HeaderActionModel> getHeaderActionModels() {
		HttpServletRequest httpServletRequest = cpRequestHelper.getRequest();

		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		HeaderActionModel headerActionModelCancel = new HeaderActionModel();

		headerActionModelCancel.setLabel(
			LanguageUtil.get(httpServletRequest, "cancel"));
		headerActionModelCancel.setAdditionalClasses("btn-unstyled");

		headerActionModels.add(headerActionModelCancel);

		HeaderActionModel headerActionModelSave = new HeaderActionModel();

		headerActionModelSave.setLabel(
			LanguageUtil.get(httpServletRequest, "save"));
		headerActionModelSave.setAdditionalClasses("btn-primary");

		headerActionModels.add(headerActionModelSave);

		return headerActionModels;
	}

	public PortletURL getPortletURL() throws PortalException {
		LiferayPortletResponse liferayPortletResponse =
			cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String redirect = ParamUtil.getString(
			cpRequestHelper.getRequest(), "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		String filterFields = ParamUtil.getString(
			cpRequestHelper.getRequest(), "filterFields");

		if (Validator.isNotNull(filterFields)) {
			portletURL.setParameter("filterFields", filterFields);
		}

		String filtersLabels = ParamUtil.getString(
			cpRequestHelper.getRequest(), "filtersLabels");

		if (Validator.isNotNull(filtersLabels)) {
			portletURL.setParameter("filtersLabels", filtersLabels);
		}

		String filtersValues = ParamUtil.getString(
			cpRequestHelper.getRequest(), "filtersValues");

		if (Validator.isNotNull(filtersValues)) {
			portletURL.setParameter("filtersValues", filtersValues);
		}

		return portletURL;
	}

	public boolean hasAddCatalogPermission() {
		HttpServletRequest httpServletRequest = cpRequestHelper.getRequest();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return PortalPermissionUtil.contains(
			themeDisplay.getPermissionChecker(),
			CPActionKeys.ADD_COMMERCE_CATALOG);
	}

	public boolean hasPermission(long commerceCatalogId, String actionId)
		throws PortalException {

		HttpServletRequest httpServletRequest = cpRequestHelper.getRequest();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _commerceCatalogModelResourcePermission.contains(
			themeDisplay.getPermissionChecker(), commerceCatalogId, actionId);
	}

	protected final CPRequestHelper cpRequestHelper;

	private final ModelResourcePermission<CommerceCatalog>
		_commerceCatalogModelResourcePermission;
	private final CommerceCatalogService _commerceCatalogService;
	private final CommerceCurrencyService _commerceCurrencyService;
	private final Portal _portal;

}