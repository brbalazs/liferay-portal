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
import com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel;
import com.liferay.commerce.price.list.model.CommercePriceListRel;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListPriceModifierRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListRelLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalService;
import com.liferay.commerce.pricing.discovery.modifier.CommercePriceModifierDiscovery;
import com.liferay.commerce.pricing.discovery.price.CommercePriceValueImpl;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.service.CommercePriceModifierLocalService;
import com.liferay.commerce.pricing.type.CommercePriceModifierType;
import com.liferay.commerce.pricing.type.CommercePriceModifierTypeRegistry;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
			throw new PortalException();
		}

		if (baseCommercePriceEntry.isHasTierPrice()) {
			if (commercePriceEntry.isBulkPricing()) {
				CommerceTierPriceEntry commerceTierPriceEntry =
					_commerceTierPriceEntryLocalService.
						findClosestCommerceTierPriceEntry(
							commercePriceEntry.getCommercePriceEntryId(),
							quantity);

				commercePriceValues.add(
					_getCommercePriceValueWithPriceModifiers(
						commercePriceListId,
						commerceTierPriceEntry.getPriceMoney(
							commerceCurrency.getCommerceCurrencyId()),
						quantity, commerceContext, cpInstanceId));

				return commercePriceValues;
			}

			List<CommerceTierPriceEntry> commerceTierPriceEntries =
				_commerceTierPriceEntryLocalService.
					findCommerceTierPriceEntries(
						commercePriceEntry.getCommercePriceEntryId(), quantity);

			return _getCommercePriceValueWithPriceModifiers(
				baseCommercePriceEntry.getCommercePriceListId(),
				commerceTierPriceEntries, commerceCurrency, quantity,
				commerceContext, cpInstanceId);
		}

		commercePriceValues.add(
			_getCommercePriceValueWithPriceModifiers(
				commercePriceListId,
				baseCommercePriceEntry.getPriceMoney(
					commerceCurrency.getCommerceCurrencyId()),
				quantity, commerceContext, cpInstanceId));

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
			commercePriceListId, commercePriceValues, quantity, commerceContext,
			cpInstanceId);
	}

	private long _getBasePriceListId(CPInstance cpInstance)
		throws PortalException {

		CommerceCatalog commerceCatalog = cpInstance.getCommerceCatalog();

		List<CommercePriceListRel> commercePriceListRels =
			_commercePriceListRelLocalService.getCommercePriceListRels(
				CommerceCatalog.class.getName(),
				commerceCatalog.getCommerceCatalogId());

		if ((commercePriceListRels != null) &&
			!commercePriceListRels.isEmpty()) {

			CommercePriceListRel commercePriceListRel =
				commercePriceListRels.get(0);

			return commercePriceListRel.getCommercePriceListId();
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

	private List<CommercePriceValue> _getCommercePriceValuesByPriceEntry(
			CommercePriceEntry commercePriceEntry, int quantity,
			CommerceCurrency commerceCurrency, CommerceContext commerceContext,
			long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> commercePriceValues = new ArrayList<>();

		if (!commercePriceEntry.isHasTierPrice()) {
			commercePriceValues.add(
				_getCommercePriceValue(
					commercePriceEntry.isDiscountDiscovery(),
					commercePriceEntry.getPriceMoney(
						commerceCurrency.getCommerceCurrencyId()),
					quantity, Integer.MAX_VALUE,
					commercePriceEntry.getCommercePriceListId(),
					commerceContext, cpInstanceId,
					commercePriceEntry.getDiscountLevel1(),
					commercePriceEntry.getDiscountLevel2(),
					commercePriceEntry.getDiscountLevel3(),
					commercePriceEntry.getDiscountLevel4()));

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
						commerceTierPriceEntry.isDiscountDiscovery(),
						commerceTierPriceEntry.getPriceMoney(
							commerceCurrency.getCommerceCurrencyId()),
						quantity, Integer.MAX_VALUE,
						commercePriceEntry.getCommercePriceListId(),
						commerceContext, cpInstanceId,
						commerceTierPriceEntry.getDiscountLevel1(),
						commerceTierPriceEntry.getDiscountLevel2(),
						commerceTierPriceEntry.getDiscountLevel3(),
						commerceTierPriceEntry.getDiscountLevel4()));

				return commercePriceValues;
			}
		}

		List<CommerceTierPriceEntry> commerceTierPriceEntries =
			_commerceTierPriceEntryLocalService.findCommerceTierPriceEntries(
				commercePriceEntry.getCommercePriceEntryId(), quantity);

		for (CommerceTierPriceEntry commerceTierPriceEntry :
				commerceTierPriceEntries) {

			if (commerceTierPriceEntry != null) {
				commercePriceValues.add(
					_getCommercePriceValue(
						commerceTierPriceEntry.isDiscountDiscovery(),
						commerceTierPriceEntry.getPriceMoney(
							commerceCurrency.getCommerceCurrencyId()),
						quantity, commerceTierPriceEntry.getMinQuantity(),
						commercePriceEntry.getCommercePriceListId(),
						commerceContext, cpInstanceId,
						commerceTierPriceEntry.getDiscountLevel1(),
						commerceTierPriceEntry.getDiscountLevel2(),
						commerceTierPriceEntry.getDiscountLevel3(),
						commerceTierPriceEntry.getDiscountLevel4()));
			}
		}

		return commercePriceValues;
	}

	private CommercePriceValue _getCommercePriceValueWithPriceModifiers(
			long commercePriceListId, CommerceMoney commerceMoney, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		List<CommercePriceListPriceModifierRel>
			commercePriceListPriceModifierRels =
				_commercePriceListPriceModifierRelLocalService.
					getCommercePriceListPriceModifierRels(commercePriceListId);

		CommerceMoney actualCommerceMoney = commerceMoney;

		if ((commercePriceListPriceModifierRels != null) &&
			!commercePriceListPriceModifierRels.isEmpty()) {

			Stream<CommercePriceListPriceModifierRel> stream =
				commercePriceListPriceModifierRels.stream();

			long[] commercePriceModifierIds = stream.mapToLong(
				CommercePriceListPriceModifierRel::getCommercePriceModifierId
			).toArray();

			List<CommercePriceModifier> commercePriceModifiers =
				_commercePriceModifierLocalService.
					getQualifiedCommercePriceModifiers(
						commercePriceModifierIds,
						cpInstance.getCPDefinitionId());

			if ((commercePriceModifiers != null) &&
				!commercePriceModifiers.isEmpty()) {

				CommercePriceModifier commercePriceModifier =
					_commercePriceModifierDiscovery.
						getApplicableCommercePriceModifier(
							commercePriceModifiers, commerceMoney,
							commerceMoney.getCommerceCurrency());

				CommercePriceModifierType commercePriceModifierType =
					_commercePriceModifierTypeRegistry.
						getCommercePriceModifierType(
							commercePriceModifier.getModifierType());

				actualCommerceMoney = commercePriceModifierType.evaluate(
					commerceMoney, commercePriceModifier,
					commerceMoney.getCommerceCurrency());
			}
		}

		return new CommercePriceValueImpl(
			actualCommerceMoney, Integer.MAX_VALUE,
			_commerceDiscountDiscovery.getProductCommerceDiscountLevels(
				commercePriceListId, actualCommerceMoney.getPrice(), quantity,
				commerceContext, cpInstanceId));
	}

	private List<CommercePriceValue> _getCommercePriceValueWithPriceModifiers(
			long commercePriceListId,
			List<CommercePriceValue> commercePriceValues, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> finalPriceValues = new ArrayList<>();

		for (CommercePriceValue commercePriceValue : commercePriceValues) {
			finalPriceValues.add(
				_getCommercePriceValueWithPriceModifiers(
					commercePriceListId, commercePriceValue.getCommerceMoney(),
					quantity, commerceContext, cpInstanceId));
		}

		return finalPriceValues;
	}

	private List<CommercePriceValue> _getCommercePriceValueWithPriceModifiers(
			long commercePriceListId,
			List<CommerceTierPriceEntry> commerceTierPriceEntries,
			CommerceCurrency commerceCurrency, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		List<CommercePriceValue> commercePriceValues = new ArrayList<>();

		for (CommerceTierPriceEntry commerceTierPriceEntry :
				commerceTierPriceEntries) {

			commercePriceValues.add(
				_getCommercePriceValueWithPriceModifiers(
					commercePriceListId,
					commerceTierPriceEntry.getPriceMoney(
						commerceCurrency.getCommerceCurrencyId()),
					quantity, commerceContext, cpInstanceId));
		}

		return commercePriceValues;
	}

	@Reference
	private CommerceDiscountDiscovery _commerceDiscountDiscovery;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CommercePriceListPriceModifierRelLocalService
		_commercePriceListPriceModifierRelLocalService;

	@Reference
	private CommercePriceListRelLocalService _commercePriceListRelLocalService;

	@Reference
	private CommercePriceModifierDiscovery _commercePriceModifierDiscovery;

	@Reference
	private CommercePriceModifierLocalService
		_commercePriceModifierLocalService;

	@Reference
	private CommercePriceModifierTypeRegistry
		_commercePriceModifierTypeRegistry;

	@Reference
	private CommerceTierPriceEntryLocalService
		_commerceTierPriceEntryLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

}