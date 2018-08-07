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

package com.liferay.commerce.data.integration.apio.internal.util;

import com.liferay.commerce.organization.constants.CommerceOrganizationConstants;
import com.liferay.commerce.organization.service.CommerceOrganizationService;
import com.liferay.external.reference.service.EROrganizationLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true, service = CommerceAccountHelper.class)
public class CommerceAccountHelper {

	public void deleteOrganization(long organizationId) throws PortalException {
		_removeAllMembers(organizationId);
		_commerceOrganizationService.deleteOrganization(organizationId);
	}

	public Organization upsert(
			String externalReferenceCode, long parentOrganizationId,
			String name, long regionId, long countryId, List<Long> userIds,
			User currentUser)
		throws PortalException {

		ServiceContext serviceContext = _getServiceContext(currentUser);

		Organization organization =
			_erOrganizationLocalService.addOrUpdateOrganization(
				externalReferenceCode, serviceContext.getUserId(),
				parentOrganizationId, name,
				CommerceOrganizationConstants.TYPE_ACCOUNT, regionId, countryId,
				ListTypeConstants.ORGANIZATION_STATUS_DEFAULT, StringPool.BLANK,
				false, false, null, serviceContext);

		_addMembers(organization, userIds);

		return organization;
	}

	private void _addMembers(Organization organization, List<Long> userIds) {
		if (userIds != null) {
			_removeAllMembers(organization.getOrganizationId());

			for (Long userId : userIds) {
				try {
					User userMember = _userLocalService.getUser(userId);

					if (userMember != null) {
						_userLocalService.addOrganizationUser(
							organization.getOrganizationId(), userId);
					}
				}
				catch (PortalException pe) {
					_log.error("Error on add member", pe);
				}
			}
		}
	}

	private ServiceContext _getServiceContext(User currentUser) throws PortalException {
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(currentUser.getCompanyId());
		serviceContext.setTimeZone(currentUser.getTimeZone());
		serviceContext.setUserId(currentUser.getUserId());

		return serviceContext;
	}

	private void _removeAllMembers(long organizationId) {
		_userLocalService.clearOrganizationUsers(organizationId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceAccountHelper.class);

	@Reference
	private CommerceOrganizationService _commerceOrganizationService;

	@Reference
	private EROrganizationLocalService _erOrganizationLocalService;

	@Reference
	private UserLocalService _userLocalService;

}