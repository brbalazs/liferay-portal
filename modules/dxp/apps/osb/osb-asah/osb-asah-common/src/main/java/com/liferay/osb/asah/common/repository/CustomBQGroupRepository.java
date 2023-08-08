/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQGroup;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Marcos Martins
 */
@Repository
public interface CustomBQGroupRepository {

	public long count();

	public long countByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, @Nullable String keywords);

	public void deleteById(String id);

	public List<BQGroup> findByIdIn(Collection<String> ids);

	public BQGroup insert(BQGroup bqGroup);

	public List<BQGroup> searchByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, @Nullable String keywords, Pageable pageable);

}