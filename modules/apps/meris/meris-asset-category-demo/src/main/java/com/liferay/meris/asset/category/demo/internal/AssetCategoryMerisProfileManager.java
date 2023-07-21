/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris.asset.category.demo.internal;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.meris.MerisProfileManager;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(immediate = true, service = MerisProfileManager.class)
public class AssetCategoryMerisProfileManager
	implements MerisProfileManager<AssetCategoryMerisProfile> {

	@Override
	public AssetCategoryMerisProfile getMerisProfile(String merisProfileId) {
		long userId = GetterUtil.getLong(merisProfileId);

		User user = _userLocalService.fetchUser(userId);

		if (user != null) {
			long[] assetCategoryIds = _assetCategoryLocalService.getCategoryIds(
				user.getModelClassName(), userId);

			return new AssetCategoryMerisProfile(user, assetCategoryIds);
		}

		return null;
	}

	@Override
	public List<AssetCategoryMerisProfile> getMerisProfiles(
		int start, int end, Comparator<AssetCategoryMerisProfile> comparator) {

		List<User> users = _userLocalService.getUsers(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Stream<User> stream = users.stream();

		return stream.map(
			user -> getMerisProfile(String.valueOf(user.getUserId()))
		).collect(
			Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					list.sort(comparator);

					return ListUtil.subList(list, start, end);
				})
		);
	}

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private UserLocalService _userLocalService;

}