/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.EventAnalysis;

import java.util.Optional;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Rachael Koestartyo
 */
public interface EventAnalysisRepository
	extends CustomEventAnalysisRepository, Repository<EventAnalysis, Long> {

	@CacheEvict(allEntries = true)
	@Modifying
	@Query("DELETE FROM EventAnalysis WHERE channelId IN (:channelIds)")
	public void deleteByChannelIdIn(@Param("channelIds") Set<Long> channelIds);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByIdIn(@Param("ids") Set<Long> ids);

	@Cacheable
	public Optional<EventAnalysis> findByChannelIdAndNameIgnoreCase(
		Long channelId, String name);

}