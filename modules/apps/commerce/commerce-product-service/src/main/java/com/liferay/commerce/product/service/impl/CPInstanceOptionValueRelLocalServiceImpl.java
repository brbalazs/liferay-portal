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

package com.liferay.commerce.product.service.impl;

import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstanceOptionValueRel;
import com.liferay.commerce.product.service.base.CPInstanceOptionValueRelLocalServiceBaseImpl;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The implementation of the cp instance option value rel local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.commerce.product.service.CPInstanceOptionValueRelLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Marco Leo
 * @see CPInstanceOptionValueRelLocalServiceBaseImpl
 */
public class CPInstanceOptionValueRelLocalServiceImpl
	extends CPInstanceOptionValueRelLocalServiceBaseImpl {

	@Override
	public CPInstanceOptionValueRel addCPInstanceOptionValueRel(
			long groupId, long companyId, long userId,
			long cpDefinitionOptionRelId, long cpDefinitionOptionValueRelId,
			long cpInstanceId)
		throws PortalException {

		long cpInstanceOptionValueRelId = counterLocalService.increment();

		CPInstanceOptionValueRel cpInstanceOptionValueRel =
			cpInstanceOptionValueRelPersistence.create(
				cpInstanceOptionValueRelId);

		cpInstanceOptionValueRel.setGroupId(groupId);
		cpInstanceOptionValueRel.setCompanyId(companyId);
		cpInstanceOptionValueRel.setUserId(userId);

		User user = userLocalService.getUser(userId);

		cpInstanceOptionValueRel.setUserName(user.getFullName());

		Date createDate = new Date();

		cpInstanceOptionValueRel.setCreateDate(createDate);
		cpInstanceOptionValueRel.setModifiedDate(createDate);

		cpInstanceOptionValueRel.setCPDefinitionOptionRelId(
			cpDefinitionOptionRelId);
		cpInstanceOptionValueRel.setCPDefinitionOptionValueRelId(
			cpDefinitionOptionValueRelId);
		cpInstanceOptionValueRel.setCPInstanceId(cpInstanceId);

		return cpInstanceOptionValueRelPersistence.update(
			cpInstanceOptionValueRel);
	}

	@Override
	public boolean hasCPInstanceOptionValueRel(long cpInstanceId) {
		int countByCPInstanceId =
			cpInstanceOptionValueRelPersistence.countByCPInstanceId(
				cpInstanceId);

		if (countByCPInstanceId > 0) {
			return true;
		}

		return false;
	}

	@Override
	public boolean matchesCPInstanceOptionValueRels(
		long cpInstanceId,
		List<CPInstanceOptionValueRel> cpInstanceOptionValueRels) {

		List<CPInstanceOptionValueRel> cpInstanceCPInstanceOptionValueRels =
			cpInstanceOptionValueRelPersistence.findByCPInstanceId(
				cpInstanceId);

		if (cpInstanceOptionValueRels.size() !=
				cpInstanceCPInstanceOptionValueRels.size()) {

			return false;
		}

		int matchCount = 0;

		for (CPInstanceOptionValueRel cpInstanceOptionValueRel :
				cpInstanceOptionValueRels) {

			for (CPInstanceOptionValueRel currCPInstanceOptionValueRel :
					cpInstanceCPInstanceOptionValueRels) {

				if ((cpInstanceOptionValueRel.getCPDefinitionOptionRelId() ==
						currCPInstanceOptionValueRel.
							getCPDefinitionOptionRelId()) &&
					(cpInstanceOptionValueRel.getCPDefinitionOptionRelId() ==
						currCPInstanceOptionValueRel.
							getCPDefinitionOptionRelId())) {

					matchCount++;
				}
			}
		}

		if (cpInstanceOptionValueRels.size() == matchCount) {
			return true;
		}

		return false;
	}

	@Override
	public void updateCPInstanceOptionValueRels(
			long groupId, long companyId, long userId, long cpDefinitionId,
			long cpInstanceId, String json)
		throws PortalException {

		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpDefinitionOptionRelsMap =
				_cpInstanceHelper.getCPDefinitionOptionRelsMap(
					cpDefinitionId, json);

		for (Map.Entry<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels :
					cpDefinitionOptionRelsMap.entrySet()) {

			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
				cpDefinitionOptionRelCPDefinitionOptionValueRels.getValue();

			for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
					cpDefinitionOptionValueRels) {

				cpInstanceOptionValueRelLocalService.
					addCPInstanceOptionValueRel(
						groupId, companyId, userId,
						cpDefinitionOptionValueRel.getCPDefinitionOptionRelId(),
						cpDefinitionOptionValueRel.
							getCPDefinitionOptionValueRelId(),
						cpInstanceId);
			}
		}
	}

	@ServiceReference(type = CPInstanceHelper.class)
	private CPInstanceHelper _cpInstanceHelper;

}