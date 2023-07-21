/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.service.impl;

import com.liferay.osb.faro.model.FaroPreferences;
import com.liferay.osb.faro.service.base.FaroPreferencesLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

/**
 * @author Matthew Kong
 */
public class FaroPreferencesLocalServiceImpl
	extends FaroPreferencesLocalServiceBaseImpl {

	@Override
	public FaroPreferences deleteFaroPreferences(long groupId, long ownerId) {
		FaroPreferences faroPreferences = faroPreferencesPersistence.fetchByG_O(
			groupId, ownerId);

		if (faroPreferences != null) {
			faroPreferencesPersistence.remove(faroPreferences);
		}

		return faroPreferences;
	}

	@Override
	public void deleteFaroPreferencesByGroupId(long groupId) {
		faroPreferencesPersistence.removeByGroupId(groupId);
	}

	@Override
	public FaroPreferences fetchFaroPreferences(long groupId, long ownerId) {
		return faroPreferencesPersistence.fetchByG_O(groupId, ownerId);
	}

	@Override
	public FaroPreferences savePreferences(
			long userId, long groupId, long ownerId, String preferences)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		FaroPreferences faroPreferences = faroPreferencesPersistence.fetchByG_O(
			groupId, ownerId);

		long now = System.currentTimeMillis();

		if (faroPreferences == null) {
			faroPreferences = faroPreferencesPersistence.create(
				counterLocalService.increment());

			faroPreferences.setGroupId(groupId);
			faroPreferences.setCreateTime(now);
			faroPreferences.setOwnerId(ownerId);
		}

		faroPreferences.setUserId(userId);
		faroPreferences.setUserName(user.getFullName());
		faroPreferences.setModifiedTime(now);
		faroPreferences.setPreferences(preferences);

		return updateFaroPreferences(faroPreferences);
	}

}