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
import com.liferay.commerce.order.CommerceOrderValidatorRegistry;
import com.liferay.commerce.order.CommerceOrderValidatorResult;
import com.liferay.commerce.order.content.web.internal.frontend.util.CommerceOrderClayTableUtil;
import com.liferay.commerce.order.content.web.internal.model.OrderItem;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPSubscriptionInfo;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.commerce.product.util.CPSubscriptionTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
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
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_PENDING_ORDER_ITEMS,
	service = CommerceDataSetDataProvider.class
)
public class CommercePendingOrderItemDataSetDataProvider
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

		List<CommerceOrderItem> commerceOrderItems =
			baseModelSearchResult.getBaseModels();

		try {
			return _getOrderItems(
				commerceOrderItems,
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY));
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return Collections.emptyList();
	}

	private String _formatDiscountAmount(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		if (commerceOrderItem.getDiscountAmountMoney() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney discountAmountMoney =
			commerceOrderItem.getDiscountAmountMoney();

		return discountAmountMoney.format(locale);
	}

	private String _formatFinalPrice(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		if (commerceOrderItem.getFinalPriceMoney() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney finalPriceMoney = commerceOrderItem.getFinalPriceMoney();

		return finalPriceMoney.format(locale);
	}

	private String _formatPromoPrice(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		CommerceMoney promoPriceMoney = commerceOrderItem.getPromoPriceMoney();

		if (promoPriceMoney == null) {
			return StringPool.BLANK;
		}

		BigDecimal price = promoPriceMoney.getPrice();

		if (price.compareTo(BigDecimal.ZERO) <= 0) {
			return StringPool.BLANK;
		}

		return promoPriceMoney.format(locale);
	}

	private String _formatSubscriptionPeriod(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

		if ((cpInstance == null) ||
			(cpInstance.getCPSubscriptionInfo() == null)) {

			return null;
		}

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

		return LanguageUtil.format(
			locale, "every-x-x",
			new Object[] {cpSubscriptionInfo.getSubscriptionLength(), period});
	}

	private String _formatUnitPrice(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		if (commerceOrderItem.getUnitPriceMoney() == null) {
			return StringPool.BLANK;
		}

		CommerceMoney unitPriceMoney = commerceOrderItem.getUnitPriceMoney();

		return unitPriceMoney.format(locale);
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
			commerceOrderId, filter.getKeywords(), start, end, sort);
	}

	private long _getCommerceOptionValueCPDefinitionId(
		CommerceOrderItem commerceOrderItem) {

		if (!commerceOrderItem.hasParentCommerceOrderItem()) {
			return commerceOrderItem.getCPDefinitionId();
		}

		return commerceOrderItem.getParentCommerceOrderItemCPDefinitionId();
	}

	private String[] _getCommerceOrderErrorMessages(
		CommerceOrderItem commerceOrderItem,
		Map<Long, List<CommerceOrderValidatorResult>>
			commerceOrderValidatorResultMap) {

		List<CommerceOrderValidatorResult> commerceOrderValidatorResults =
			commerceOrderValidatorResultMap.get(
				commerceOrderItem.getCommerceOrderItemId());

		List<String> errorMessages = new ArrayList<>();

		for (CommerceOrderValidatorResult commerceOrderValidatorResult :
				commerceOrderValidatorResults) {

			errorMessages.add(
				commerceOrderValidatorResult.getLocalizedMessage());
		}

		return ArrayUtil.toStringArray(errorMessages);
	}

	private String _getCommerceOrderOptions(
			CommerceOrderItem commerceOrderItem, Locale locale)
		throws PortalException {

		List<KeyValuePair> commerceOptionValueKeyValuePairs =
			_cpInstanceHelper.getKeyValuePairs(
				_getCommerceOptionValueCPDefinitionId(commerceOrderItem),
				commerceOrderItem.getJson(), locale);

		StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);

		for (KeyValuePair keyValuePair : commerceOptionValueKeyValuePairs) {
			stringJoiner.add(keyValuePair.getValue());
		}

		return stringJoiner.toString();
	}

	private Map<Long, List<CommerceOrderValidatorResult>>
			_getCommerceOrderValidatorResultMap(
				List<CommerceOrderItem> commerceOrderItems,
				ThemeDisplay themeDisplay)
		throws PortalException {

		if (commerceOrderItems.isEmpty()) {
			return Collections.emptyMap();
		}

		CommerceOrderItem commerceOrderItem = commerceOrderItems.get(0);

		return _commerceOrderValidatorRegistry.getCommerceOrderValidatorResults(
			themeDisplay.getLocale(),
			_commerceOrderService.getCommerceOrder(
				commerceOrderItem.getCommerceOrderId()));
	}

	private List<OrderItem> _getOrderItems(
			List<CommerceOrderItem> commerceOrderItems,
			ThemeDisplay themeDisplay)
		throws Exception {

		List<OrderItem> orderItems = new ArrayList<>();

		Map<Long, List<CommerceOrderValidatorResult>>
			commerceOrderValidatorResultMap =
				_getCommerceOrderValidatorResultMap(
					commerceOrderItems, themeDisplay);

		Locale locale = themeDisplay.getLocale();

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			orderItems.add(
				new OrderItem(
					commerceOrderItem.getCommerceOrderItemId(),
					commerceOrderItem.getCommerceOrderId(),
					commerceOrderItem.getSku(),
					commerceOrderItem.getName(locale),
					_getCommerceOrderOptions(commerceOrderItem, locale),
					_formatUnitPrice(commerceOrderItem, locale),
					_formatPromoPrice(commerceOrderItem, locale),
					_formatDiscountAmount(commerceOrderItem, locale),
					commerceOrderItem.getQuantity(),
					_formatFinalPrice(commerceOrderItem, locale),
					_cpInstanceHelper.getCPInstanceThumbnailSrc(
						commerceOrderItem.getCPInstanceId()),
					CommerceOrderClayTableUtil.getViewShipmentURL(
						commerceOrderItem.getCommerceOrderId(), themeDisplay),
					0,
					_getCommerceOrderErrorMessages(
						commerceOrderItem, commerceOrderValidatorResultMap),
					_formatSubscriptionPeriod(commerceOrderItem, locale)));
		}

		return orderItems;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePendingOrderItemDataSetDataProvider.class);

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceOrderValidatorRegistry _commerceOrderValidatorRegistry;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;

}