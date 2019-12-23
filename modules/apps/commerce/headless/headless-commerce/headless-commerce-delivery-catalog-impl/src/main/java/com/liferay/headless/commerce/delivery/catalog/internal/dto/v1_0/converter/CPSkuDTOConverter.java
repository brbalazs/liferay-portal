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

import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngine;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngineRegistry;
import com.liferay.commerce.model.CPDefinitionInventory;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.service.CPDefinitionInventoryLocalService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Availability;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Price;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Sku;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Locale;

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

		CPSku cpSku = cpSkuDTOConverterConvertContext.getCPSku();

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpSku.getCPInstanceId());

		CPDefinitionInventory cpDefinitionInventory =
			_cpDefinitionInventoryLocalService.
				fetchCPDefinitionInventoryByCPDefinitionId(
					cpSkuDTOConverterConvertContext.getCPDefinitionId());

		CPDefinitionInventoryEngine cpDefinitionInventoryEngine =
			_cpDefinitionInventoryEngineRegistry.getCPDefinitionInventoryEngine(
				cpDefinitionInventory);

		return new Sku() {
			{
				allowedOrderQuantities =
					cpDefinitionInventoryEngine.getAllowedOrderQuantities(
						cpInstance);
				availability = _getAvailability(cpSku);
				maxOrderQuantity =
					cpDefinitionInventoryEngine.getMinOrderQuantity(cpInstance);
				minOrderQuantity =
					cpDefinitionInventoryEngine.getMinOrderQuantity(cpInstance);
				price = _getPrice(
					cpSku, cpSkuDTOConverterConvertContext.getLocale());
				sku = cpSku.getSku();
			}
		};
	}

	private Availability _getAvailability(CPSku cpSku) {
		return new Availability() {
			{
				availabilityLabel =
					cpSku.isPurchasable() && cpSku.isPublished() ? "Available" :
					"Not available";
				availabilityNumber =
					cpSku.isPurchasable() && cpSku.isPublished() ? 1 : 0;
			}
		};
	}

	private Price _getPrice(CPSku cpSku, Locale locale) throws PortalException {
		return new Price() {
			{
				price = _commercePriceFormatter.format(
					cpSku.getPrice(), locale);
				promoPrice = _commercePriceFormatter.format(
					cpSku.getPromoPrice(), locale);
			}
		};
	}

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
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}