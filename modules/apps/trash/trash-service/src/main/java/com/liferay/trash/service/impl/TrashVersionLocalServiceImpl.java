/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.trash.service.impl;

import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.trash.model.TrashVersion;
import com.liferay.trash.service.base.TrashVersionLocalServiceBaseImpl;

import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class TrashVersionLocalServiceImpl
	extends TrashVersionLocalServiceBaseImpl {

	@Override
	public TrashVersion addTrashVersion(
		long trashEntryId, String className, long classPK, int status,
		UnicodeProperties typeSettingsProperties) {

		long versionId = counterLocalService.increment();

		TrashVersion trashVersion = trashVersionPersistence.create(versionId);

		trashVersion.setEntryId(trashEntryId);
		trashVersion.setClassName(className);
		trashVersion.setClassPK(classPK);

		if (typeSettingsProperties != null) {
			trashVersion.setTypeSettingsProperties(typeSettingsProperties);
		}

		trashVersion.setStatus(status);

		return trashVersionPersistence.update(trashVersion);
	}

	@Override
	public TrashVersion deleteTrashVersion(String className, long classPK) {
		long classNameId = classNameLocalService.getClassNameId(className);

		TrashVersion trashVersion = trashVersionPersistence.fetchByC_C(
			classNameId, classPK);

		if (trashVersion != null) {
			return deleteTrashVersion(trashVersion);
		}

		return null;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #fetchVersion(String, long)}
	 */
	@Deprecated
	@Override
	public TrashVersion fetchVersion(
		long entryId, String className, long classPK) {

		return fetchVersion(className, classPK);
	}

	@Override
	public TrashVersion fetchVersion(String className, long classPK) {
		long classNameId = classNameLocalService.getClassNameId(className);

		return trashVersionPersistence.fetchByC_C(classNameId, classPK);
	}

	@Override
	public List<TrashVersion> getVersions(long entryId) {
		return trashVersionPersistence.findByEntryId(entryId);
	}

	@Override
	public List<TrashVersion> getVersions(long entryId, String className) {
		if (Validator.isNull(className)) {
			return trashVersionPersistence.findByEntryId(entryId);
		}

		long classNameId = classNameLocalService.getClassNameId(className);

		return trashVersionPersistence.findByE_C(entryId, classNameId);
	}

}