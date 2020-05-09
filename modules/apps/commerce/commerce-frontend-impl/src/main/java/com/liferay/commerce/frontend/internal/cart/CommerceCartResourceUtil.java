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

package com.liferay.commerce.frontend.internal.cart;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.frontend.internal.cart.model.Cart;
import com.liferay.commerce.frontend.internal.cart.model.Product;
import com.liferay.commerce.frontend.internal.cart.model.Summary;
import com.liferay.commerce.frontend.model.PriceModel;
import com.liferay.commerce.frontend.model.ProductSettingsModel;
import com.liferay.commerce.frontend.util.ProductHelper;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.order.CommerceOrderValidatorRegistry;
import com.liferay.commerce.order.CommerceOrderValidatorResult;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(service = CommerceCartResourceUtil.class)
public class CommerceCartResourceUtil {

	public Cart getCart(
			long commerceOrderId, String detailsUrl, Locale locale,
			CommerceContext commerceContext, boolean valid)
		throws Exception {

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			commerceOrderId);

		List<Product> product = getProducts(
			commerceOrder, commerceContext, locale);

		if (valid && product.isEmpty()) {
			valid = false;
		}

		return new Cart(
			detailsUrl, commerceOrderId, product,
			getSummary(commerceOrder, locale, commerceContext), valid);
	}

	protected String[] getErrorMessages(
			Locale locale, CommerceOrderItem commerceOrderItem)
		throws PortalException {

		String[] errorMessages = new String[0];

		List<CommerceOrderValidatorResult> commerceOrderValidatorResults =
			_commerceOrderValidatorRegistry.validate(locale, commerceOrderItem);

		for (CommerceOrderValidatorResult commerceOrderValidatorResult :
				commerceOrderValidatorResults) {

			errorMessages = ArrayUtil.append(
				errorMessages,
				commerceOrderValidatorResult.getLocalizedMessage());
		}

		return errorMessages;
	}

	protected List<Product> getProducts(
			CommerceOrder commerceOrder, CommerceContext commerceContext,
			Locale locale)
		throws Exception {

		List<Product> products = new ArrayList<>();

		List<CommerceOrderItem> commerceOrderItems =
			commerceOrder.getCommerceOrderItems();

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			PriceModel prices = _getCommerceOrderItemPriceModel(
				commerceOrderItem, commerceContext, locale);

			ProductSettingsModel settings =
				_productHelper.getProductSettingsModel(
					commerceOrderItem.getCPInstanceId());

			Product product = new Product(
				commerceOrderItem.getCommerceOrderItemId(),
				commerceOrderItem.getParentCommerceOrderItemId(),
				commerceOrderItem.getName(locale), commerceOrderItem.getSku(),
				commerceOrderItem.getQuantity(),
				_cpInstanceHelper.getCPInstanceThumbnailSrc(
					commerceOrderItem.getCPInstanceId()),
				prices, settings, getErrorMessages(locale, commerceOrderItem),
				commerceOrderItem.getCPInstanceId());

			long commerceOptionValueCPDefinitionId =
				commerceOrderItem.getCPDefinitionId();

			if (commerceOrderItem.hasParentCommerceOrderItem()) {
				commerceOptionValueCPDefinitionId =
					commerceOrderItem.
						getParentCommerceOrderItemCPDefinitionId();
			}

			product.setOptions(
				_cpInstanceHelper.getKeyValuePairs(
					commerceOptionValueCPDefinitionId,
					commerceOrderItem.getJson(), locale));

			products.add(product);
		}

		return _groupProductByOrderItemId(products);
	}

	protected Summary getSummary(
			CommerceOrder commerceOrder, Locale locale,
			CommerceContext commerceContext)
		throws PortalException {

		CommerceOrderPrice commerceOrderPrice =
			_commerceOrderPriceCalculation.getCommerceOrderPrice(
				commerceOrder, commerceContext);

		if (commerceOrderPrice == null) {
			return null;
		}

		CommerceMoney subtotal = commerceOrderPrice.getSubtotal();
		CommerceMoney total = commerceOrderPrice.getTotal();

		int itemsQuantity =
			_commerceOrderItemService.getCommerceOrderItemsQuantity(
				commerceOrder.getCommerceOrderId());

		CommerceDiscountValue totalDiscountValue =
			commerceOrderPrice.getTotalDiscountValue();

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByOrderGroupId(
				commerceOrder.getGroupId());

		String priceDisplayType = commerceChannel.getPriceDisplayType();

		if (priceDisplayType.equals(
				CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {

			subtotal = commerceOrderPrice.getSubtotalWithTaxAmount();
			total = commerceOrderPrice.getTotalWithTaxAmount();
			totalDiscountValue =
				commerceOrderPrice.getTotalDiscountValueWithTaxAmount();
		}

		Summary summary = new Summary(
			subtotal.format(locale), total.format(locale), itemsQuantity);

		if (totalDiscountValue != null) {
			CommerceMoney discountAmount =
				totalDiscountValue.getDiscountAmount();

			summary.setDiscount(discountAmount.format(locale));
		}

		return summary;
	}

	private PriceModel _getCommerceOrderItemPriceModel(
			CommerceOrderItem commerceOrderItem,
			CommerceContext commerceContext, Locale locale)
		throws PortalException {

		CommerceMoney unitPriceMoney = commerceOrderItem.getUnitPriceMoney();
		CommerceMoney promoPriceMoney = commerceOrderItem.getPromoPriceMoney();

		CommerceMoney discountAmountMoney =
			commerceOrderItem.getDiscountAmountMoney();

		CommerceMoney finalPriceMoney = commerceOrderItem.getFinalPriceMoney();

		BigDecimal level1 = commerceOrderItem.getDiscountPercentageLevel1();
		BigDecimal level2 = commerceOrderItem.getDiscountPercentageLevel2();
		BigDecimal level3 = commerceOrderItem.getDiscountPercentageLevel3();
		BigDecimal level4 = commerceOrderItem.getDiscountPercentageLevel4();

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByOrderGroupId(
				commerceOrderItem.getGroupId());

		String priceDisplayType = commerceChannel.getPriceDisplayType();

		if (priceDisplayType.equals(
				CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {

			unitPriceMoney = commerceOrderItem.getUnitPriceWithTaxAmountMoney();
			promoPriceMoney =
				commerceOrderItem.getPromoPriceWithTaxAmountMoney();

			discountAmountMoney =
				commerceOrderItem.getDiscountWithTaxAmountMoney();

			level1 =
				commerceOrderItem.getDiscountPercentageLevel1WithTaxAmount();
			level2 =
				commerceOrderItem.getDiscountPercentageLevel2WithTaxAmount();
			level3 =
				commerceOrderItem.getDiscountPercentageLevel3WithTaxAmount();
			level4 =
				commerceOrderItem.getDiscountPercentageLevel4WithTaxAmount();

			finalPriceMoney =
				commerceOrderItem.getFinalPriceWithTaxAmountMoney();
		}

		String[] discountPercentages = {
			level1.toString(), level2.toString(), level3.toString(),
			level4.toString()
		};

		PriceModel prices = new PriceModel(unitPriceMoney.format(locale));

		BigDecimal activePrice = unitPriceMoney.getPrice();

		if (promoPriceMoney != null) {
			BigDecimal promoPrice = promoPriceMoney.getPrice();

			if (promoPrice.compareTo(BigDecimal.ZERO) > 0) {
				prices.setPromoPrice(promoPriceMoney.format(locale));

				activePrice = promoPrice;
			}
		}

		if (discountAmountMoney != null) {
			BigDecimal discountAmount = discountAmountMoney.getPrice();

			if ((discountAmount == null) ||
				(discountAmount.compareTo(BigDecimal.ZERO) == 0)) {

				return prices;
			}

			prices.setDiscount(discountAmountMoney.format(locale));

			BigDecimal discountedAmount = activePrice.subtract(discountAmount);

			CommerceCurrency commerceCurrency =
				commerceContext.getCommerceCurrency();

			BigDecimal discountPercentage = _getDiscountPercentage(
				discountedAmount, activePrice,
				RoundingMode.valueOf(commerceCurrency.getRoundingMode()));

			prices.setDiscountPercentage(
				_commercePriceFormatter.format(discountPercentage, locale));

			prices.setDiscountPercentages(discountPercentages);

			prices.setFinalPrice(finalPriceMoney.format(locale));
		}

		return prices;
	}

	private BigDecimal _getDiscountPercentage(
		BigDecimal discountedAmount, BigDecimal amount,
		RoundingMode roundingMode) {

		double actualPrice = discountedAmount.doubleValue();
		double originalPrice = amount.doubleValue();

		double percentage = actualPrice / originalPrice;

		BigDecimal discountPercentage = new BigDecimal(percentage);

		discountPercentage = discountPercentage.multiply(_ONE_HUNDRED);

		MathContext mathContext = new MathContext(
			discountPercentage.precision(), roundingMode);

		return _ONE_HUNDRED.subtract(discountPercentage, mathContext);
	}

	private List<Product> _groupProductByOrderItemId(List<Product> products) {
		Map<Long, Product> productMap = new HashMap<>();

		for (Product product : products) {
			productMap.put(product.getId(), product);
		}

		for (Product product : products) {
			long parentProductId = product.getParentProductId();

			if (parentProductId == 0) {
				continue;
			}

			Product parent = productMap.get(parentProductId);

			if (parent != null) {
				if (parent.getChildItems() == null) {
					parent.setChildItems(new ArrayList<>());
				}

				List<Product> childItems = parent.getChildItems();

				childItems.add(product);

				productMap.remove(product.getId());
			}
		}

		return new ArrayList(productMap.values());
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceOrderValidatorRegistry _commerceOrderValidatorRegistry;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private ProductHelper _productHelper;

}