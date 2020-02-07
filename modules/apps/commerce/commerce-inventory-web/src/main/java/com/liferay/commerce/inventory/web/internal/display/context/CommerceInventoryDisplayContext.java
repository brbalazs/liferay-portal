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

package com.liferay.commerce.inventory.web.internal.display.context;

import static com.liferay.portal.kernel.security.permission.PermissionThreadLocal.getPermissionChecker;

import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuItem;
import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.inventory.constants.CommerceInventoryActionKeys;
import com.liferay.commerce.inventory.model.CommerceInventoryAudit;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem;
import com.liferay.commerce.inventory.service.CommerceInventoryAuditService;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseItemService;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseService;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luca Pellizzon
 */
public class CommerceInventoryDisplayContext {

	public CommerceInventoryDisplayContext(
		CommerceInventoryAuditService commerceInventoryAuditService,
		CommerceInventoryWarehouseService commerceInventoryWarehouseService,
		CommerceInventoryWarehouseItemService inventoryWarehouseItemService,
		JSONFactory jsonFactory, HttpServletRequest httpServletRequest) {

		_commerceInventoryAuditService = commerceInventoryAuditService;
		_commerceInventoryWarehouseService = commerceInventoryWarehouseService;
		_commerceInventoryWarehouseItemService = inventoryWarehouseItemService;
		_jsonFactory = jsonFactory;

		_httpServletRequest = httpServletRequest;
		_cpRequestHelper = new CPRequestHelper(httpServletRequest);

		_skuCode = ParamUtil.getString(_httpServletRequest, "sku");
	}

