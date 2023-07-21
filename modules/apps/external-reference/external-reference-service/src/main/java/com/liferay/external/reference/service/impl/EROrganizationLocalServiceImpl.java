/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.external.reference.service.impl;

import com.liferay.external.reference.service.base.EROrganizationLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.users.admin.kernel.file.uploads.UserFileUploadsSettings;

/**
 * @author Dylan Rebelak
 */
public class EROrganizationLocalServiceImpl
	extends EROrganizationLocalServiceBaseImpl {

	@Override
	public Organization addOrUpdateOrganization(
			String externalReferenceCode, long userId,
			long parentOrganizationId, String name, String type, long regionId,
			long countryId, long statusId, String comments, boolean site,
			boolean logo, byte[] logoBytes, ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		Organization organization =
			organizationLocalService.fetchOrganizationByReferenceCode(
				user.getCompanyId(), externalReferenceCode);

		if (organization == null) {
			organization = organizationLocalService.addOrganization(
				userId, parentOrganizationId, name, type, regionId, countryId,
				statusId, comments, site, serviceContext);

			organization.setExternalReferenceCode(externalReferenceCode);

			PortalUtil.updateImageId(
				organization, logo, logoBytes, "logoId",
				_userFileUploadsSettings.getImageMaxSize(),
				_userFileUploadsSettings.getImageMaxHeight(),
				_userFileUploadsSettings.getImageMaxWidth());

			organization = organizationLocalService.updateOrganization(
				organization);
		}
		else {
			organizationLocalService.updateOrganization(
				user.getCompanyId(), organization.getOrganizationId(),
				parentOrganizationId, name, type, regionId, countryId, statusId,
				comments, logo, logoBytes, site, serviceContext);
		}

		return organization;
	}

	@ServiceReference(type = UserFileUploadsSettings.class)
	private UserFileUploadsSettings _userFileUploadsSettings;

}