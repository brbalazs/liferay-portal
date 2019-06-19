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

package com.liferay.commerce.bom.rest.internal.dto.v1_0.converter;

import com.liferay.commerce.bom.rest.dto.v1_0.Breadcrumb;
import com.liferay.commerce.bom.rest.dto.v1_0.Folder;
import com.liferay.commerce.bom.rest.dto.v1_0.ItemData;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "model.class.name=",
	service = {FolderDTOConverter.class, DTOConverter.class}
)
public class FolderDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return Folder.class.getSimpleName();
	}

	public Folder toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		return new Folder() {
			{
				breadcrumbs = new Breadcrumb[0];
				data = new ItemData();
			}
		};
	}

}