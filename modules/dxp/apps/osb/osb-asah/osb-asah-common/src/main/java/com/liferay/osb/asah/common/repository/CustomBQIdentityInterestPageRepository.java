/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQIdentityInterestPage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.lang.Nullable;

/**
 * @author Leslie Wong
 */
public interface CustomBQIdentityInterestPageRepository {

	public long countActivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType);

	public long countInactivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType);

	@Modifying
	public void deleteAll();

	public List<Map<String, Object>> getActivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType, Pageable pageable);

	public List<BQIdentityInterestPage> getBQIdentityInterestPages(
		String keyword);

	public List<Map<String, Object>> getInactivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType, Pageable pageable);

	public void insertAll(List<BQIdentityInterestPage> bqIdentityInterestPages);

}