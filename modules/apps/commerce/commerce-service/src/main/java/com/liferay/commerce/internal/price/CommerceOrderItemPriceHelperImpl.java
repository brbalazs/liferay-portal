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

package com.liferay.commerce.internal.price;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.price.CommerceOrderItemPrice;
import com.liferay.commerce.price.CommerceOrderItemPriceHelper;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(immediate = true, service = CommerceOrderItemPriceHelper.class)
public class CommerceOrderItemPriceHelperImpl
	implements CommerceOrderItemPriceHelper {

	@Override
	public CommerceOrderItemPrice getCommerceOrderItemPrice(
			CommerceCurrency commerceCurrency,
			CommerceOrderItem commerceOrderItem)
		throws PortalException {

		return _getCommerceOrderItemPrice(
			commerceCurrency, commerceOrderItem, false);
	}

	@Override
	public CommerceOrderItemPrice getCommerceOrderItemPricePerUnit(
			CommerceCurrency commerceCurrency,
			CommerceOrderItem commerceOrderItem)
		throws PortalException {

		return _getCommerceOrderItemPrice(
			commerceCurrency, commerceOrderItem, true);
	}

	private CommerceOrderItemPrice _getCommerceOrderItemPrice(
			CommerceCurrency commerceCurrency,
			CommerceOrderItem commerceOrderItem, boolean isUnit)
		throws PortalException {

		CommerceMoney unitPriceMoney = commerceOrderItem.getUnitPriceMoney();
		CommerceMoney promoPriceMoney = commerceOrderItem.getPromoPriceMoney();

		CommerceMoney discountAmountMoney =
			commerceOrderItem.getDiscountAmountMoney();

		CommerceMoney finalPriceMoney = commerceOrderItem.getFinalPriceMoney();

		BigDecimal discountPercentageLevel1 =
			commerceOrderItem.getDiscountPercentageLevel1();
		BigDecimal discountPercentageLevel2 =
			commerceOrderItem.getDiscountPercentageLevel2();
		BigDecimal discountPercentageLevel3 =
			commerceOrderItem.getDiscountPercentageLevel3();
		BigDecimal discountPercentageLevel4 =
			commerceOrderItem.getDiscountPercentageLevel4();

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

			discountPercentageLevel1 =
				commerceOrderItem.getDiscountPercentageLevel1WithTaxAmount();
			discountPercentageLevel2 =
				commerceOrderItem.getDiscountPercentageLevel2WithTaxAmount();
			discountPercentageLevel3 =
				commerceOrderItem.getDiscountPercentageLevel3WithTaxAmount();
			discountPercentageLevel4 =
				commerceOrderItem.getDiscountPercentageLevel4WithTaxAmount();

			finalPriceMoney =
				commerceOrderItem.getFinalPriceWithTaxAmountMoney();
		}

		BigDecimal unitPrice = unitPriceMoney.getPrice();
		BigDecimal promoPrice = promoPriceMoney.getPrice();
		BigDecimal finalPrice = finalPriceMoney.getPrice();
		BigDecimal discountAmount = discountAmountMoney.getPrice();

		List<CommerceOrderItem> childCommerceOrderItems =
			_commerceOrderItemService.getChildCommerceOrderItems(
				commerceOrderItem.getCommerceOrderItemId());

		int parentQuantity = commerceOrderItem.getQuantity();

		for (CommerceOrderItem childCommerceOrderItem :
				childCommerceOrderItems) {

			BigDecimal childUnitPrice = childCommerceOrderItem.getUnitPrice();
			BigDecimal childPromoPrice = childCommerceOrderItem.getPromoPrice();
			BigDecimal childDiscountAmount =
				childCommerceOrderItem.getDiscountAmount();
			BigDecimal childFinalPrice = childCommerceOrderItem.getFinalPrice();

			if (priceDisplayType.equals(
					CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {

				childUnitPrice =
					childCommerceOrderItem.getUnitPriceWithTaxAmount();
				childPromoPrice =
					childCommerceOrderItem.getPromoPriceWithTaxAmount();
				childDiscountAmount =
					childCommerceOrderItem.getDiscountWithTaxAmount();
				childFinalPrice =
					childCommerceOrderItem.getFinalPriceWithTaxAmount();
			}

			if ((childPromoPrice.compareTo(BigDecimal.ZERO) > 0) &&
				(promoPrice.compareTo(BigDecimal.ZERO) == 0)) {

				promoPrice = promoPrice.add(unitPrice);
			}
			else if ((childPromoPrice.compareTo(BigDecimal.ZERO) == 0) &&
					 (promoPrice.compareTo(BigDecimal.ZERO) > 0)) {

				promoPrice = promoPrice.add(
					_getPricePerUnit(
						commerceCurrency, childUnitPrice,
						childCommerceOrderItem.getQuantity(), parentQuantity));
			}

			unitPrice = unitPrice.add(
				_getPricePerUnit(
					commerceCurrency, childUnitPrice,
					childCommerceOrderItem.getQuantity(), parentQuantity));

			promoPrice = promoPrice.add(
				_getPricePerUnit(
					commerceCurrency, childPromoPrice,
					childCommerceOrderItem.getQuantity(), parentQuantity));

			discountAmount = discountAmount.add(childDiscountAmount);

			finalPrice = finalPrice.add(childFinalPrice);
		}

		if (isUnit) {
			finalPrice = finalPrice.divide(
				BigDecimal.valueOf(parentQuantity),
				RoundingMode.valueOf(commerceCurrency.getRoundingMode()));
		}

		CommerceOrderItemPriceImpl commerceOrderItemPrice =
			new CommerceOrderItemPriceImpl(
				_commerceMoneyFactory.create(commerceCurrency, unitPrice));

		_setCommerceOrderItemPrice(
			commerceCurrency, commerceOrderItemPrice, discountAmount,
			discountPercentageLevel1, discountPercentageLevel2,
			discountPercentageLevel3, discountPercentageLevel4, finalPrice,
			promoPrice, commerceOrderItem.getQuantity(), unitPrice);

		return commerceOrderItemPrice;
	}

	private BigDecimal _getDiscountPercentage(
		BigDecimal amount, BigDecimal discount, RoundingMode roundingMode) {

		BigDecimal discountedAmount = amount.subtract(discount);

		double percentage =
			discountedAmount.doubleValue() / amount.doubleValue();

		BigDecimal discountPercentage = new BigDecimal(percentage);

		discountPercentage = discountPercentage.multiply(_ONE_HUNDRED);

		MathContext mathContext = new MathContext(
			discountPercentage.precision(), roundingMode);

		return _ONE_HUNDRED.subtract(discountPercentage, mathContext);
	}

	private BigDecimal _getPricePerUnit(
		CommerceCurrency commerceCurrency, BigDecimal price, int quantity,
		int parentQuantity) {

		BigDecimal pricePerUnit = price.multiply(BigDecimal.valueOf(quantity));

		return pricePerUnit.divide(
			BigDecimal.valueOf(parentQuantity),
			RoundingMode.valueOf(commerceCurrency.getRoundingMode()));
	}

	private void _setCommerceOrderItemPrice(
		CommerceCurrency commerceCurrency,
		CommerceOrderItemPriceImpl commerceOrderItemPrice,
		BigDecimal discountAmount, BigDecimal discountPercentageLevel1,
		BigDecimal discountPercentageLevel2,
		BigDecimal discountPercentageLevel3,
		BigDecimal discountPercentageLevel4, BigDecimal finalPrice,
		BigDecimal promoPrice, int quantity, BigDecimal unitPrice) {

		BigDecimal activePrice = unitPrice;

		if ((promoPrice != null) &&
			(promoPrice.compareTo(BigDecimal.ZERO) > 0)) {

			commerceOrderItemPrice.setPromoPrice(
				_commerceMoneyFactory.create(commerceCurrency, promoPrice));

			activePrice = promoPrice;
		}

		commerceOrderItemPrice.setDiscountAmount(
			_commerceMoneyFactory.create(commerceCurrency, discountAmount));

		BigDecimal discountPercentage = _getDiscountPercentage(
			activePrice.multiply(BigDecimal.valueOf(quantity)), discountAmount,
			RoundingMode.valueOf(commerceCurrency.getRoundingMode()));

		commerceOrderItemPrice.setDiscountPercentage(discountPercentage);

		commerceOrderItemPrice.setDiscountPercentageLevel1(
			discountPercentageLevel1);
		commerceOrderItemPrice.setDiscountPercentageLevel2(
			discountPercentageLevel2);
		commerceOrderItemPrice.setDiscountPercentageLevel3(
			discountPercentageLevel3);
		commerceOrderItemPrice.setDiscountPercentageLevel4(
			discountPercentageLevel4);

		commerceOrderItemPrice.setFinalPrice(
			_commerceMoneyFactory.create(commerceCurrency, finalPrice));
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

}