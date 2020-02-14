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

package com.liferay.commerce.pricing.internal.discovery.price;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountLevel;
import com.liferay.commerce.discount.discovery.CommerceDiscountDiscovery;
import com.liferay.commerce.price.CommercePriceDiscovery;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalService;
import com.liferay.commerce.pricing.discovery.modifier.CommercePriceModifierDiscovery;
import com.liferay.commerce.pricing.discovery.price.CommercePriceValueImpl;
import com.liferay.commerce.pricing.exception.CommerceUndefinedBasePriceListException;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(service = CommercePriceDiscovery.class)
public class CommercePriceDiscoveryImpl implements CommercePriceDiscovery {

	@Override
	public List<CommercePriceValue> getCommercePriceValue(
			long commercePriceListId, long cpInstanceId, int quantity,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext)
		throws PortalException {

		List<CommercePriceValue> commercePriceValues = new ArrayList<>();

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceListId, cpInstance.getCPInstanceUuid(), true);

		if (commercePriceEntry != null) {
			return _getCommercePriceValuesByPriceEntry(
				commercePriceEntry, quantity, commerceCurrency, commerceContext,
				cpInstanceId);
		}

		CommercePriceEntry baseCommercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				_getBasePriceListId(cpInstance), cpInstance.getCPInstanceUuid(),
				false);

		if (baseCommercePriceEntry == null) {
			throw new CommerceUndefinedBasePriceListException();
		}

		if (baseCommercePriceEntry.isHasTierPrice()) {
			if (baseCommercePriceEntry.isBulkPricing()) {
				CommerceTierPriceEntry commerceTierPriceEntry =
					_commerceTierPriceEntryLocalService.
						findClosestCommerceTierPriceEntry(
							baseCommercePriceEntry.getCommercePriceEntryId(),
							quantity);

				commercePriceValues.add(
					_getCommercePriceValueWithPriceModifiers(
						commercePriceListId,
						commerceTierPriceEntry.getPriceMoney(
							commerceCurrency.getCommerceCurrencyId()),
						quantity, commerceTierPriceEntry.getMinQuantity(),
						commerceContext, cpInstanceId));

				return commercePriceValues;
			}

			List<CommerceTierPriceEntry> commerceTierPriceEntries =
				_commerceTierPriceEntryLocalService.
					findCommerceTierPriceEntries(
						baseCommercePriceEntry.getCommercePriceEntryId(),
						quantity);

			return _getCommercePriceValueWithPriceModifiers(
				baseCommercePriceEntry,
				baseCommercePriceEntry.getCommercePriceListId(),
				commerceTierPriceEntries, commerceCurrency, quantity,
				commerceContext, cpInstanceId);
		}

		commercePriceValues.add(
			_getCommercePriceValueWithPriceModifiers(
				commercePriceListId,
				baseCommercePriceEntry.getPriceMoney(
					commerceCurrency.getCommerceCurrencyId()),
				quantity, 1, commerceContext, cpInstanceId));

		return commercePriceValues;
	}

	@Override
	public List<CommercePriceValue> getCommercePromoPriceValue(
			long commercePriceListId, long cpInstanceId, int quantity,
			List<CommercePriceValue> commercePriceValues,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				commercePriceListId, cpInstance.getCPInstanceUuid(), true);

		if (commercePriceEntry != null) {
			return _getCommercePriceValuesByPriceEntry(
				commercePriceEntry, quantity, commerceCurrency, commerceContext,
				cpInstanceId);
		}

		return _getCommercePriceValueWithPriceModifiers(
			commercePriceListId, commercePriceValues, quantity, 1,
			commerceContext, cpInstanceId);
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

		return 0;
	}

	private CommercePriceValue _getCommercePriceValue(
			boolean discountDiscovery, CommerceMoney commerceMoney,
			int quantity, int minQuantity, long commercePriceListId,
			CommerceContext commerceContext, long cpInstanceId,
			BigDecimal discountLevel1, BigDecimal discountLevel2,
			BigDecimal discountLevel3, BigDecimal discountLevel4)
		throws PortalException {

		if (discountDiscovery) {
			return new CommercePriceValueImpl(
				commerceMoney, minQuantity,
				_commerceDiscountDiscovery.getProductCommerceDiscountLevels(
					commercePriceListId, commerceMoney.getPrice(), quantity,
					commerceContext, cpInstanceId));
		}

		return new CommercePriceValueImpl(
			commerceMoney, minQuantity,
			new CommerceDiscountLevel(discountLevel1),
			new CommerceDiscountLevel(discountLevel2),
			new CommerceDiscountLevel(discountLevel3),
			new CommerceDiscountLevel(discountLevel4));
	}

	private CommercePriceValue _getCommercePriceValue(
			CommercePriceEntry commercePriceEntry,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext,
			long cpInstanceId, int quantity, int minQuantity)
		throws PortalException {

		return _getCommercePriceValue(
			commercePriceEntry.isDiscountDiscovery(),
			commercePriceEntry.getPriceMoney(
				commerceCurrency.getCommerceCurrencyId()),
			quantity, minQuantity, commercePriceEntry.getCommercePriceListId(),
			commerceContext, cpInstanceId,
			commercePriceEntry.getDiscountLevel1(),
			commercePriceEntry.getDiscountLevel2(),
			commercePriceEntry.getDiscountLevel3(),
			commercePriceEntry.getDiscountLevel4());
	}

	private CommercePriceValue _getCommercePriceValue(
			CommerceTierPriceEntry commerceTierPriceEntry,
			CommercePriceEntry commercePriceEntry,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext,
			long cpInstanceId, int quantity, int minQuantity)
		throws PortalException {

		return _getCommercePriceValue(
			commerceTierPriceEntry.isDiscountDiscovery(),
			commerceTierPriceEntry.getPriceMoney(
				commerceCurrency.getCommerceCurrencyId()),
			quantity, minQuantity, commercePriceEntry.getCommercePriceListId(),
			commerceContext, cpInstanceId,
			commerceTierPriceEntry.getDiscountLevel1(),
			commerceTierPriceEntry.getDiscountLevel2(),
			commerceTierPriceEntry.getDiscountLevel3(),
			commerceTierPriceEntry.getDiscountLevel4());
	}

	private List<CommercePriceValue> _getCommercePriceValuesByPriceEntry(
			CommercePriceEntry commercePriceEntry, int quantity,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext,
			long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> commercePriceValues = new ArrayList<>();

		if (!commercePriceEntry.isHasTierPrice()) {
			commercePriceValues.add(
				_getCommercePriceValue(
					commercePriceEntry, commerceCurrency, commerceContext,
					cpInstanceId, quantity, 1));

			return commercePriceValues;
		}

		if (commercePriceEntry.isBulkPricing()) {
			CommerceTierPriceEntry commerceTierPriceEntry =
				_commerceTierPriceEntryLocalService.
					findClosestCommerceTierPriceEntry(
						commercePriceEntry.getCommercePriceEntryId(), quantity);

			if (commerceTierPriceEntry != null) {
				commercePriceValues.add(
					_getCommercePriceValue(
						commerceTierPriceEntry, commercePriceEntry,
						commerceCurrency, commerceContext, cpInstanceId,
						quantity, 1));

				return commercePriceValues;
			}

			commercePriceValues.add(
				_getCommercePriceValue(
					commercePriceEntry, commerceCurrency, commerceContext,
					cpInstanceId, quantity, 1));

			return commercePriceValues;
		}

		List<CommerceTierPriceEntry> commerceTierPriceEntries =
			_commerceTierPriceEntryLocalService.findCommerceTierPriceEntries(
				commercePriceEntry.getCommercePriceEntryId(), quantity);

		if ((commerceTierPriceEntries == null) ||
			commerceTierPriceEntries.isEmpty()) {

			commercePriceValues.add(
				_getCommercePriceValue(
					commercePriceEntry, commerceCurrency, commerceContext,
					cpInstanceId, quantity, 1));

			return commercePriceValues;
		}

		commercePriceValues.add(
			_getCommercePriceValue(
				commercePriceEntry, commerceCurrency, commerceContext,
				cpInstanceId, quantity, 1));

		for (CommerceTierPriceEntry commerceTierPriceEntry :
				commerceTierPriceEntries) {

			if (commerceTierPriceEntry != null) {
				commercePriceValues.add(
					_getCommercePriceValue(
						commerceTierPriceEntry, commercePriceEntry,
						commerceCurrency, commerceContext, cpInstanceId,
						quantity, commerceTierPriceEntry.getMinQuantity()));
			}
		}

		return commercePriceValues;
	}

	private List<CommercePriceValue> _getCommercePriceValueWithPriceModifiers(
			CommercePriceEntry commercePriceEntry, long commercePriceListId,
			List<CommerceTierPriceEntry> commerceTierPriceEntries,
			CommerceCurrency commerceCurrency, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> commercePriceValues = new ArrayList<>();

		commercePriceValues.add(
			_getCommercePriceValueWithPriceModifiers(
				commercePriceListId,
				commercePriceEntry.getPriceMoney(
					commerceCurrency.getCommerceCurrencyId()),
				quantity, 1, commerceContext, cpInstanceId));

		for (CommerceTierPriceEntry commerceTierPriceEntry :
				commerceTierPriceEntries) {

			commercePriceValues.add(
				_getCommercePriceValueWithPriceModifiers(
					commercePriceListId,
					commerceTierPriceEntry.getPriceMoney(
						commerceCurrency.getCommerceCurrencyId()),
					quantity, commerceTierPriceEntry.getMinQuantity(),
					commerceContext, cpInstanceId));
		}

		return commercePriceValues;
	}

	private CommercePriceValue _getCommercePriceValueWithPriceModifiers(
			long commercePriceListId, CommerceMoney commerceMoney, int quantity,
			int minQuantity, CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		CommerceMoney actualCommerceMoney =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceListId, cpInstance.getCPDefinitionId(),
				commerceMoney, commerceMoney.getCommerceCurrency());

		return new CommercePriceValueImpl(
			actualCommerceMoney, minQuantity,
			_commerceDiscountDiscovery.getProductCommerceDiscountLevels(
				commercePriceListId, actualCommerceMoney.getPrice(), quantity,
				commerceContext, cpInstanceId));
	}

	private List<CommercePriceValue> _getCommercePriceValueWithPriceModifiers(
			long commercePriceListId,
			List<CommercePriceValue> commercePriceValues, int quantity,
			int minQuantity, CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> finalPriceValues = new ArrayList<>();

		for (CommercePriceValue commercePriceValue : commercePriceValues) {
			finalPriceValues.add(
				_getCommercePriceValueWithPriceModifiers(
					commercePriceListId, commercePriceValue.getCommerceMoney(),
					quantity, minQuantity, commerceContext, cpInstanceId));
		}

		return finalPriceValues;
	}

	@Reference
	private CommerceDiscountDiscovery _commerceDiscountDiscovery;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Reference
	private CommercePriceModifierDiscovery _commercePriceModifierDiscovery;

	@Reference
	private CommerceTierPriceEntryLocalService
		_commerceTierPriceEntryLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

}