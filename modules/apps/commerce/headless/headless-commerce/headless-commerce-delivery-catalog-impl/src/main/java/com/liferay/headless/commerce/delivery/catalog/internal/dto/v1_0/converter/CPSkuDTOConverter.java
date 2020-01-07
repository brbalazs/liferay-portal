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

package com.liferay.headless.commerce.delivery.catalog.internal.dto.v1_0.converter;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyService;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngineRegistry;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CPDefinitionInventoryLocalService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Price;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Sku;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 */
@Component(
	property = "model.class.name=com.liferay.commerce.product.catalog.CPSku",
	service = {CPSkuDTOConverter.class, DTOConverter.class}
)
public class CPSkuDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return Product.class.getSimpleName();
	}

	public Sku toDTO(DTOConverterContext dtoConverterContext) throws Exception {
		CPSkuDTOConverterContext cpSkuDTOConverterConvertContext =
			(CPSkuDTOConverterContext)dtoConverterContext;

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpSkuDTOConverterConvertContext.getResourcePrimKey());

		CPDefinition cpDefinition =
			cpSkuDTOConverterConvertContext.getCPDefinition();

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		CommerceCurrency commerceCurrency =
			_commerceCurrencyService.getCommerceCurrency(
				cpSkuDTOConverterConvertContext.getCompanyId(),
				commerceCatalog.getCommerceCurrencyCode());

		return new Sku() {
			{
				depth = cpInstance.getDepth();
				displayDate = cpInstance.getDisplayDate();
				expirationDate = cpInstance.getExpirationDate();
				gtin = cpInstance.getGtin();
				height = cpInstance.getHeight();
				manufacturerPartNumber = cpInstance.getManufacturerPartNumber();
				options = _getOptions(cpInstance);
				price = _getPrice(
					commerceCurrency, cpInstance,
					cpSkuDTOConverterConvertContext.getLocale());
				published = cpInstance.isPublished();
				purchasable = cpInstance.isPurchasable();
				sku = cpInstance.getSku();
				weight = cpInstance.getWeight();
				width = cpInstance.getWidth();
			}
		};
	}

	private Map<String, String> _getOptions(CPInstance cpInstance)
		throws PortalException {

		Map<String, String> options = new HashMap<>();

		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpDefinitionOptionRelsMap =
				_cpInstanceHelper.getCPDefinitionOptionRelsMap(
					cpInstance.getCPDefinitionId(), cpInstance.getJson());

		for (Map.Entry<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				entry : cpDefinitionOptionRelsMap.entrySet()) {

			CPDefinitionOptionRel cpDefinitionOptionRel = entry.getKey();

			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
				entry.getValue();

			for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
					cpDefinitionOptionValueRels) {

				options.put(
					String.valueOf(
						cpDefinitionOptionRel.getCPDefinitionOptionRelId()),
					String.valueOf(
						cpDefinitionOptionValueRel.
							getCPDefinitionOptionValueRelId()));
			}
		}

		return options;
	}

	private Price _getPrice(
			CommerceCurrency currencyCode, CPInstance cpInstance, Locale locale)
		throws PortalException {

		BigDecimal cpInstancePrice = cpInstance.getPrice();
		BigDecimal cpInstancePromoPrice = cpInstance.getPromoPrice();

		return new Price() {
			{
				price = cpInstancePrice.doubleValue();
				priceFormatted = _commercePriceFormatter.format(
					currencyCode, cpInstancePrice, locale);
				promoPrice = cpInstancePromoPrice.doubleValue();
				promoPriceFormatted = _commercePriceFormatter.format(
					currencyCode, cpInstancePromoPrice, locale);
			}
		};
	}

	@Reference
	private CommerceCurrencyService _commerceCurrencyService;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private CPContentHelper _cpContentHelper;

	@Reference
	private CPDefinitionInventoryEngineRegistry
		_cpDefinitionInventoryEngineRegistry;

	@Reference
	private CPDefinitionInventoryLocalService
		_cpDefinitionInventoryLocalService;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}