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

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.discount.CommerceDiscountCalculation;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.discount.helper.CommerceDiscountHelper;
import com.liferay.commerce.dto.price.CommerceProductPriceImpl;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.list.constants.CommercePriceListTypeKeys;
import com.liferay.commerce.price.list.discovery.CommercePriceListDiscovery;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalService;
import com.liferay.commerce.pricing.configuration.CommercePricingConfiguration;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.pricing.modifier.CommercePriceModifierHelper;
import com.liferay.commerce.pricing.exception.CommerceUndefinedBasePriceListException;
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.tax.CommerceTaxCalculation;
import com.liferay.commerce.tax.CommerceTaxValue;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Riccardo Alberti
 */
@Component(
	property = "commerce.price.calculation.key=v2.0",
	service = CommerceProductPriceCalculation.class
)
public class CommerceProductPriceCalculationV2Impl
	implements CommerceProductPriceCalculation {

	@Override
	public CommerceProductPrice getCommerceProductPrice(
			long cpInstanceId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

		CommerceMoney unitPriceMoney = getUnitPrice(
			cpInstanceId, quantity, commerceContext.getCommerceCurrency(),
			secure, commerceContext);

		long commercePriceListId = _getCommercePriceListId(
			cpInstanceId, commerceContext);

		CommerceMoney promoPriceMoney = getPromoPrice(
			cpInstanceId, quantity, commerceContext.getCommerceCurrency(),
			secure, commerceContext);

		long commercePromoPriceListId = _getCommercePromoPriceListId(
			cpInstanceId, commerceContext);

		CommerceProductPriceImpl commerceProductPriceImpl =
			new CommerceProductPriceImpl();

		commerceProductPriceImpl.setQuantity(quantity);
		commerceProductPriceImpl.setUnitPrice(unitPriceMoney);
		commerceProductPriceImpl.setUnitPromoPrice(promoPriceMoney);

		BigDecimal finalPrice = unitPriceMoney.getPrice();

		BigDecimal promoPrice = promoPriceMoney.getPrice();

		long commerceFinalPriceListId = commercePriceListId;

		if ((promoPrice != null) &&
			(promoPrice.compareTo(BigDecimal.ZERO) > 0) &&
			(promoPrice.compareTo(unitPriceMoney.getPrice()) <= 0)) {

			finalPrice = promoPriceMoney.getPrice();

			commerceFinalPriceListId = commercePromoPriceListId;
		}

		CommerceDiscountValue commerceDiscountValue = _getCommerceDiscountValue(
			cpInstanceId, commerceFinalPriceListId, quantity, finalPrice,
			commerceContext);

		finalPrice = finalPrice.multiply(BigDecimal.valueOf(quantity));

		if (commerceDiscountValue != null) {
			CommerceMoney discountAmountMoney =
				commerceDiscountValue.getDiscountAmount();

			finalPrice = finalPrice.subtract(discountAmountMoney.getPrice());
		}

		commerceProductPriceImpl.setCommerceDiscountValue(
			commerceDiscountValue);

		commerceProductPriceImpl.setTaxValue(
			_getTaxValue(cpInstanceId, commerceContext, finalPrice));

		commerceProductPriceImpl.setFinalPrice(
			_commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), finalPrice));

		return commerceProductPriceImpl;
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

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

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

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

		long commercePromoPriceListId = _getCommercePromoPriceListId(
			cpInstanceId, commerceContext);

		if (commercePromoPriceListId > 0) {
			CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
				cpInstanceId);

			CommercePriceEntry commercePriceEntry =
				_commercePriceEntryLocalService.fetchCommercePriceEntry(
					commercePromoPriceListId, cpInstance.getCPInstanceUuid(),
					true);

			if (commercePriceEntry != null) {
				BigDecimal promoPrice = _getCommercePrice(
					commercePriceEntry, quantity, false);

				return _getCommerceMoney(
					commercePromoPriceListId, commerceCurrency, promoPrice);
			}

			CommerceMoney unitPrice = getUnitPrice(
				cpInstanceId, quantity, commerceContext.getCommerceCurrency(),
				secure, commerceContext);

			BigDecimal promoPrice = _getCommercePrice(
				unitPrice, commercePromoPriceListId, cpInstanceId);

			return _getCommerceMoney(
				commercePromoPriceListId, commerceCurrency, promoPrice);
		}

		return null;
	}

	@Override
	public CommerceMoney getUnitMaxPrice(
			long cpDefinitionId, int quantity, boolean secure,
			CommerceContext commerceContext)
		throws PortalException {

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

		CommerceMoney commerceMoney = null;
		BigDecimal maxPrice = BigDecimal.ZERO;

		List<CPInstance> cpInstances =
			_cpInstanceLocalService.getCPDefinitionInstances(
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

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

		CommerceMoney commerceMoney = null;
		BigDecimal minPrice = BigDecimal.ZERO;

		List<CPInstance> cpInstances =
			_cpInstanceLocalService.getCPDefinitionInstances(
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

		if (secure && !_hasViewPricePermission(commerceContext)) {
			return null;
		}

		long commercePriceListId = _getCommercePriceListId(
			cpInstanceId, commerceContext);

		CommercePriceList commercePriceList =
			_commercePriceListLocalService.getCommercePriceList(
				commercePriceListId);

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		if (commercePriceList.isCatalogBasePriceList() &&
			(cpInstance.getGroupId() == commercePriceList.getGroupId())) {

			CommercePriceEntry commercePriceEntry =
				_commercePriceEntryLocalService.fetchCommercePriceEntry(
					commercePriceListId, cpInstance.getCPInstanceUuid(), false);

			BigDecimal unitPrice = _getCommercePrice(
				commercePriceEntry, quantity, true);

			return _getCommerceMoney(
				commercePriceListId, commerceCurrency, unitPrice);
		}

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceListId, cpInstance.getCPInstanceUuid(), true);

		BigDecimal unitPrice = _getCommercePrice(
			commercePriceEntry, quantity, false);

		return _getCommerceMoney(
			commercePriceListId, commerceCurrency, unitPrice);
	}

	public void unsetCommerceDiscountHelper(
		CommerceDiscountHelper commerceDiscountHelper,
		Map<String, Object> properties) {

		String commerceDiscountHelperKey = GetterUtil.getString(
			properties.get("commerce.discount.helper.key"));

		_commerceDiscountHelperMap.remove(commerceDiscountHelperKey);
	}

	public void unsetCommercePriceListDiscovery(
		CommercePriceListDiscovery commercePriceListDiscovery,
		Map<String, Object> properties) {

		String commercePriceListDiscoveryKey = GetterUtil.getString(
			properties.get("commerce.price.list.discovery.key"));

		_commercePriceListDiscoveryMap.remove(commercePriceListDiscoveryKey);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setCommerceDiscountHelper(
		CommerceDiscountHelper commerceDiscountHelper,
		Map<String, Object> properties) {

		String commerceDiscountHelperKey = GetterUtil.getString(
			properties.get("commerce.discount.helper.key"));

		_commerceDiscountHelperMap.put(
			commerceDiscountHelperKey, commerceDiscountHelper);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setCommercePriceListDiscovery(
		CommercePriceListDiscovery commercePriceListDiscovery,
		Map<String, Object> properties) {

		String commercePriceListDiscoveryKey = GetterUtil.getString(
			properties.get("commerce.price.list.discovery.key"));

		_commercePriceListDiscoveryMap.put(
			commercePriceListDiscoveryKey, commercePriceListDiscovery);
	}

	private CommerceDiscountValue _calculateCommerceDiscountValue(
			BigDecimal[] values, BigDecimal finalPrice,
			CommerceContext commerceContext)
		throws PortalException {

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		RoundingMode roundingMode = RoundingMode.valueOf(
			commerceCurrency.getRoundingMode());

		CommerceDiscountHelper commerceDiscountHelper =
			_getCommerceDiscountHelper();

		BigDecimal discountedAmount =
			commerceDiscountHelper.applyCommerceDiscounts(finalPrice, values);

		BigDecimal currentDiscountAmount = finalPrice.subtract(
			discountedAmount);

		CommerceMoney discountAmount = _commerceMoneyFactory.create(
			commerceCurrency, currentDiscountAmount);

		return new CommerceDiscountValue(
			0, discountAmount,
			_getDiscountPercentage(discountedAmount, finalPrice, roundingMode),
			values);
	}

	private long _getBasePriceListId(CPInstance cpInstance)
		throws PortalException {

		CommerceCatalog commerceCatalog = cpInstance.getCommerceCatalog();

		CommercePriceList basePriceList =
			_commercePriceListLocalService.getCommerceCatalogBasePriceList(
				commerceCatalog.getGroupId());

		if (basePriceList != null) {
			return basePriceList.getCommercePriceListId();
		}

		throw new CommerceUndefinedBasePriceListException();
	}

	private CommerceDiscountHelper _getCommerceDiscountHelper()
		throws ConfigurationException {

		CommercePricingConfiguration commercePricingConfiguration =
			_configurationProvider.getSystemConfiguration(
				CommercePricingConfiguration.class);

		String commerceDiscountApplicationMethod =
			commercePricingConfiguration.commerceDiscountApplicationMethod();

		if (!_commerceDiscountHelperMap.containsKey(
				commerceDiscountApplicationMethod)) {

			if (_log.isWarnEnabled()) {
				_log.warn(
					"No commerce discount helper specified for " +
						commerceDiscountApplicationMethod);
			}

			return null;
		}

		return _commerceDiscountHelperMap.get(
			commerceDiscountApplicationMethod);
	}

	private CommerceDiscountValue _getCommerceDiscountValue(
			long cpInstanceId, long commercePriceListId, int quantity,
			BigDecimal finalPrice, CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceListId, cpInstance.getCPInstanceUuid(), false);

		BigDecimal[] values = new BigDecimal[4];

		if ((commercePriceEntry != null) &&
			!commercePriceEntry.isDiscountDiscovery()) {

			if (!commercePriceEntry.isHasTierPrice()) {
				values[0] = commercePriceEntry.getDiscountLevel1();
				values[1] = commercePriceEntry.getDiscountLevel2();
				values[2] = commercePriceEntry.getDiscountLevel3();
				values[3] = commercePriceEntry.getDiscountLevel4();

				return _calculateCommerceDiscountValue(
					values, finalPrice, commerceContext);
			}
			else if (commercePriceEntry.isHasTierPrice() &&
					 commercePriceEntry.isBulkPricing()) {

				CommerceTierPriceEntry commerceTierPriceEntry =
					_commerceTierPriceEntryLocalService.
						findClosestCommerceTierPriceEntry(
							commercePriceEntry.getCommercePriceEntryId(),
							quantity);

				if (commerceTierPriceEntry != null) {
					values[0] = commerceTierPriceEntry.getDiscountLevel1();
					values[1] = commerceTierPriceEntry.getDiscountLevel2();
					values[2] = commerceTierPriceEntry.getDiscountLevel3();
					values[3] = commerceTierPriceEntry.getDiscountLevel4();
				}

				return _calculateCommerceDiscountValue(
					values, finalPrice, commerceContext);
			}
		}

		return _commerceDiscountCalculation.getProductCommerceDiscountValue(
			cpInstanceId, quantity, finalPrice, commerceContext);
	}

	private CommerceMoney _getCommerceMoney(
			long commercePriceListId, CommerceCurrency commerceCurrency,
			BigDecimal price)
		throws PortalException {

		CommercePriceList commercePriceList =
			_commercePriceListLocalService.getCommercePriceList(
				commercePriceListId);

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

		if (price != null) {
			return _commerceMoneyFactory.create(commerceCurrency, price);
		}

		return null;
	}

	private BigDecimal _getCommercePrice(
			CommerceMoney unitPriceMoney, long commercePriceListId,
			long cpInstanceId)
		throws PortalException {

		CommercePriceList commercePriceList =
			_commercePriceListLocalService.getCommercePriceList(
				commercePriceListId);

		BigDecimal commercePrice = null;

		if (commercePriceList != null) {
			CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
				cpInstanceId);

			CommerceCurrency commerceCurrency =
				_commerceCurrencyLocalService.getCommerceCurrency(
					commercePriceList.getCommerceCurrencyId());

			commercePrice = unitPriceMoney.getPrice();

			CommerceMoney actualCommerceMoney =
				_commercePriceModifierHelper.applyCommercePriceModifier(
					commercePriceListId, cpInstance.getCPDefinitionId(),
					_commerceMoneyFactory.create(
						commerceCurrency, commercePrice),
					commerceCurrency);

			commercePrice = actualCommerceMoney.getPrice();
		}

		return commercePrice;
	}

	private BigDecimal _getCommercePrice(
			CommercePriceEntry commercePriceEntry, int quantity,
			boolean applyModifiers)
		throws PortalException {

		if (commercePriceEntry == null) {
			return null;
		}

		CPInstance cpInstance = commercePriceEntry.getCPInstance();

		CommercePriceList commercePriceList =
			_commercePriceListLocalService.getCommercePriceList(
				commercePriceEntry.getCommercePriceListId());

		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				commercePriceList.getCommerceCurrencyId());

		BigDecimal commercePrice = BigDecimal.ZERO;

		if (!commercePriceEntry.isHasTierPrice()) {
			commercePrice = commercePriceEntry.getPrice();

			if (applyModifiers) {
				CommerceMoney actualCommerceMoney =
					_commercePriceModifierHelper.applyCommercePriceModifier(
						commercePriceEntry.getCommercePriceListId(),
						cpInstance.getCPDefinitionId(),
						_commerceMoneyFactory.create(
							commerceCurrency, commercePrice),
						commerceCurrency);

				commercePrice = actualCommerceMoney.getPrice();
			}

			return commercePrice;
		}

		if (commercePriceEntry.isBulkPricing()) {
			CommerceTierPriceEntry commerceTierPriceEntry =
				_commerceTierPriceEntryLocalService.
					findClosestCommerceTierPriceEntry(
						commercePriceEntry.getCommercePriceEntryId(), quantity);

			if (commerceTierPriceEntry != null) {
				commercePrice = commerceTierPriceEntry.getPrice();
			}

			if (applyModifiers) {
				if (applyModifiers) {
					CommerceMoney actualCommerceMoney =
						_commercePriceModifierHelper.applyCommercePriceModifier(
							commercePriceEntry.getCommercePriceListId(),
							cpInstance.getCPDefinitionId(),
							_commerceMoneyFactory.create(
								commerceCurrency, commercePrice),
							commerceCurrency);

					commercePrice = actualCommerceMoney.getPrice();
				}
			}

			return commercePrice;
		}

		int totalTierCounter = 0;

		List<CommerceTierPriceEntry> commerceTierPriceEntries =
			_commerceTierPriceEntryLocalService.findCommerceTierPriceEntries(
				commercePriceEntry.getCommercePriceEntryId(), quantity);

		for (int i = 0; i < (commerceTierPriceEntries.size() - 1); i++) {
			CommerceTierPriceEntry commerceTierPriceEntry1 =
				commerceTierPriceEntries.get(i);

			CommerceTierPriceEntry commerceTierPriceEntry2 =
				commerceTierPriceEntries.get(i + 1);

			int tierCounter =
				commerceTierPriceEntry2.getMinQuantity() - totalTierCounter - 1;

			BigDecimal currentPrice = commerceTierPriceEntry1.getPrice();

			currentPrice = currentPrice.multiply(
				BigDecimal.valueOf(tierCounter));

			commercePrice = commercePrice.add(currentPrice);

			totalTierCounter += tierCounter;
		}

		totalTierCounter = quantity - totalTierCounter;

		CommerceTierPriceEntry commerceTierPriceEntry =
			commerceTierPriceEntries.get(commerceTierPriceEntries.size() - 1);

		BigDecimal currentPrice = commerceTierPriceEntry.getPrice();

		currentPrice = currentPrice.multiply(
			BigDecimal.valueOf(totalTierCounter));

		commercePrice = commercePrice.add(currentPrice);

		commercePrice = commercePrice.divide(BigDecimal.valueOf(quantity));

		if (applyModifiers) {
			CommerceMoney actualCommerceMoney =
				_commercePriceModifierHelper.applyCommercePriceModifier(
					commercePriceEntry.getCommercePriceListId(),
					cpInstance.getCPDefinitionId(),
					_commerceMoneyFactory.create(
						commerceCurrency, commercePrice),
					commerceCurrency);

			commercePrice = actualCommerceMoney.getPrice();
		}

		return commercePrice;
	}

	private CommercePriceList _getCommercePriceList(
			long cpInstanceId, CommerceContext commerceContext,
			String commercePriceListType)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		CommercePriceListDiscovery commercePriceListDiscovery =
			_getCommercePriceListDiscovery(commercePriceListType);

		if (commercePriceListDiscovery == null) {
			return null;
		}

		return commercePriceListDiscovery.getCommercePriceList(
			cpInstance.getGroupId(), commerceAccountId,
			commerceContext.getCommerceChannelId(),
			cpInstance.getCPInstanceUuid(), commercePriceListType);
	}

	private CommercePriceListDiscovery _getCommercePriceListDiscovery(
			String commercePriceListType)
		throws PortalException {

		CommercePricingConfiguration commercePricingConfiguration =
			_configurationProvider.getSystemConfiguration(
				CommercePricingConfiguration.class);

		String discoveryMethod = CommercePricingConstants.ORDER_BY_HIERARCHY;

		if (commercePriceListType.equals(
				CommercePriceListTypeKeys.TYPE_PRICE_LIST)) {

			discoveryMethod =
				commercePricingConfiguration.commercePriceListDiscovery();
		}
		else if (commercePriceListType.equals(
					CommercePriceListTypeKeys.TYPE_PROMOTION)) {

			discoveryMethod =
				commercePricingConfiguration.commercePromotionDiscovery();
		}

		if (!_commercePriceListDiscoveryMap.containsKey(discoveryMethod)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No commerce price list discovery specified for " +
						discoveryMethod);
			}

			return null;
		}

		return _commercePriceListDiscoveryMap.get(discoveryMethod);
	}

	private long _getCommercePriceListId(
			long cpInstanceId, CommerceContext commerceContext)
		throws PortalException {

		CommercePriceList commercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PRICE_LIST);

		long commercePriceListId = 0;

		if (commercePriceList != null) {
			commercePriceListId = commercePriceList.getCommercePriceListId();
		}

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceListId, cpInstance.getCPInstanceUuid(), true);

		if (commercePriceEntry != null) {
			return commercePriceEntry.getCommercePriceListId();
		}

		return _getBasePriceListId(cpInstance);
	}

	private long _getCommercePromoPriceListId(
			long cpInstanceId, CommerceContext commerceContext)
		throws PortalException {

		CommercePriceList commercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PROMOTION);

		if (commercePriceList != null) {
			return commercePriceList.getCommercePriceListId();
		}

		return 0;
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

	private BigDecimal _getTaxValue(
			long cpInstanceId, CommerceContext commerceContext,
			BigDecimal finalPrice)
		throws PortalException {

		List<CommerceTaxValue> commerceTaxValues = null;

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder == null) {
			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			if (commerceAccount == null) {
				return BigDecimal.ZERO;
			}

			CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
				cpInstanceId);

			commerceTaxValues = _commerceTaxCalculation.getCommerceTaxValues(
				commerceContext.getSiteGroupId(), cpInstance.getCPInstanceId(),
				commerceAccount.getDefaultBillingAddressId(),
				commerceAccount.getDefaultShippingAddressId(), finalPrice,
				commerceContext);
		}
		else {
			commerceTaxValues = _commerceTaxCalculation.getCommerceTaxValues(
				commerceOrder, commerceContext);
		}

		if ((commerceTaxValues == null) || commerceTaxValues.isEmpty()) {
			return BigDecimal.ZERO;
		}

		BigDecimal taxAmount = BigDecimal.ZERO;

		for (CommerceTaxValue commerceTaxValue : commerceTaxValues) {
			taxAmount = taxAmount.add(commerceTaxValue.getAmount());
		}

		return taxAmount;
	}

	private boolean _hasViewPricePermission(CommerceContext commerceContext)
		throws PortalException {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		if ((commerceAccount != null) &&
			(commerceAccount.getType() ==
				CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS)) {

			return _portletResourcePermission.contains(
				permissionChecker, commerceAccount.getCommerceAccountGroupId(),
				CPActionKeys.VIEW_PRICE);
		}

		return _portletResourcePermission.contains(
			permissionChecker, commerceContext.getSiteGroupId(),
			CPActionKeys.VIEW_PRICE);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceProductPriceCalculationV2Impl.class);

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Reference
	private CommerceDiscountCalculation _commerceDiscountCalculation;

	private final Map<String, CommerceDiscountHelper>
		_commerceDiscountHelperMap = new ConcurrentHashMap<>();

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	private final Map<String, CommercePriceListDiscovery>
		_commercePriceListDiscoveryMap = new ConcurrentHashMap<>();

	@Reference
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Reference
	private CommercePriceModifierHelper _commercePriceModifierHelper;

	@Reference
	private CommerceTaxCalculation _commerceTaxCalculation;

	@Reference
	private CommerceTierPriceEntryLocalService
		_commerceTierPriceEntryLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference(target = "(resource.name=" + CPConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}