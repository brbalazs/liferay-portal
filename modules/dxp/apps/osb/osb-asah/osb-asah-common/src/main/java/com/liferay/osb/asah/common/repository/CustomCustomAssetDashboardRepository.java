/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.CustomAssetDashboard;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author Ivica Cardic
 */
public interface CustomCustomAssetDashboardRepository {

	@Cacheable
	public long countCustomAssetDashboards(Long channelId, String keywords);

	@Cacheable
	public List<CustomAssetDashboard> searchCustomAssetDashboards(
		Long channelId, String keywords, Pageable pageable);

}