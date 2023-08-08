/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BlockedKeyword;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author André Miranda
 */
public interface BlockedKeywordRepository
	extends Repository<BlockedKeyword, Long> {

	@Cacheable
	public long countByKeywordContainingIgnoreCase(String keyword);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByIdIn(@Param("ids") Set<Long> ids);

	@Cacheable
	public List<BlockedKeyword> findByKeywordContainingIgnoreCase(
		String keyword, Pageable pageable);

	@Cacheable
	public List<BlockedKeyword> findByKeywordIn(Set<String> keywords);

}