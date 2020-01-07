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

import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Sku;
import com.liferay.headless.commerce.delivery.catalog.internal.dto.v1_0.converter.CPSkuDTOConverterContext;
import com.liferay.headless.commerce.delivery.catalog.resource.v1_0.SkuResource;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldId;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Andrea Sbarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/sku.properties",
	scope = ServiceScope.PROTOTYPE, service = SkuResource.class
)
public class SkuResourceImpl extends BaseSkuResourceImpl {

	@NestedField(parentClass = Product.class, value = "skus")
	@Override
	public Page<Sku> getProductIdSkusPage(
			@NestedFieldId("productId") @NotNull Long id, Pagination pagination)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			return super.getProductIdSkusPage(id, pagination);
		}

		List<CPInstance> cpInstances =
			_cpInstanceService.getCPDefinitionInstances(
				cpDefinition.getCPDefinitionId(),
				WorkflowConstants.STATUS_APPROVED,
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);

		int totalItems = _cpInstanceService.getCPDefinitionInstancesCount(
			cpDefinition.getCPDefinitionId(),
			WorkflowConstants.STATUS_APPROVED);

		return Page.of(
			_toSKUs(cpInstances, cpDefinition), pagination, totalItems);
	}

	private List<Sku> _toSKUs(
			List<CPInstance> cpInstances, CPDefinition cpDefinition)
		throws Exception {

		List<Sku> skus = new ArrayList<>();

		DTOConverter skuDTOConverter = _dtoConverterRegistry.getDTOConverter(
			CPSku.class.getName());

		for (CPInstance cpInstance : cpInstances) {
			skus.add(
				(Sku)skuDTOConverter.toDTO(
					new CPSkuDTOConverterContext(
						contextAcceptLanguage.getPreferredLocale(),
						cpInstance.getCPInstanceId(), cpDefinition,
						contextCompany.getCompanyId())));
		}

		return skus;
	}

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}