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

package com.liferay.commerce.order.content.web.internal.frontend;

import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.content.web.internal.model.OrderItem;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPSubscriptionInfo;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.commerce.product.util.CPSubscriptionTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_PLACED_ORDER_ITEMS,
	service = CommerceDataSetDataProvider.class
)
public class CommercePlacedOrderItemDataSetDataProvider
	implements CommerceDataSetDataProvider<OrderItem> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		OrderFilterImpl orderFilterImpl = (OrderFilterImpl)filter;

		return _commerceOrderItemService.getCommerceOrderItemsCount(
			orderFilterImpl.getCommerceOrderId());
	}

	@Override
	public List<OrderItem> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		BaseModelSearchResult<CommerceOrderItem> baseModelSearchResult =
			_getBaseModelSearchResult(
				httpServletRequest, filter, pagination, sort);

		try {
			return _getOrderItems(
				baseModelSearchResult.getBaseModels(), httpServletRequest);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return Collections.emptyList();
	}

	private BaseModelSearchResult<CommerceOrderItem> _getBaseModelSearchResult(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		int start = 0;
		int end = 0;

		if (pagination != null) {
			start = pagination.getStartPosition();
			end = pagination.getEndPosition();
		}

		return _commerceOrderItemService.search(
			commerceOrderId, 0, filter.getKeywords(), start, end, sort);
	}

	private List<OrderItem> _getChildOrderItems(
			CommerceOrderItem commerceOrderItem,
			HttpServletRequest httpServletRequest)
		throws Exception {

		List<CommerceOrderItem> childCommerceOrderItems =
			_commerceOrderItemService.getChildCommerceOrderItems(
				commerceOrderItem.getCommerceOrderItemId());

		return _getOrderItems(childCommerceOrderItems, httpServletRequest);
	}

	private List<OrderItem> _getOrderItems(
			List<CommerceOrderItem> commerceOrderItems,
			HttpServletRequest httpServletRequest)
		throws Exception {

		List<OrderItem> orderItems = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String priceDisplayType =
			CommercePricingConstants.TAX_EXCLUDED_FROM_PRICE;

		if ((commerceOrderItems != null) && !commerceOrderItems.isEmpty()) {
			CommerceOrderItem firstCommerceOrderItem = commerceOrderItems.get(
				0);

			CommerceChannel commerceChannel =
				_commerceChannelService.getCommerceChannelByOrderGroupId(
					firstCommerceOrderItem.getGroupId());

			priceDisplayType = commerceChannel.getPriceDisplayType();
		}

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			CommerceMoney unitPriceMoney =
				commerceOrderItem.getUnitPriceMoney();
			CommerceMoney promoPriceMoney =
				commerceOrderItem.getPromoPriceMoney();
			CommerceMoney discountAmountMoney =
				commerceOrderItem.getDiscountAmountMoney();
			CommerceMoney finalPriceMoney =
				commerceOrderItem.getFinalPriceMoney();

			if (priceDisplayType.equals(
					CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {

				unitPriceMoney =
					commerceOrderItem.getUnitPriceWithTaxAmountMoney();

				promoPriceMoney =
					commerceOrderItem.getPromoPriceWithTaxAmountMoney();

				finalPriceMoney =
					commerceOrderItem.getFinalPriceWithTaxAmountMoney();

				discountAmountMoney =
					commerceOrderItem.getDiscountWithTaxAmountMoney();
			}

			Locale locale = themeDisplay.getLocale();

			String formattedUnitPrice = unitPriceMoney.format(locale);
			String formattedDiscountAmount = discountAmountMoney.format(locale);
			String formattedFinalPrice = finalPriceMoney.format(locale);

			String formattedPromoPrice = StringPool.BLANK;

			BigDecimal promoPriceValue = promoPriceMoney.getPrice();

			if ((promoPriceMoney != null) &&
				(promoPriceValue.compareTo(BigDecimal.ZERO) > 0)) {

				formattedPromoPrice = promoPriceMoney.format(
					themeDisplay.getLocale());
			}

			String formattedSubscriptionPeriod = null;

			CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

			if ((cpInstance != null) &&
				(cpInstance.getCPSubscriptionInfo() != null)) {

				CPSubscriptionInfo cpSubscriptionInfo =
					cpInstance.getCPSubscriptionInfo();

				String period = StringPool.BLANK;

				CPSubscriptionType cpSubscriptionType =
					_cpSubscriptionTypeRegistry.getCPSubscriptionType(
						cpSubscriptionInfo.getSubscriptionType());

				if (cpSubscriptionType != null) {
					period = cpSubscriptionType.getLabel(locale);

					if (cpSubscriptionInfo.getSubscriptionLength() > 1) {
						period = LanguageUtil.get(
							locale,
							StringUtil.toLowerCase(
								cpSubscriptionType.getLabel(LocaleUtil.US) +
									CharPool.LOWER_CASE_S));
					}
				}

				formattedSubscriptionPeriod = LanguageUtil.format(
					locale, "every-x-x",
					new Object[] {
						cpSubscriptionInfo.getSubscriptionLength(), period
					});
			}

			List<KeyValuePair> keyValuePairs =
				_cpInstanceHelper.getKeyValuePairs(
					commerceOrderItem.getCPDefinitionId(),
					commerceOrderItem.getJson(), themeDisplay.getLocale());

			StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);

			for (KeyValuePair keyValuePair : keyValuePairs) {
				stringJoiner.add(keyValuePair.getValue());
			}

			orderItems.add(
				new OrderItem(
					commerceOrderItem.getCommerceOrderItemId(),
					commerceOrderItem.getCommerceOrderId(),
					commerceOrderItem.getSku(),
					commerceOrderItem.getName(themeDisplay.getLocale()),
					stringJoiner.toString(),
					_getChildOrderItems(commerceOrderItem, httpServletRequest),
					commerceOrderItem.getParentCommerceOrderItemId(),
					formattedUnitPrice, formattedPromoPrice,
					formattedDiscountAmount, commerceOrderItem.getQuantity(),
					formattedFinalPrice,
					_cpInstanceHelper.getCPInstanceThumbnailSrc(
						commerceOrderItem.getCPInstanceId()),
					commerceOrderItem.getShippedQuantity(), null,
					formattedSubscriptionPeriod));
		}

		return orderItems;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePlacedOrderItemDataSetDataProvider.class);

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;

}