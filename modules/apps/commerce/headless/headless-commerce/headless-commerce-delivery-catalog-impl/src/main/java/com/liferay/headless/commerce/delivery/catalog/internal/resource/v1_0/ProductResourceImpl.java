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

package com.liferay.headless.commerce.delivery.catalog.internal.resource.v1_0;

import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPQuery;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.internal.dto.v1_0.converter.ProductDTOConverterContext;
import com.liferay.headless.commerce.delivery.catalog.resource.v1_0.ProductResource;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Andrea Sbarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/product.properties",
	scope = ServiceScope.PROTOTYPE, service = ProductResource.class
)
public class ProductResourceImpl extends BaseProductResourceImpl {

	@Override
	public Product getProduct(@NotNull Long id) throws Exception {
		CPDefinition cpDefinition = _cpDefinitionService.getCPDefinition(id);

		return super.getProduct(id);
	}

	@Override
	public Page<Product> getStoreChannelProductsPage(
			@NotNull Long channelId, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		long companyId = contextCompany.getCompanyId();
		SearchContext searchContext = new SearchContext();

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);

		CommerceChannel commerceChannel =
			_commerceChannelService.fetchCommerceChannel(channelId);

		if (commerceChannel != null) {
			attributes.put(
				"commerceChannelGroupId", commerceChannel.getGroupId());
		}

		searchContext.setAttributes(attributes);
		searchContext.setCompanyId(companyId);

		List<CommerceCatalog> commerceCatalogs =
			_commerceCatalogService.getCommerceCatalogs(
				companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Stream<CommerceCatalog> commerceCatalogStream =
			commerceCatalogs.stream();

		searchContext.setGroupIds(
			commerceCatalogStream.mapToLong(
				CommerceCatalog::getGroupId
			).toArray());

		CPQuery cpQuery = new CPQuery();

		cpQuery.setOrderByCol1("title");
		cpQuery.setOrderByCol2("modifiedDate");
		cpQuery.setOrderByType1("ASC");
		cpQuery.setOrderByType2("DESC");

		CPDataSourceResult cpDataSourceResult = _cpDefinitionHelper.search(
			commerceChannel.getGroupId(), searchContext, cpQuery,
			pagination.getStartPosition(), pagination.getEndPosition());

		return Page.of(
			_toProducts(cpDataSourceResult), pagination,
			cpDataSourceResult.getLength());
	}

	@Context
	protected Company contextCompany;

	private List<Product> _toProducts(CPDataSourceResult cpDataSourceResult)
		throws Exception {

		List<Product> products = new ArrayList<>();
		DTOConverter cpCatalogEntryDTOConverter =
			_dtoConverterRegistry.getDTOConverter(
				CPCatalogEntry.class.getName());

		for (CPCatalogEntry cpCatalogEntry :
				cpDataSourceResult.getCPCatalogEntries()) {

			products.add(
				(Product)cpCatalogEntryDTOConverter.toDTO(
					new ProductDTOConverterContext(
						contextAcceptLanguage.getPreferredLocale(),
						cpCatalogEntry.getCPDefinitionId(), cpCatalogEntry)));
		}

		return products;
	}

	@Reference
	private CommerceCatalogService _commerceCatalogService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CPDefinitionHelper _cpDefinitionHelper;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}