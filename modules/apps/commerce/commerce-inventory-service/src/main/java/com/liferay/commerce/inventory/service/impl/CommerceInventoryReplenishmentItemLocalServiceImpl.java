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

package com.liferay.commerce.inventory.service.impl;

import com.liferay.commerce.inventory.model.CommerceInventoryAdminUIReplenishment;
import com.liferay.commerce.inventory.model.CommerceInventoryReplenishmentItem;
import com.liferay.commerce.inventory.service.base.CommerceInventoryReplenishmentItemLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Luca Pellizzon
 */
public class CommerceInventoryReplenishmentItemLocalServiceImpl
	extends CommerceInventoryReplenishmentItemLocalServiceBaseImpl {

	@Override
	public CommerceInventoryReplenishmentItem
			addCommerceInventoryReplenishmentItem(
				long userId, long commerceInventoryWarehouseId, String sku,
				Date availabilityDate, int quantity)
		throws PortalException {

		if (Validator.isNull(sku)) {
			throw new PortalException("SKU code is null");
		}

		User user = userLocalService.getUser(userId);

		long commerceReplenishmentId = counterLocalService.increment();

		CommerceInventoryReplenishmentItem commerceInventoryReplenishmentItem =
			commerceInventoryReplenishmentItemPersistence.create(
				commerceReplenishmentId);

		commerceInventoryReplenishmentItem.setCompanyId(user.getCompanyId());
		commerceInventoryReplenishmentItem.setUserId(userId);
		commerceInventoryReplenishmentItem.setUserName(user.getFullName());
		commerceInventoryReplenishmentItem.setCommerceInventoryWarehouseId(
			commerceInventoryWarehouseId);
		commerceInventoryReplenishmentItem.setSku(sku);
		commerceInventoryReplenishmentItem.setAvailabilityDate(
			availabilityDate);
		commerceInventoryReplenishmentItem.setQuantity(quantity);

		return commerceInventoryReplenishmentItemPersistence.update(
			commerceInventoryReplenishmentItem);
	}

	@Override
	public int countAdminUIReplenishmentItemsByCompanyIdAndSku(
		long companyId, String sku) {

		return commerceInventoryReplenishmentItemFinder.
			countAdminUIReplenishmentItemsByCompanyIdAndSku(companyId, sku);
	}

	@Override
	public List<CommerceInventoryAdminUIReplenishment>
		getAdminUIReplenishmentItemsByCompanyIdAndSku(
			long companyId, String sku, int start, int end) {

		List<Object[]> adminUIReplenishmentItems =
			commerceInventoryReplenishmentItemFinder.
				findAdminUIReplenishmentItemsByCompanyIdAndSku(
					companyId, sku, start, end);

		List<CommerceInventoryAdminUIReplenishment> replenishmentArrayList =
			new ArrayList<>();

		for (Object[] adminUIWarehouse : adminUIReplenishmentItems) {
			String warehouseName = "";

			if ((adminUIWarehouse.length > 0) &&
				(adminUIWarehouse[0] != null)) {

				warehouseName = (String)adminUIWarehouse[0];
			}

			Date date = null;

			if ((adminUIWarehouse.length > 1) &&
				(adminUIWarehouse[1] != null)) {

				date = (Date)adminUIWarehouse[1];
			}

			Integer quantity = 0;

			if ((adminUIWarehouse.length > 2) &&
				(adminUIWarehouse[2] != null)) {

				quantity = (Integer)adminUIWarehouse[2];
			}

			replenishmentArrayList.add(
				new CommerceInventoryAdminUIReplenishment(
					warehouseName, date, quantity));
		}

		return replenishmentArrayList;
	}

}