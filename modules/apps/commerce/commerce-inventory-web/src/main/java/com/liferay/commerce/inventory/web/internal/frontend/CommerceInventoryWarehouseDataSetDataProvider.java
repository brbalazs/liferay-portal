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
import com.liferay.commerce.inventory.model.CommerceInventoryAdminUIWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseLocalService;
import com.liferay.commerce.inventory.web.internal.model.Warehouse;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_WAREHOUSES,
	service = CommerceDataSetDataProvider.class
)
public class CommerceInventoryWarehouseDataSetDataProvider
	implements CommerceDataSetDataProvider<Warehouse> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceInventoryActionKeys.MANAGE_INVENTORY);

		long companyId = _portal.getCompanyId(httpServletRequest);

		String sku = ParamUtil.getString(httpServletRequest, "sku");

		return _commerceInventoryWarehouseLocalService.
			countAdminUIWarehousesByCompanyIdAndSku(companyId, sku);
	}

	@Override
	public List<Warehouse> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceInventoryActionKeys.MANAGE_INVENTORY);

		List<Warehouse> warehouses = new ArrayList<>();

		long companyId = _portal.getCompanyId(httpServletRequest);

		String sku = ParamUtil.getString(httpServletRequest, "sku");

		List<CommerceInventoryAdminUIWarehouse> adminUIWarehouses =
			_commerceInventoryWarehouseLocalService.
				getAdminUIWarehousesByCompanyIdAndSku(
					companyId, sku, pagination.getStartPosition(),
					pagination.getEndPosition());

		for (CommerceInventoryAdminUIWarehouse adminUIWarehouse :
				adminUIWarehouses) {

			warehouses.add(
				new Warehouse(
					adminUIWarehouse.getName(),
					adminUIWarehouse.getStockQuantity(),
					adminUIWarehouse.getReservedQuantity(),
					adminUIWarehouse.getReplenishmentQuantity()));
		}

		return warehouses;
	}

	@Reference
	private CommerceInventoryWarehouseLocalService
		_commerceInventoryWarehouseLocalService;

	@Reference
	private Portal _portal;

}