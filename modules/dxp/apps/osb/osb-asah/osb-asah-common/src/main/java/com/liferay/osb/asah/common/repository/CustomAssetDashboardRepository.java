/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.CustomAssetDashboard;

import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author André Miranda
 */
public interface CustomAssetDashboardRepository
	extends CustomCustomAssetDashboardRepository,
			Repository<CustomAssetDashboard, String> {

	@CacheEvict(allEntries = true)
	@Modifying
	@Query("DELETE FROM CustomAssetDashboard WHERE channelId IN (:channelIds)")
	public void deleteByChannelIdIn(@Param("channelIds") Set<Long> channelIds);

}