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

package com.liferay.commerce.application.service.impl;

import com.liferay.commerce.application.constants.CommerceApplicationActionKeys;
import com.liferay.commerce.application.model.CommerceApplicationBrand;
import com.liferay.commerce.application.service.base.CommerceApplicationBrandServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

/**
 * @author Luca Pellizzon
 */
public class CommerceApplicationBrandServiceImpl
	extends CommerceApplicationBrandServiceBaseImpl {

	@Override
	public CommerceApplicationBrand addCommerceApplicationBrand(
			long userId, String name, long logoId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceApplicationActionKeys.ADD_COMMERCE_BRAND);

		return commerceApplicationBrandLocalService.addCommerceApplicationBrand(
			userId, name, logoId);
	}

	@Override
	public CommerceApplicationBrand updateCommerceApplicationBrand(
			long commerceApplicationBrandId, String name, long logoId)
		throws PortalException {

		_commerceApplicationBrandModelResourcePermission.check(
			getPermissionChecker(), commerceApplicationBrandId,
			ActionKeys.UPDATE);

		return commerceApplicationBrandLocalService.
			updateCommerceApplicationBrand(
				commerceApplicationBrandId, name, logoId);
	}

	private static volatile ModelResourcePermission<CommerceApplicationBrand>
		_commerceApplicationBrandModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommerceApplicationBrandServiceImpl.class,
				"_commerceApplicationBrandModelResourcePermission",
				CommerceApplicationBrand.class);

}