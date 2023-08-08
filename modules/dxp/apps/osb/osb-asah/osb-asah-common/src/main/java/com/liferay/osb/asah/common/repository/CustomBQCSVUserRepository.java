/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQCSVUser;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcellus Tavares
 */
public interface CustomBQCSVUserRepository {

	public long count();

	@Cacheable
	public long countByDataSourceId(Long dataSourceId);

	public void deleteAll();

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByDataSourceId(@Param("dataSourceId") Long dataSourceId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByDataSourceIdAndDataSourceUserPKIn(
		@Param("dataSourceId") Long dataSourceId,
		@Param("dataSourceUserPKs") List<String> dataSourceUserPKs);

	public List<BQCSVUser> findAll();

	@Cacheable
	public List<BQCSVUser> findByDataSourceId(
		Long dataSourceId, Pageable pageable);

	@CacheEvict(allEntries = true)
	@Modifying
	public void insert(BQCSVUser bqCSVUser);

	@CacheEvict(allEntries = true)
	@Modifying
	public void insertAll(List<BQCSVUser> bqCSVUsers);

}