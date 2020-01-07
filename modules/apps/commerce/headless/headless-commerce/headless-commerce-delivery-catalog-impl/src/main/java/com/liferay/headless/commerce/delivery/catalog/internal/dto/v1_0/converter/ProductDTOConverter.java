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

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetTagService;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngineRegistry;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;

import java.util.List;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 */
@Component(
	property = "model.class.name=com.liferay.commerce.product.catalog.CPCatalogEntry",
	service = {DTOConverter.class, ProductDTOConverter.class}
)
public class ProductDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return Product.class.getSimpleName();
	}

	public Product toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		ProductDTOConverterContext cpCatalogEntryDTOConverterConvertContext =
			(ProductDTOConverterContext)dtoConverterContext;

		CPDefinition cpDefinition = _cpDefinitionService.getCPDefinition(
			cpCatalogEntryDTOConverterConvertContext.getResourcePrimKey());

		String languageId = LanguageUtil.getLanguageId(
			cpCatalogEntryDTOConverterConvertContext.getLocale());

		ExpandoBridge expandoBridge = cpDefinition.getExpandoBridge();

		Company company = _companyLocalService.getCompany(
			cpDefinition.getCompanyId());

		String portalURL = company.getPortalURL(0);

		return new Product() {
			{
				createDate = cpDefinition.getCreateDate();
				description = cpDefinition.getDescription();
				expando = expandoBridge.getAttributes();
				externalReferenceCode = cProduct.getExternalReferenceCode();
				friendlyUrl = _getFriendlyUrl(
					cpDefinition, languageId, portalURL);
				id = cpDefinition.getCPDefinitionId();
				metaDescription = cpDefinition.getMetaDescription(languageId);
				metaKeyword = cpDefinition.getMetaKeywords(languageId);
				metaTitle = cpDefinition.getMetaTitle(languageId);
				modifiedDate = cpDefinition.getModifiedDate();
				name = cpDefinition.getName();
				productId = cpDefinition.getCProductId();
				productType = cpDefinition.getProductTypeName();
				shortDescription = cpDefinition.getShortDescription();
				slug = cpDefinition.getURL(languageId);
				tags = _getTags(cpDefinition);
				urlImage = portalURL + cpDefinition.getDefaultImageFileURL();
			}
		};
	}

	private String _getFriendlyUrl(
		CPDefinition cpDefinition, String languageId, String portalURL) {

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		return StringBundler.concat(
			portalURL, "/group/", commerceCatalog.getName(), "/p/",
			cpDefinition.getURL(languageId));
	}

	private String[] _getTags(CPDefinition cpDefinition) {
		List<AssetTag> assetEntryAssetTags = _assetTagService.getTags(
			cpDefinition.getModelClassName(), cpDefinition.getCPDefinitionId());

		Stream<AssetTag> stream = assetEntryAssetTags.stream();

		return stream.map(
			AssetTag::getName
		).toArray(
			String[]::new
		);
	}

	@Reference
	private AssetTagService _assetTagService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private CPContentHelper _cpContentHelper;

	@Reference
	private CPDefinitionHelper _cpDefinitionHelper;

	@Reference
	private CPDefinitionInventoryEngineRegistry
		_cpDefinitionInventoryEngineRegistry;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}