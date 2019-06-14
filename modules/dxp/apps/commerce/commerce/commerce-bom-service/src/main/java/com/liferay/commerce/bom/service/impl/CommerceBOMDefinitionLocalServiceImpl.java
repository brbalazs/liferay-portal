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

import com.liferay.commerce.bom.model.CommerceBOMDefinition;
import com.liferay.commerce.bom.service.base.CommerceBOMDefinitionLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;

import java.util.List;

/**
 * @author Luca Pellizzon
 */
public class CommerceBOMDefinitionLocalServiceImpl
	extends CommerceBOMDefinitionLocalServiceBaseImpl {

	@Override
	public CommerceBOMDefinition addCommerceBOMDefinition(
			long userId, String name, long imageId, String friendlyUrl,
			long commerceBOMFolderId)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long commerceBOMDefinitionId = counterLocalService.increment();

		CommerceBOMDefinition commerceBOMDefinition =
			commerceBOMDefinitionPersistence.create(commerceBOMDefinitionId);

		commerceBOMDefinition.setCompanyId(user.getCompanyId());
		commerceBOMDefinition.setUserId(user.getUserId());
		commerceBOMDefinition.setUserName(user.getFullName());
		commerceBOMDefinition.setName(name);
		commerceBOMDefinition.setImageId(imageId);
		commerceBOMDefinition.setFriendlyUrl(friendlyUrl);
		commerceBOMDefinition.setCommerceBOMFolderId(commerceBOMFolderId);

		commerceBOMDefinition = commerceBOMDefinitionPersistence.update(
			commerceBOMDefinition);

		// Resources

		resourceLocalService.addResources(
			user.getCompanyId(), GroupConstants.DEFAULT_LIVE_GROUP_ID,
			user.getUserId(), CommerceBOMDefinition.class.getName(),
			commerceBOMDefinition.getCommerceBOMDefinitionId(), false, false,
			false);

		return commerceBOMDefinition;
	}

	@Override
	public CommerceBOMDefinition deleteCommerceBOMDefinition(
			CommerceBOMDefinition commerceBOMDefinition)
		throws PortalException {

		// Resources

		resourceLocalService.deleteResource(
			commerceBOMDefinition, ResourceConstants.SCOPE_INDIVIDUAL);

		// Commerce BOM definition

		return commerceBOMDefinitionPersistence.remove(commerceBOMDefinition);
	}

	@Override
	public CommerceBOMDefinition deleteCommerceBOMDefinition(
			long commerceBOMDefinitionId)
		throws PortalException {

		CommerceBOMDefinition commerceBOMDefinition =
			commerceBOMDefinitionPersistence.findByPrimaryKey(
				commerceBOMDefinitionId);

		return commerceBOMDefinitionLocalService.deleteCommerceBOMDefinition(
			commerceBOMDefinition);
	}

	@Override
	public List<CommerceBOMDefinition> getCommerceBOMDefinitions(
		long commerceBOMFolderId) {

		return commerceBOMDefinitionPersistence.findBycommerceBOMFolderId(
			commerceBOMFolderId);
	}

	@Override
	public CommerceBOMDefinition updateCommerceBOMDefinition(
			long commerceBOMDefinitionId, String name, long imageId,
			String friendlyUrl, long commerceBOMFolderId)
		throws PortalException {

		CommerceBOMDefinition commerceBOMDefinition =
			commerceBOMDefinitionLocalService.getCommerceBOMDefinition(
				commerceBOMDefinitionId);

		commerceBOMDefinition.setName(name);
		commerceBOMDefinition.setImageId(imageId);
		commerceBOMDefinition.setFriendlyUrl(friendlyUrl);
		commerceBOMDefinition.setCommerceBOMFolderId(commerceBOMFolderId);

		return commerceBOMDefinitionPersistence.update(commerceBOMDefinition);
	}

}