	public List<Map<String, String>> getChangelogElements() throws Exception {
		List<CommerceInventoryAudit> commerceInventoryAudits =
			_commerceInventoryAuditService.getCommerceInventoryAudits(
				_skuCode, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		List<Map<String, String>> elements = new ArrayList<>();

		for (CommerceInventoryAudit commerceInventoryAudit :
				commerceInventoryAudits) {

			Map<String, String> element = new HashMap<>();

			Date createDate = commerceInventoryAudit.getCreateDate();

			element.put("date", createDate.toString());

			String description = commerceInventoryAudit.getDescription();

			int indexOf = StringUtil.indexOfAny(description, new char[] {'{'});
			int lastIndexOf = StringUtil.lastIndexOfAny(
				description, new char[] {'}'});

			StringBundler titleSB = new StringBundler(3);

			titleSB.append(description.subSequence(0, indexOf));
			titleSB.append(CharPool.SPACE);
			titleSB.append(commerceInventoryAudit.getQuantity());

			element.put("title", titleSB.toString());

			CharSequence serializedHashMap = description.subSequence(
				indexOf, lastIndexOf + 1);

			HashMap<String, String> deserialize =
				(HashMap<String, String>)_jsonFactory.deserialize(
					serializedHashMap.toString());

			StringBundler descriptionSB = new StringBundler();

			for (Map.Entry<String, String> entry : deserialize.entrySet()) {
				descriptionSB.append(entry.getKey());
				descriptionSB.append(CharPool.SPACE);
				descriptionSB.append(CharPool.COLON);
				descriptionSB.append(CharPool.SPACE);
				descriptionSB.append(entry.getValue());
				descriptionSB.append("<br />");
			}

			element.put("description", descriptionSB.toString());

			elements.add(element);
		}

		return elements;
	}

	public CommerceInventoryWarehouseItem getCommerceInventoryWarehouseItem()
		throws PrincipalException {

		long companyId = _cpRequestHelper.getCompanyId();

		List<CommerceInventoryWarehouseItem>
			commerceInventoryWarehouseItemsByCompanyId =
				_commerceInventoryWarehouseItemService.
					getCommerceInventoryWarehouseItemsByCompanyIdAndSku(
						companyId, _skuCode, 0, 1);

		return commerceInventoryWarehouseItemsByCompanyId.get(0);
	}

	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses()
		throws PrincipalException {

		long companyId = _cpRequestHelper.getCompanyId();

		return _commerceInventoryWarehouseService.
			getCommerceInventoryWarehouses(
				companyId, true, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	public String getCreateReplenishmentActionURL() throws Exception {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		portletURL.setParameter(
			"mvcRenderCommandName", "addInventoryReplenishment");
		portletURL.setParameter("sku", _skuCode);

		return portletURL.toString();
	}

	public List<DropdownItem> getDropdownItems() {
		List<DropdownItem> headerDropdownItems = new ArrayList<>();

		DropdownItem headerDropdownItem1 = new DropdownItem();

		headerDropdownItem1.setLabel("First link");
		headerDropdownItem1.setHref("/first-link");
		headerDropdownItem1.setIcon("home");

		headerDropdownItems.add(headerDropdownItem1);

		DropdownItem headerDropdownItem2 = new DropdownItem();

		headerDropdownItem2.setLabel("Second link");
		headerDropdownItem2.setIcon("blogs");
		headerDropdownItem2.setHref("/second-link");
		headerDropdownItem2.setActive(true);

		headerDropdownItems.add(headerDropdownItem2);

		return headerDropdownItems;
	}

	public List<HeaderActionModel> getHeaderActionModels()
		throws PortalException {

		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		if (_skuCode == null) {
			return headerActionModels;
		}

		PortletURL portletURL = getTransitionInventoryPortletURL();

		HeaderActionModel headerActionModel;

		if (PortalPermissionUtil.contains(
				getPermissionChecker(),
				CommerceInventoryActionKeys.MANAGE_INVENTORY)) {

			portletURL.setParameter("transitionName", "save");

			headerActionModel = new HeaderActionModel(
				"btn-primary", null, portletURL.toString(), null, "save");

			headerActionModels.add(headerActionModel);
		}

		return headerActionModels;
	}

	public PortletURL getPortletURL() {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String redirect = ParamUtil.getString(
			_cpRequestHelper.getRequest(), "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		if (_skuCode != null) {
			portletURL.setParameter("sku", _skuCode);
		}

		return portletURL;
	}

	public ClayCreationMenu getReplenishmentClayCreationMenu()
		throws Exception {

		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (PortalPermissionUtil.contains(
				getPermissionChecker(),
				CommerceInventoryActionKeys.MANAGE_INVENTORY)) {

			clayCreationMenu.addClayCreationMenuItem(
				new ClayCreationMenuItem(
					getCreateReplenishmentActionURL(),
					LanguageUtil.get(
						_cpRequestHelper.getRequest(), "add-income"),
					ClayCreationMenuItem.CLAY_CREATION_MENU_ITEM_TARGET_MODAL));
		}

		return clayCreationMenu;
	}

	public String getSkuCode() {
		return _skuCode;
	}

	public String getTransferQuantitiesActionURL() throws Exception {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		portletURL.setParameter("mvcRenderCommandName", "transferQuantities");
		portletURL.setParameter("sku", _skuCode);

		return portletURL.toString();
	}

	public PortletURL getTransitionInventoryPortletURL() {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createActionURL();

		portletURL.setParameter(
			ActionRequest.ACTION_NAME, "editCommerceInventoryItem");
		portletURL.setParameter(Constants.CMD, "transition");
		portletURL.setParameter("sku", _skuCode);
		portletURL.setParameter("redirect", _cpRequestHelper.getCurrentURL());

		return portletURL;
	}

	public ClayCreationMenu getWarehousesClayCreationMenu() throws Exception {
		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (PortalPermissionUtil.contains(
				getPermissionChecker(),
				CommerceInventoryActionKeys.MANAGE_INVENTORY)) {

			clayCreationMenu.addClayCreationMenuItem(
				new ClayCreationMenuItem(
					getTransferQuantitiesActionURL(),
					LanguageUtil.get(
						_cpRequestHelper.getRequest(), "transfer-quantity"),
					ClayCreationMenuItem.CLAY_CREATION_MENU_ITEM_TARGET_MODAL));
		}

		return clayCreationMenu;
	}

	private final CommerceInventoryAuditService _commerceInventoryAuditService;
	private final CommerceInventoryWarehouseItemService
		_commerceInventoryWarehouseItemService;
	private final CommerceInventoryWarehouseService
		_commerceInventoryWarehouseService;
	private final CPRequestHelper _cpRequestHelper;
	private final HttpServletRequest _httpServletRequest;
	private final JSONFactory _jsonFactory;
	private String _skuCode;

}