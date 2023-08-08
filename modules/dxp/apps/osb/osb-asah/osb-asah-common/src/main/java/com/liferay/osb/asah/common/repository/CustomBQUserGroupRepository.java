/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQUserGroup;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author Marcos Martins
 */
@Repository
public interface CustomBQUserGroupRepository {

	public long count();

	public long countByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords);

	public void deleteById(String id);

	public Optional<BQUserGroup> findById(String id);

	public List<BQUserGroup> findByIdIn(Collection<String> ids);

	public BQUserGroup insert(BQUserGroup bqUserGroup);

	public List<BQUserGroup> searchByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords, Pageable pageable);

}