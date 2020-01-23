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

package com.liferay.commerce.price.list.internal.discovery;

import com.liferay.commerce.price.list.discovery.CommercePriceListDiscovery;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Arrays;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(service = CommercePriceListDiscovery.class)
public class CommercePriceListDiscoveryImpl
	implements CommercePriceListDiscovery {

	@Override
	public CommercePriceList getCommercePriceList(
			String type, String cPInstanceUuid, long commerceAccountId,
			long[] commerceAccountGroupIds, long commerceChannelId)
		throws PortalException {

		return _getCommercePriceListByHierarchy(
			type, commerceAccountId, commerceAccountGroupIds,
			commerceChannelId);
	}

	private CommercePriceList _getCommercePriceListByHierarchy(
			String type, long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId)
		throws PortalException {

		if (commerceAccountGroupIds == null) {
			commerceAccountGroupIds = new long[0];
		}
		else if (commerceAccountGroupIds.length > 1) {
			commerceAccountGroupIds = ArrayUtil.unique(commerceAccountGroupIds);

			Arrays.sort(commerceAccountGroupIds);
		}

		CommercePriceList commercePriceList =
			_commercePriceListLocalService.getCommercePriceListByAccountId(
				type, commerceAccountId);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		commercePriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountGroupIds(
					type, commerceAccountGroupIds);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		commercePriceList =
			_commercePriceListLocalService.getCommercePriceListByChannelId(
				type, commerceChannelId);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		commercePriceList =
			_commercePriceListLocalService.getCommercePriceListByUnqualified(
				type);

		if (commercePriceList != null) {
			return commercePriceList;
		}

		return null;
	}

	private CommercePriceList _getCommercePriceListByLowestPrice(
			String type, String cPInstanceUuid, long commerceAccountId,
			long[] commerceAccountGroupIds, long commerceChannelId)
		throws PortalException {

		if (commerceAccountGroupIds == null) {
			commerceAccountGroupIds = new long[0];
		}
		else if (commerceAccountGroupIds.length > 1) {
			commerceAccountGroupIds = ArrayUtil.unique(commerceAccountGroupIds);

			Arrays.sort(commerceAccountGroupIds);
		}

		return _commercePriceListLocalService.getCommercePriceListByLowestPrice(
			type, cPInstanceUuid, commerceAccountId, commerceAccountGroupIds,
			commerceChannelId);
	}

	@Reference
	private CommercePriceListLocalService _commercePriceListLocalService;

}