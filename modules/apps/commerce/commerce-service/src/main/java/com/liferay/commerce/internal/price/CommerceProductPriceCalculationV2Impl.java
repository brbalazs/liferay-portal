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
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.discount.discovery.CommerceDiscountDiscovery;
import com.liferay.commerce.dto.price.CommerceProductPriceImpl;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommercePriceDiscovery;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.list.constants.CommercePriceListTypeKeys;
import com.liferay.commerce.price.list.discovery.CommercePriceListDiscovery;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.tax.CommerceTaxCalculation;
import com.liferay.commerce.tax.CommerceTaxValue;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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

		CommercePriceList plCommercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PRICE_LIST);

		long plCommercePriceListId = 0;

		if (plCommercePriceList != null) {
			plCommercePriceListId =
				plCommercePriceList.getCommercePriceListId();
		}

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				plCommercePriceListId, cpInstanceId, quantity,
				plCommercePriceList.getCommerceCurrency(), commerceContext);

		CommercePriceValue unitCommercePriceValue = commercePriceValues.get(0);

		CommercePriceList promoCommercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PROMOTION);

		List<CommercePriceValue> commercePromoPriceValues = new ArrayList<>();

		if (promoCommercePriceList != null) {
			commercePromoPriceValues =
				_commercePriceDiscovery.getCommercePromoPriceValue(
					promoCommercePriceList.getCommercePriceListId(),
					cpInstanceId, quantity, commercePriceValues,
					promoCommercePriceList.getCommerceCurrency(),
					commerceContext);
		}

		List<CommercePriceValue> finalPriceValues = _getBestCommercePriceValues(
			commercePriceValues, commercePromoPriceValues, quantity);

		CommerceProductPriceImpl commerceProductPriceImpl =
			new CommerceProductPriceImpl();

		commerceProductPriceImpl.setQuantity(quantity);
		commerceProductPriceImpl.setUnitPrice(
			unitCommercePriceValue.getCommerceMoney());

		if (!commercePromoPriceValues.isEmpty()) {
			CommercePriceValue promoCommercePriceValue =
				commercePromoPriceValues.get(0);

			commerceProductPriceImpl.setUnitPromoPrice(
				promoCommercePriceValue.getCommerceMoney());
		}

		commerceProductPriceImpl.setCommerceDiscountValue(null);

		commerceProductPriceImpl.setCommercePriceValues(finalPriceValues);

		BigDecimal finalDiscountedPrice = _getCommercePrice(
			finalPriceValues, quantity, true);

		commerceProductPriceImpl.setTaxValue(
			_getTaxValue(cpInstanceId, commerceContext, finalDiscountedPrice));

		commerceProductPriceImpl.setFinalPrice(
			_commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), finalDiscountedPrice));

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

		CommercePriceList plCommercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PRICE_LIST);

		long plCommercePriceListId = 0;

		if (plCommercePriceList != null) {
			plCommercePriceListId =
				plCommercePriceList.getCommercePriceListId();
		}

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				plCommercePriceListId, cpInstanceId, quantity,
				plCommercePriceList.getCommerceCurrency(), commerceContext);

		CommercePriceList promoCommercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PROMOTION);

		if (promoCommercePriceList != null) {
			List<CommercePriceValue> commercePromoPriceValues =
				_commercePriceDiscovery.getCommercePromoPriceValue(
					promoCommercePriceList.getCommercePriceListId(),
					cpInstanceId, quantity, commercePriceValues,
					promoCommercePriceList.getCommerceCurrency(),
					commerceContext);

			CommercePriceValue promoCommercePriceValue =
				commercePromoPriceValues.get(0);

			return promoCommercePriceValue.getCommerceMoney();
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

		CommercePriceList plCommercePriceList = _getCommercePriceList(
			cpInstanceId, commerceContext,
			CommercePriceListTypeKeys.TYPE_PRICE_LIST);

		long plCommercePriceListId = 0;

		if (plCommercePriceList != null) {
			plCommercePriceListId =
				plCommercePriceList.getCommercePriceListId();
		}

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				plCommercePriceListId, cpInstanceId, quantity,
				plCommercePriceList.getCommerceCurrency(), commerceContext);

		CommercePriceValue unitCommercePriceValue = commercePriceValues.get(0);

		return unitCommercePriceValue.getCommerceMoney();
	}

	private List<CommercePriceValue> _getBestCommercePriceValues(
			List<CommercePriceValue> commercePriceValues,
			List<CommercePriceValue> commercePromoPriceValues, int quantity)
		throws PortalException {

		BigDecimal commercePrice = _getCommercePrice(
			commercePriceValues, quantity, false);

		BigDecimal commercePromoPrice = _getCommercePrice(
			commercePromoPriceValues, quantity, false);

		if ((commercePromoPrice == null) ||
			(commercePromoPrice.compareTo(BigDecimal.ZERO) == 0) ||
			(commercePrice.compareTo(commercePromoPrice) < 0)) {

			return commercePriceValues;
		}

		return commercePromoPriceValues;
	}

	private BigDecimal _getCommercePrice(
			List<CommercePriceValue> commercePriceValues, int quantity,
			boolean applyDiscounts)
		throws PortalException {

		if ((commercePriceValues == null) || commercePriceValues.isEmpty()) {
			return null;
		}

		BigDecimal commercePrice = BigDecimal.ZERO;

		if (commercePriceValues.size() == 1) {
			CommercePriceValue commercePriceValue = commercePriceValues.get(0);

			CommerceMoney currentMoney = commercePriceValue.getCommerceMoney();

			BigDecimal currentPrice = currentMoney.getPrice();

			if (applyDiscounts) {
				currentPrice =
					_commerceDiscountDiscovery.applyCommerceDiscounts(
						currentPrice, commercePriceValue.getDiscountLevels());
			}

			commercePrice = currentPrice.multiply(BigDecimal.valueOf(quantity));

			return commercePrice;
		}

		int commercePriceValueSize = commercePriceValues.size();

		int totalTierCounter = 0;

		for (int i = 0; i < (commercePriceValueSize - 1); i++) {
			CommercePriceValue commercePriceValue0 = commercePriceValues.get(i);

			CommercePriceValue commercePriceValue1 = commercePriceValues.get(
				i + 1);

			int tierCounter =
				commercePriceValue1.getMinQuantity() - totalTierCounter - 1;

			CommerceMoney currentMoney = commercePriceValue0.getCommerceMoney();

			BigDecimal currentPrice = currentMoney.getPrice();

			if (applyDiscounts) {
				currentPrice =
					_commerceDiscountDiscovery.applyCommerceDiscounts(
						currentPrice, commercePriceValue0.getDiscountLevels());
			}

			currentPrice = currentPrice.multiply(
				BigDecimal.valueOf(tierCounter));

			commercePrice = commercePrice.add(currentPrice);

			totalTierCounter += tierCounter;
		}

		totalTierCounter = quantity - totalTierCounter;

		CommercePriceValue commercePriceValue = commercePriceValues.get(
			commercePriceValueSize - 1);

		CommerceMoney currentMoney = commercePriceValue.getCommerceMoney();

		BigDecimal currentPrice = currentMoney.getPrice();

		if (applyDiscounts) {
			currentPrice = _commerceDiscountDiscovery.applyCommerceDiscounts(
				currentPrice, commercePriceValue.getDiscountLevels());
		}

		currentPrice = currentPrice.multiply(
			BigDecimal.valueOf(totalTierCounter));

		commercePrice = commercePrice.add(currentPrice);

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

		List<CommerceAccountGroup> commerceAccountGroups =
			_commerceAccountGroupLocalService.
				getCommerceAccountGroupsByCommerceAccountId(
					commerceAccount.getCommerceAccountId());

		Stream<CommerceAccountGroup> stream = commerceAccountGroups.stream();

		long[] commerceAccountGroupIds = stream.mapToLong(
			CommerceAccountGroup::getCommerceAccountGroupId
		).toArray();

		return _commercePriceListDiscovery.getCommercePriceList(
			cpInstance.getGroupId(), commercePriceListType,
			cpInstance.getCPInstanceUuid(), commerceAccountId,
			commerceAccountGroupIds, commerceContext.getCommerceChannelId());
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
				commerceAccount.getDefaultBillingAddressId(), finalPrice,
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

	@Reference
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Reference
	private CommerceDiscountDiscovery _commerceDiscountDiscovery;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommercePriceDiscovery _commercePriceDiscovery;

	@Reference
	private CommercePriceListDiscovery _commercePriceListDiscovery;

	@Reference
	private CommerceTaxCalculation _commerceTaxCalculation;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference(target = "(resource.name=" + CPConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}