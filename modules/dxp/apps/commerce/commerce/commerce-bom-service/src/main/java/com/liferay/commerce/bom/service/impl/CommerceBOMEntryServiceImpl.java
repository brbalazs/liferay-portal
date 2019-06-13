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

package com.liferay.commerce.bom.service.impl;

import com.liferay.commerce.bom.constants.CommerceBOMActionKeys;
import com.liferay.commerce.bom.model.CommerceBOMDefinition;
import com.liferay.commerce.bom.model.CommerceBOMEntry;
import com.liferay.commerce.bom.service.base.CommerceBOMEntryServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

/**
 * @author Luca Pellizzon
 */
public class CommerceBOMEntryServiceImpl
	extends CommerceBOMEntryServiceBaseImpl {

	@Override
	public CommerceBOMEntry addCommerceBOMEntry(
			long userId, int number, String cpInstanceUuid, long cProductId,
			long commerceBOMDefinitionId, double positionX, double positionY,
			double radius)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceBOMActionKeys.ADD_COMMERCE_BOM_DEFINITION);

		return commerceBOMEntryLocalService.addCommerceBOMEntry(
			userId, number, cpInstanceUuid, cProductId, commerceBOMDefinitionId,
			positionX, positionY, radius);
	}

	@Override
	public CommerceBOMEntry updateCommerceBOMEntry(
			long commerceBOMEntryId, int number, String cpInstanceUuid,
			long cProductId, long commerceBOMDefinitionId, double positionX,
			double positionY, double radius)
		throws PortalException {

		_commerceBOMDefinitionModelResourcePermission.check(
			getPermissionChecker(), commerceBOMDefinitionId, ActionKeys.UPDATE);

		return commerceBOMEntryLocalService.updateCommerceBOMEntry(
			commerceBOMEntryId, number, cpInstanceUuid, cProductId,
			commerceBOMDefinitionId, positionX, positionY, radius);
	}

	private static volatile ModelResourcePermission<CommerceBOMDefinition>
		_commerceBOMDefinitionModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommerceBOMEntryServiceImpl.class,
				"_commerceBOMDefinitionModelResourcePermission",
				CommerceBOMDefinition.class);

}