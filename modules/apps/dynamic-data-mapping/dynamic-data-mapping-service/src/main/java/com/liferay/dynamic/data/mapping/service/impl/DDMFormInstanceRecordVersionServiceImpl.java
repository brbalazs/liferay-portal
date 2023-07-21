/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.service.impl;

import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.base.DDMFormInstanceRecordVersionServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Leonardo Barros
 */
public class DDMFormInstanceRecordVersionServiceImpl
	extends DDMFormInstanceRecordVersionServiceBaseImpl {

	@Override
	public DDMFormInstanceRecordVersion fetchLatestFormInstanceRecordVersion(
			long userId, long formInstanceId, String formInstanceVersion,
			int status)
		throws PortalException {

		DDMFormInstanceRecordVersion latestFormInstanceRecordVersion =
			ddmFormInstanceRecordVersionLocalService.
				fetchLatestFormInstanceRecordVersion(
					userId, formInstanceId, formInstanceVersion, status);

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(),
			latestFormInstanceRecordVersion.getFormInstanceRecordId(),
			ActionKeys.VIEW);

		return latestFormInstanceRecordVersion;
	}

	@Override
	public DDMFormInstanceRecordVersion getFormInstanceRecordVersion(
			long ddmFormInstanceRecordVersionId)
		throws PortalException {

		DDMFormInstanceRecordVersion ddmFormInstanceRecordVersion =
			ddmFormInstanceRecordVersionLocalService.
				getFormInstanceRecordVersion(ddmFormInstanceRecordVersionId);

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(),
			ddmFormInstanceRecordVersion.getFormInstanceRecordId(),
			ActionKeys.VIEW);

		return ddmFormInstanceRecordVersion;
	}

	@Override
	public DDMFormInstanceRecordVersion getFormInstanceRecordVersion(
			long ddmFormInstanceRecordId, String version)
		throws PortalException {

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(), ddmFormInstanceRecordId, ActionKeys.VIEW);

		return ddmFormInstanceRecordVersionPersistence.findByF_V(
			ddmFormInstanceRecordId, version);
	}

	@Override
	public List<DDMFormInstanceRecordVersion> getFormInstanceRecordVersions(
			long ddmFormInstanceRecordId)
		throws PortalException {

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(), ddmFormInstanceRecordId, ActionKeys.VIEW);

		return ddmFormInstanceRecordVersionPersistence.
			findByFormInstanceRecordId(ddmFormInstanceRecordId);
	}

	@Override
	public List<DDMFormInstanceRecordVersion> getFormInstanceRecordVersions(
			long ddmFormInstanceRecordId, int start, int end,
			OrderByComparator<DDMFormInstanceRecordVersion> orderByComparator)
		throws PortalException {

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(), ddmFormInstanceRecordId, ActionKeys.VIEW);

		return ddmFormInstanceRecordVersionPersistence.
			findByFormInstanceRecordId(
				ddmFormInstanceRecordId, start, end, orderByComparator);
	}

	@Override
	public int getFormInstanceRecordVersionsCount(long ddmFormInstanceRecordId)
		throws PortalException {

		_ddmFormInstanceRecordModelResourcePermission.check(
			getPermissionChecker(), ddmFormInstanceRecordId, ActionKeys.VIEW);

		return ddmFormInstanceRecordVersionPersistence.
			countByFormInstanceRecordId(ddmFormInstanceRecordId);
	}

	private static volatile ModelResourcePermission<DDMFormInstanceRecord>
		_ddmFormInstanceRecordModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				DDMFormInstanceRecordServiceImpl.class,
				"_ddmFormInstanceRecordModelResourcePermission",
				DDMFormInstanceRecord.class);

}