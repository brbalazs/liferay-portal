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

import com.liferay.commerce.application.model.CommerceApplicationBrand;
import com.liferay.commerce.application.service.base.CommerceApplicationBrandLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

/**
 * @author Luca Pellizzon
 */
public class CommerceApplicationBrandLocalServiceImpl
	extends CommerceApplicationBrandLocalServiceBaseImpl {

	@Override
	public CommerceApplicationBrand addCommerceApplicationBrand(
			long userId, String name, long logoId)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long commerceBOMDefinitionId = counterLocalService.increment();

		CommerceApplicationBrand commerceApplicationBrand =
			commerceApplicationBrandPersistence.create(commerceBOMDefinitionId);

		commerceApplicationBrand.setCompanyId(user.getCompanyId());
		commerceApplicationBrand.setUserId(user.getUserId());
		commerceApplicationBrand.setUserName(user.getFullName());
		commerceApplicationBrand.setName(name);
		commerceApplicationBrand.setLogoId(logoId);

		return commerceApplicationBrandPersistence.update(
			commerceApplicationBrand);
	}

	@Override
	public CommerceApplicationBrand updateCommerceApplicationBrand(
			long commerceApplicationBrandId, String name, long logoId)
		throws PortalException {

		CommerceApplicationBrand commerceApplicationBrand =
			commerceApplicationBrandLocalService.getCommerceApplicationBrand(
				commerceApplicationBrandId);

		commerceApplicationBrand.setName(name);
		commerceApplicationBrand.setLogoId(logoId);

		return commerceApplicationBrandPersistence.update(
			commerceApplicationBrand);
	}

}