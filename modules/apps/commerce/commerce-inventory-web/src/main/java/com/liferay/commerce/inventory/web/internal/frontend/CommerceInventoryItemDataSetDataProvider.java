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

package com.liferay.commerce.inventory.web.internal.frontend;

import static com.liferay.portal.kernel.security.permission.PermissionThreadLocal.getPermissionChecker;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.inventory.constants.CommerceInventoryActionKeys;
import com.liferay.commerce.inventory.model.CIWarehouseItem;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseItemLocalService;
import com.liferay.commerce.inventory.web.internal.model.InventoryItem;
import com.liferay.commerce.inventory.web.internal.model.Sku;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_ITEMS,
	service = CommerceDataSetDataProvider.class
)
public class CommerceInventoryItemDataSetDataProvider
	implements CommerceDataSetDataProvider<InventoryItem> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceInventoryActionKeys.MANAGE_INVENTORY);

		return _commerceInventoryWarehouseItemLocalService.
			countItemsByCompanyId(_portal.getCompanyId(httpServletRequest));
	}

	@Override
	public List<InventoryItem> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceInventoryActionKeys.MANAGE_INVENTORY);

		List<InventoryItem> inventoryItems = new ArrayList<>();

		List<CIWarehouseItem> ciWarehouseItems =
			_commerceInventoryWarehouseItemLocalService.getItemsByCompanyId(
				_portal.getCompanyId(httpServletRequest),
				pagination.getStartPosition(), pagination.getEndPosition());

		for (CIWarehouseItem ciWarehouseItem : ciWarehouseItems) {
			inventoryItems.add(
				new InventoryItem(
					new Sku(
						ciWarehouseItem.getSkuCode(),
						_getWarehouseItemPanelURL(
							ciWarehouseItem.getSkuCode(), httpServletRequest)),
					ciWarehouseItem.getStockQuantity(),
					ciWarehouseItem.getBookedQuantity(),
					ciWarehouseItem.getReplenishmentQuantity()));
		}

		return inventoryItems;
	}

	private String _getWarehouseItemPanelURL(
			String sku, HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CommerceOrder.class.getName(),
			PortletProvider.Action.MANAGE);

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommerceWarehouseItem");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(httpServletRequest));
		portletURL.setParameter("sku", String.valueOf(sku));

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException wse) {
			_log.error(wse, wse);
		}

		return portletURL.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceInventoryItemDataSetDataProvider.class);

	@Reference
	private CommerceInventoryWarehouseItemLocalService
		_commerceInventoryWarehouseItemLocalService;

	@Reference
	private Portal _portal;

}