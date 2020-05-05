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

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.discount.CommerceDiscountCalculation;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.internal.util.CommercePriceConverterUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.CommerceProductPriceImpl;
import com.liferay.commerce.price.CommerceProductPriceRequest;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalService;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.option.CommerceOptionValue;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.tax.CommerceTaxCalculation;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	property = "commerce.price.calculation.key=v1.0",
	service = CommerceProductPriceCalculation.class
)
public class CommerceProductPriceCalculationImpl
	extends BaseCommerceProductPriceCalculation {

	@Override
	public CommerceProductPrice getCommerceProductPrice(
			CommerceProductPriceRequest commerceProductPriceRequest)
		throws PortalException {

		long cpInstanceId = commerceProductPriceRequest.getCpInstanceId();
		int quantity = commerceProductPriceRequest.getQuantity();
		boolean secure = commerceProductPriceRequest.isSecure();

		CommerceContext commerceContext =
			commerceProductPriceRequest.getCommerceContext();

		CommerceMoney unitPriceMoney = getUnitPrice(
			cpInstanceId, quantity, commerceContext.getCommerceCurrency(),
			secure, commerceContext);

		BigDecimal finalPrice = unitPriceMoney.getPrice();

		CommerceMoney promoPriceMoney = getPromoPrice(
			cpInstanceId, quantity, commerceContext.getCommerceCurrency(),
			secure, commerceContext);

		BigDecimal promoPrice = promoPriceMoney.getPrice();

		BigDecimal unitPrice = unitPriceMoney.getPrice();

		if ((promoPrice != null) &&
			(promoPrice.compareTo(BigDecimal.ZERO) > 0) &&
			(promoPrice.compareTo(unitPrice) <= 0)) {

			finalPrice = promoPriceMoney.getPrice();
		}

		List<CommerceOptionValue> commerceOptionValues =
			commerceProductPriceRequest.getCommerceOptionValues();

		BigDecimal[] updatedPrices = _getUpdatedPrices(
			unitPrice, promoPrice, finalPrice, commerceContext,
			commerceOptionValues);

		finalPrice = updatedPrices[2];

		boolean discountsTargetNetPrice = true;

		long commerceChannelId = commerceContext.getCommerceChannelId();

		if (commerceChannelId > 0) {
			CommerceChannel commerceChannel =
				_commerceChannelLocalService.getCommerceChannel(
					commerceContext.getCommerceChannelId());

			discountsTargetNetPrice =
				commerceChannel.isDiscountsTargetNetPrice();
		}

		CommerceDiscountValue commerceDiscountValue;

		BigDecimal finalPriceWithTaxAmount = null;

		if (discountsTargetNetPrice) {
			commerceDiscountValue =
				_commerceDiscountCalculation.getProductCommerceDiscountValue(
					cpInstanceId, quantity, finalPrice, commerceContext);

			finalPrice = finalPrice.multiply(BigDecimal.valueOf(quantity));

			if (commerceDiscountValue != null) {
				CommerceMoney discountAmountMoney =
					commerceDiscountValue.getDiscountAmount();

				finalPrice = finalPrice.subtract(
					discountAmountMoney.getPrice());
			}
		}
		else {
			finalPriceWithTaxAmount = _getConvertedPrice(
				cpInstanceId, finalPrice, false, commerceContext);

			commerceDiscountValue =
				_commerceDiscountCalculation.getProductCommerceDiscountValue(
					cpInstanceId, quantity, finalPriceWithTaxAmount,
					commerceContext);

			finalPriceWithTaxAmount = finalPriceWithTaxAmount.multiply(
				BigDecimal.valueOf(quantity));

			if (commerceDiscountValue != null) {
				CommerceMoney discountAmountMoney =
					commerceDiscountValue.getDiscountAmount();

				finalPriceWithTaxAmount = finalPriceWithTaxAmount.subtract(
					discountAmountMoney.getPrice());
			}

			finalPrice = _getConvertedPrice(
				cpInstanceId, finalPriceWithTaxAmount, true, commerceContext);
		}

		CommerceProductPriceImpl commerceProductPriceImpl =
			new CommerceProductPriceImpl();

		commerceProductPriceImpl.setQuantity(quantity);
		commerceProductPriceImpl.setCommercePriceListId(
			_getUsedCommercePriceList(cpInstanceId, commerceContext));

		commerceProductPriceImpl.setUnitPrice(unitPriceMoney);

		commerceProductPriceImpl.setUnitPromoPrice(promoPriceMoney);

		commerceProductPriceImpl.setCommerceDiscountValue(
			commerceDiscountValue);

		commerceProductPriceImpl.setFinalPrice(
			commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), finalPrice));

		if (commerceProductPriceRequest.isCalculateTax()) {
			_setCommerceProductPriceWithTaxAmount(
				cpInstanceId, finalPriceWithTaxAmount, commerceProductPriceImpl,
				commerceContext);
		}

		return commerceProductPriceImpl;
	}

	@Override
	public CommerceProductPrice getCommerceProductPrice(
			long cpInstanceId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		boolean calculateTax = false;

		long commerceChannelId = commerceContext.getCommerceChannelId();

		if (commerceChannelId > 0) {
			CommerceChannel commerceChannel =
				_commerceChannelLocalService.getCommerceChannel(
					commerceContext.getCommerceChannelId());

			calculateTax = Objects.equals(
				commerceChannel.getPriceDisplayType(),
				CommercePricingConstants.TAX_INCLUDED_IN_PRICE);
		}

		long commercePriceListId = _getUsedCommercePriceList(
			cpInstanceId, commerceContext);

		if (commercePriceListId > 0) {
			CommercePriceList commercePriceList =
				_commercePriceListLocalService.getCommercePriceList(
					commercePriceListId);

			calculateTax = calculateTax || !commercePriceList.isNetPrice();
		}

		CommerceProductPriceRequest commerceProductPriceRequest =
			new CommerceProductPriceRequest();

		commerceProductPriceRequest.setCpInstanceId(cpInstanceId);
		commerceProductPriceRequest.setQuantity(quantity);
		commerceProductPriceRequest.setSecure(secure);
		commerceProductPriceRequest.setCommerceContext(commerceContext);
		commerceProductPriceRequest.setCommerceOptionValues(null);
		commerceProductPriceRequest.setCalculateTax(calculateTax);

		return getCommerceProductPrice(commerceProductPriceRequest);
	}

	@Override
	public CommerceProductPrice getCommerceProductPrice(
			long cpInstanceId, int quantity, CommerceContext commerceContext)
		throws PortalException {

		return getCommerceProductPrice(
			cpInstanceId, quantity, true, commerceContext);
	}

	@Override
	public CommerceMoney getFinalPrice(
			long cpInstanceId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		CommerceProductPrice commerceProductPrice = getCommerceProductPrice(
			cpInstanceId, quantity, commerceContext);

		if (commerceProductPrice == null) {
			return null;
		}

		return commerceProductPrice.getFinalPrice();
	}

	@Override
	public CommerceMoney getFinalPrice(
			long cpInstanceId, int quantity, CommerceContext commerceContext)
		throws PortalException {

		return getFinalPrice(cpInstanceId, quantity, true, commerceContext);
	}

	@Override
	public CommerceMoney getPromoPrice(
			long cpInstanceId, int quantity, CommerceCurrency commerceCurrency,
			boolean secure, CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		Optional<CommercePriceList> commercePriceList = _getPriceList(
			cpInstance.getGroupId(), commerceContext);

		if (commercePriceList.isPresent()) {
			Optional<BigDecimal> priceListPrice = _getPriceListPrice(
				cpInstanceId, quantity, commercePriceList.get(),
				commerceContext, true);

			if (priceListPrice.isPresent()) {
				return commerceMoneyFactory.create(
					commerceCurrency, priceListPrice.get());
			}
		}

		BigDecimal price = cpInstance.getPromoPrice();

		CommerceCatalog commerceCatalog =
			_commerceCatalogLocalService.fetchCommerceCatalogByGroupId(
				cpInstance.getGroupId());

		CommerceCurrency catalogCommerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				commerceCatalog.getCompanyId(),
				commerceCatalog.getCommerceCurrencyCode());

		if (catalogCommerceCurrency.getCommerceCurrencyId() !=
				commerceCurrency.getCommerceCurrencyId()) {

			price = price.divide(
				catalogCommerceCurrency.getRate(),
				RoundingMode.valueOf(
					catalogCommerceCurrency.getRoundingMode()));

			price = price.multiply(commerceCurrency.getRate());
		}

		return commerceMoneyFactory.create(commerceCurrency, price);
	}

	@Override
	public CommerceMoney getUnitMaxPrice(
			long cpDefinitionId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		CommerceMoney commerceMoney = null;
		BigDecimal maxPrice = BigDecimal.ZERO;

		List<CPInstance> cpInstances =
			cpInstanceLocalService.getCPDefinitionInstances(
				cpDefinitionId, WorkflowConstants.STATUS_APPROVED,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		for (CPInstance cpInstance : cpInstances) {
			CommerceMoney cpInstanceCommerceMoney = getUnitPrice(
				cpInstance.getCPInstanceId(), quantity,
				commerceContext.getCommerceCurrency(), secure, commerceContext);

			if (maxPrice.compareTo(cpInstanceCommerceMoney.getPrice()) < 0) {
				commerceMoney = cpInstanceCommerceMoney;

				maxPrice = commerceMoney.getPrice();
			}
		}

		return commerceMoney;
	}

	@Override
	public CommerceMoney getUnitMaxPrice(
			long cpDefinitionId, int quantity, CommerceContext commerceContext)
		throws PortalException {

		return getUnitMaxPrice(cpDefinitionId, quantity, true, commerceContext);
	}

	@Override
	public CommerceMoney getUnitMinPrice(
			long cpDefinitionId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		CommerceMoney commerceMoney = null;
		BigDecimal minPrice = BigDecimal.ZERO;

		List<CPInstance> cpInstances =
			cpInstanceLocalService.getCPDefinitionInstances(
				cpDefinitionId, WorkflowConstants.STATUS_APPROVED,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		for (CPInstance cpInstance : cpInstances) {
			CommerceMoney cpInstanceCommerceMoney = getUnitPrice(
				cpInstance.getCPInstanceId(), quantity,
				commerceContext.getCommerceCurrency(), secure, commerceContext);

			if ((commerceMoney == null) ||
				(minPrice.compareTo(cpInstanceCommerceMoney.getPrice()) > 0)) {

				commerceMoney = cpInstanceCommerceMoney;

				minPrice = commerceMoney.getPrice();
			}
		}

		return commerceMoney;
	}

	@Override
	public CommerceMoney getUnitMinPrice(
			long cpDefinitionId, int quantity, CommerceContext commerceContext)
		throws PortalException {

		return getUnitMinPrice(cpDefinitionId, quantity, true, commerceContext);
	}

	@Override
	public CommerceMoney getUnitPrice(
			long cpInstanceId, int quantity, CommerceCurrency commerceCurrency,
			boolean secure, CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		Optional<CommercePriceList> commercePriceList = _getPriceList(
			cpInstance.getGroupId(), commerceContext);

		if (commercePriceList.isPresent()) {
			Optional<BigDecimal> priceListPrice = _getPriceListPrice(
				cpInstanceId, quantity, commercePriceList.get(),
				commerceContext, false);

			if (priceListPrice.isPresent()) {
				return commerceMoneyFactory.create(
					commerceCurrency, priceListPrice.get());
			}
		}

		CommerceCatalog commerceCatalog =
			_commerceCatalogLocalService.fetchCommerceCatalogByGroupId(
				cpInstance.getGroupId());

		CommerceCurrency catalogCommerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				commerceCatalog.getCompanyId(),
				commerceCatalog.getCommerceCurrencyCode());

		BigDecimal price = cpInstance.getPrice();

		if (catalogCommerceCurrency.getCommerceCurrencyId() !=
				commerceCurrency.getCommerceCurrencyId()) {

			price = price.divide(
				catalogCommerceCurrency.getRate(),
				RoundingMode.valueOf(
					catalogCommerceCurrency.getRoundingMode()));

			price = price.multiply(commerceCurrency.getRate());
		}

		return commerceMoneyFactory.create(commerceCurrency, price);
	}

	private CommerceDiscountValue _getConvertedCommerceDiscountValue(
			long cpInstanceId, CommerceDiscountValue commerceDiscountValue,
			boolean includeTax, CommerceContext commerceContext,
			RoundingMode roundingMode)
		throws PortalException {

		long commerceChannelGroupId =
			commerceContext.getCommerceChannelGroupId();
		long commerceBillingAddressId = 0;
		long commerceShippingAddressId = 0;

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder != null) {
			commerceChannelGroupId = commerceOrder.getGroupId();
			commerceBillingAddressId = commerceOrder.getBillingAddressId();
			commerceShippingAddressId = commerceOrder.getShippingAddressId();
		}
		else {
			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			if (commerceAccount != null) {
				commerceBillingAddressId =
					commerceAccount.getDefaultBillingAddressId();
				commerceShippingAddressId =
					commerceAccount.getDefaultShippingAddressId();
			}
		}

		return CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
			commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
			commerceShippingAddressId, commerceDiscountValue, includeTax,
			_commerceTaxCalculation, roundingMode);
	}

	private BigDecimal _getConvertedPrice(
			long cpInstanceId, BigDecimal price, boolean includeTax,
			CommerceContext commerceContext)
		throws PortalException {

		long commerceChannelGroupId =
			commerceContext.getCommerceChannelGroupId();
		long commerceBillingAddressId = 0;
		long commerceShippingAddressId = 0;

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder != null) {
			commerceChannelGroupId = commerceOrder.getGroupId();
			commerceBillingAddressId = commerceOrder.getBillingAddressId();
			commerceShippingAddressId = commerceOrder.getShippingAddressId();
		}
		else {
			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			if (commerceAccount != null) {
				commerceBillingAddressId =
					commerceAccount.getDefaultBillingAddressId();
				commerceShippingAddressId =
					commerceAccount.getDefaultShippingAddressId();
			}
		}

		return CommercePriceConverterUtil.getConvertedPrice(
			commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
			commerceShippingAddressId, price, includeTax,
			_commerceTaxCalculation);
	}

	private Optional<CommercePriceList> _getPriceList(
			long groupId, CommerceContext commerceContext)
		throws PortalException {

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		if (commerceAccount == null) {
			return Optional.empty();
		}

		List<CommerceAccountGroup> commerceAccountGroups =
			_commerceAccountGroupLocalService.
				getCommerceAccountGroupsByCommerceAccountId(
					commerceAccount.getCommerceAccountId());

		Stream<CommerceAccountGroup> stream = commerceAccountGroups.stream();

		long[] commerceAccountGroupIds = stream.mapToLong(
			CommerceAccountGroup::getCommerceAccountGroupId
		).toArray();

		return _commercePriceListLocalService.getCommercePriceList(
			commerceAccount.getCompanyId(), groupId,
			commerceAccount.getCommerceAccountId(), commerceAccountGroupIds);
	}

	private Optional<BigDecimal> _getPriceListPrice(
			long cpInstanceId, int quantity,
			CommercePriceList commercePriceList,
			CommerceContext commerceContext, boolean promo)
		throws PortalException {

		long commerceChannelGroupId =
			commerceContext.getCommerceChannelGroupId();
		long commerceBillingAddressId = 0;
		long commerceShippingAddressId = 0;

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder != null) {
			commerceChannelGroupId = commerceOrder.getGroupId();
			commerceBillingAddressId = commerceOrder.getBillingAddressId();
			commerceShippingAddressId = commerceOrder.getShippingAddressId();
		}
		else {
			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			if (commerceAccount != null) {
				commerceBillingAddressId =
					commerceAccount.getDefaultBillingAddressId();
				commerceShippingAddressId =
					commerceAccount.getDefaultShippingAddressId();
			}
		}

		return _getPriceListPrice(
			commerceChannelGroupId, cpInstanceId, quantity,
			commerceBillingAddressId, commerceShippingAddressId,
			commercePriceList, commerceContext.getCommerceCurrency(), promo);
	}

	private Optional<BigDecimal> _getPriceListPrice(
			long groupId, long cpInstanceId, int quantity,
			long commerceBillingAddressId, long commerceShippingAddressId,
			CommercePriceList commercePriceList,
			CommerceCurrency commerceCurrency, boolean promo)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		BigDecimal price = null;

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceUuid());

		if (commercePriceEntry == null) {
			return Optional.empty();
		}

		if (promo) {
			price = commercePriceEntry.getPromoPrice();
		}
		else {
			price = commercePriceEntry.getPrice();
		}

		if (commercePriceEntry.isHasTierPrice()) {
			CommerceTierPriceEntry commerceTierPriceEntry =
				_commerceTierPriceEntryLocalService.
					findClosestCommerceTierPriceEntry(
						commercePriceEntry.getCommercePriceEntryId(), quantity);

			if (commerceTierPriceEntry != null) {
				if (promo) {
					price = commerceTierPriceEntry.getPromoPrice();
				}
				else {
					price = commerceTierPriceEntry.getPrice();
				}
			}
		}

		if (!commercePriceList.isNetPrice()) {
			price = CommercePriceConverterUtil.getConvertedPrice(
				groupId, cpInstanceId, commerceBillingAddressId,
				commerceShippingAddressId, price, true,
				_commerceTaxCalculation);
		}

		CommerceCurrency priceListCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				commercePriceList.getCommerceCurrencyId());

		if (priceListCurrency.getCommerceCurrencyId() !=
				commerceCurrency.getCommerceCurrencyId()) {

			price = price.divide(
				priceListCurrency.getRate(),
				RoundingMode.valueOf(priceListCurrency.getRoundingMode()));

			price = price.multiply(commerceCurrency.getRate());
		}

		return Optional.ofNullable(price);
	}

	private BigDecimal[] _getUpdatedPrices(
			BigDecimal unitPrice, BigDecimal promoPrice, BigDecimal finalPrice,
			CommerceContext commerceContext,
			List<CommerceOptionValue> commerceOptionValues)
		throws PortalException {

		if ((commerceOptionValues == null) || commerceOptionValues.isEmpty()) {
			return new BigDecimal[] {unitPrice, promoPrice, finalPrice};
		}

		for (CommerceOptionValue commerceOptionValue : commerceOptionValues) {
			if (Objects.equals(
					commerceOptionValue.getPriceType(),
					CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC)) {

				BigDecimal optionValuePrice = commerceOptionValue.getPrice();

				if ((optionValuePrice != null) &&
					(optionValuePrice.compareTo(BigDecimal.ZERO) > 0)) {

					unitPrice = unitPrice.add(optionValuePrice);

					if ((promoPrice != null) &&
						(promoPrice.compareTo(BigDecimal.ZERO) > 0)) {

						promoPrice = promoPrice.add(optionValuePrice);
					}

					if (commerceOptionValue.getCPInstanceId() > 0) {
						optionValuePrice = optionValuePrice.multiply(
							BigDecimal.valueOf(
								commerceOptionValue.getQuantity()));
					}

					finalPrice = finalPrice.add(optionValuePrice);
				}
			}
			else {
				CommerceProductPrice optionValueProductPrice =
					getCommerceProductPrice(
						commerceOptionValue.getCPInstanceId(),
						commerceOptionValue.getQuantity(), true,
						commerceContext);

				CommerceMoney optionValueUnitPriceMoney =
					optionValueProductPrice.getUnitPrice();

				unitPrice = unitPrice.add(optionValueUnitPriceMoney.getPrice());

				CommerceMoney optionValueUnitPromoPriceMoney =
					optionValueProductPrice.getUnitPromoPrice();

				promoPrice = promoPrice.add(
					optionValueUnitPromoPriceMoney.getPrice());

				CommerceMoney optionValueFinalPriceMoney =
					optionValueProductPrice.getFinalPrice();

				finalPrice = finalPrice.add(
					optionValueFinalPriceMoney.getPrice());
			}
		}

		return new BigDecimal[] {unitPrice, promoPrice, finalPrice};
	}

	private long _getUsedCommercePriceList(
			long cpInstanceId, CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		Optional<CommercePriceList> commercePriceList = _getPriceList(
			cpInstance.getGroupId(), commerceContext);

		if (commercePriceList.isPresent()) {
			CommercePriceList commercePriceListActive = commercePriceList.get();

			CommercePriceEntry commercePriceEntry =
				_commercePriceEntryLocalService.fetchCommercePriceEntry(
					commercePriceListActive.getCommercePriceListId(),
					cpInstance.getCPInstanceUuid());

			if (commercePriceEntry == null) {
				return 0;
			}

			return commercePriceListActive.getCommercePriceListId();
		}

		return 0;
	}

	private void _setCommerceProductPriceWithTaxAmount(
			long cpInstanceId, BigDecimal finalPriceWithTaxAmount,
			CommerceProductPriceImpl commerceProductPriceImpl,
			CommerceContext commerceContext)
		throws PortalException {

		if (finalPriceWithTaxAmount == null) {
			CommerceMoney finalPriceMoney =
				commerceProductPriceImpl.getFinalPrice();

			finalPriceWithTaxAmount = _getConvertedPrice(
				cpInstanceId, finalPriceMoney.getPrice(), false,
				commerceContext);
		}

		CommerceMoney unitPriceMoney = commerceProductPriceImpl.getUnitPrice();

		BigDecimal unitPriceWithTaxAmount = _getConvertedPrice(
			cpInstanceId, unitPriceMoney.getPrice(), false, commerceContext);

		CommerceMoney promoPriceMoney =
			commerceProductPriceImpl.getUnitPromoPrice();

		BigDecimal promoPrice = promoPriceMoney.getPrice();

		if ((promoPrice != null) &&
			(promoPrice.compareTo(BigDecimal.ZERO) > 0)) {

			BigDecimal unitPromoPriceWithTaxAmount = _getConvertedPrice(
				cpInstanceId, promoPriceMoney.getPrice(), false,
				commerceContext);

			commerceProductPriceImpl.setUnitPromoPriceWithTaxAmount(
				_commerceMoneyFactory.create(
					commerceContext.getCommerceCurrency(),
					unitPromoPriceWithTaxAmount));
		}
		else {
			commerceProductPriceImpl.setUnitPromoPriceWithTaxAmount(
				_commerceMoneyFactory.create(
					commerceContext.getCommerceCurrency(), BigDecimal.ZERO));
		}

		commerceProductPriceImpl.setUnitPriceWithTaxAmount(
			_commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), unitPriceWithTaxAmount));

		CommerceDiscountValue commerceDiscountValue =
			commerceProductPriceImpl.getDiscountValue();

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		commerceProductPriceImpl.setCommerceDiscountValueWithTaxAmount(
			_getConvertedCommerceDiscountValue(
				cpInstanceId, commerceDiscountValue, false, commerceContext,
				RoundingMode.valueOf(commerceCurrency.getRoundingMode())));

		commerceProductPriceImpl.setFinalPriceWithTaxAmount(
			_commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(),
				finalPriceWithTaxAmount));
	}

	@Reference
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Reference(target = "(commerce.discount.calculation.key=v1.0)")
	private CommerceDiscountCalculation _commerceDiscountCalculation;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Reference
	private CommerceTaxCalculation _commerceTaxCalculation;

	@Reference
	private CommerceTierPriceEntryLocalService
		_commerceTierPriceEntryLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference(target = "(resource.name=" + CPConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}