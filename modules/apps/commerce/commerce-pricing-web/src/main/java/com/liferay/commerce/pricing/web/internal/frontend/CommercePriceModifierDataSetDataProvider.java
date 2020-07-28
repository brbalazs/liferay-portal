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

package com.liferay.commerce.pricing.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.service.CommercePriceModifierService;
import com.liferay.commerce.pricing.web.internal.frontend.constants.CommercePricingDataSetConstants;
import com.liferay.commerce.pricing.web.internal.model.PriceModifier;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_MODIFIERS,
	service = CommerceDataSetDataProvider.class
)
public class CommercePriceModifierDataSetDataProvider
	implements CommerceDataSetDataProvider<PriceModifier> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commercePriceListId = ParamUtil.getLong(
			httpServletRequest, "commercePriceListId");

		return _commercePriceModifierService.getCommercePriceModifiersCount(
			commercePriceListId);
	}

	@Override
	public List<PriceModifier> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<PriceModifier> priceModifiers = new ArrayList<>();

		long commercePriceListId = ParamUtil.getLong(
			httpServletRequest, "commercePriceListId");

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierService.getCommercePriceModifiers(
				commercePriceListId, pagination.getStartPosition(),
				pagination.getEndPosition(), null);

		for (CommercePriceModifier commercePriceModifier :
				commercePriceModifiers) {

			// TODO schedule

			priceModifiers.add(
				new PriceModifier(
					commercePriceModifier.getModifierType(),
					commercePriceModifier.getTitle(),
					commercePriceModifier.getCommercePriceModifierId(), null,
					commercePriceModifier.getTarget()));
		}

		return priceModifiers;
	}

	@Reference
	private CommercePriceModifierService _commercePriceModifierService;

}