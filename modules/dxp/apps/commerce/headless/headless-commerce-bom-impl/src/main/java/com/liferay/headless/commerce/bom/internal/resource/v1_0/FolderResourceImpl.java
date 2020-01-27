/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.headless.commerce.bom.internal.resource.v1_0;

import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.commerce.bom.service.CommerceBOMFolderService;
import com.liferay.headless.commerce.bom.dto.v1_0.Folder;
import com.liferay.headless.commerce.bom.dto.v1_0.ItemData;
import com.liferay.headless.commerce.bom.dto.v1_0.Product;
import com.liferay.headless.commerce.bom.dto.v1_0.Spot;
import com.liferay.headless.commerce.bom.internal.dto.v1_0.converter.util.BreadcrumbDTOConverterUtil;
import com.liferay.headless.commerce.bom.resource.v1_0.FolderResource;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterRegistry;
import com.liferay.portal.kernel.util.GetterUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/folder.properties",
	scope = ServiceScope.PROTOTYPE, service = FolderResource.class
)
public class FolderResourceImpl extends BaseFolderResourceImpl {

	@Override
	public Folder getFolder(Long id) throws Exception {
		DTOConverter breadcrumbDTOConverter =
			_dtoConverterRegistry.getDTOConverter("breadcrumb");

		CommerceBOMFolder commerceBOMFolder = null;

		if (id > 0) {
			commerceBOMFolder = _commerceBOMFolderService.getCommerceBOMFolder(
				GetterUtil.getLong(id));
		}

		Folder folder = new Folder();

		folder.setBreadcrumbs(
			BreadcrumbDTOConverterUtil.getBreadcrumbs(
				breadcrumbDTOConverter, commerceBOMFolder,
				contextAcceptLanguage.getPreferredLocale()));

		ItemData itemData = new ItemData();

		itemData.setProducts(new Product[0]);
		itemData.setSpots(new Spot[0]);

		folder.setData(itemData);

		return folder;
	}

	@Reference
	private CommerceBOMFolderService _commerceBOMFolderService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

}