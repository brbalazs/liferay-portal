/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Channel;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author Inácio Nery
 */
public interface ChannelRepository extends Repository<Channel, Long> {

	@Cacheable
	public long countByNameContainingIgnoreCase(String name);

	@Cacheable
	public long countByNameContainingIgnoreCaseAndStateNot(
		String name, String state);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByIdIn(@Param("ids") Set<Long> ids);

	@Cacheable
	public boolean existsByIdNotAndName(Long id, String name);

	@Cacheable
	public boolean existsByName(String name);

	@Cacheable
	public List<Channel> findByDataSourceId(
		@Param("dataSourceId") Long dataSourceId);

	@Cacheable
	public List<Channel> findByDataSourceIdAndDefaultChannel(
		@Param("dataSourceId") Long dataSourceId,
		@Param("defaultChannel") Boolean defaultChannel);

	@Cacheable
	public List<Channel> findByDataSourceIdAndGroupIds(
		@Param("dataSourceId") Long dataSourceId,
		@Param("groupsIds") Set<Long> groupsIds);

	@Cacheable
	public List<Channel> findByNameContainingIgnoreCase(
		String name, Pageable pageable);

	@Cacheable
	public List<Channel> findByNameContainingIgnoreCaseAndStateNot(
		String name, Pageable pageable, String state);

}