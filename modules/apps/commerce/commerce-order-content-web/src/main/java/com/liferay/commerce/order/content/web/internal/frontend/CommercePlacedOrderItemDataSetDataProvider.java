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
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.content.web.internal.model.OrderItem;
import com.liferay.commerce.price.CommerceOrderItemPrice;
import com.liferay.commerce.price.CommerceOrderItemPriceHelper;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPSubscriptionInfo;
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

	private String _formatPromoPrice(CommerceMoney promoPrice, Locale locale)
		throws PortalException {

		if (promoPrice != null) {
			BigDecimal promoPriceValue = promoPrice.getPrice();

			if (promoPriceValue.compareTo(BigDecimal.ZERO) > 0) {
				return promoPrice.format(locale);
			}
		}

		return StringPool.BLANK;
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

		if (commerceOrderItems.isEmpty()) {
			return Collections.emptyList();
		}

		CommerceOrderItem commerceOrderItem0 = commerceOrderItems.get(0);

		CommerceOrder commerceOrder = commerceOrderItem0.getCommerceOrder();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			CommerceOrderItemPrice commerceOrderItemPrice =
				_commerceOrderItemPriceHelper.getCommerceOrderItemPrice(
					commerceOrder.getCommerceCurrency(), commerceOrderItem);

			CommerceMoney unitPrice = commerceOrderItemPrice.getUnitPrice();

			CommerceMoney discountAmount =
				commerceOrderItemPrice.getDiscountAmount();

			Locale locale = themeDisplay.getLocale();

			String formattedUnitPrice = unitPrice.format(locale);
			String formattedDiscountAmount = discountAmount.format(locale);

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

			CommerceMoney finalPrice = commerceOrderItemPrice.getFinalPrice();

			orderItems.add(
				new OrderItem(
					commerceOrderItem.getCommerceOrderItemId(),
					commerceOrderItem.getCommerceOrderId(),
					commerceOrderItem.getSku(),
					commerceOrderItem.getName(themeDisplay.getLocale()),
					stringJoiner.toString(),
					_getChildOrderItems(commerceOrderItem, httpServletRequest),
					commerceOrderItem.getParentCommerceOrderItemId(),
					formattedUnitPrice,
					_formatPromoPrice(
						commerceOrderItemPrice.getPromoPrice(),
						themeDisplay.getLocale()),
					formattedDiscountAmount, commerceOrderItem.getQuantity(),
					finalPrice.format(locale),
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
	private CommerceOrderItemPriceHelper _commerceOrderItemPriceHelper;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;

}