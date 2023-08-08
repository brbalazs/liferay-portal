/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.ItemRecommendation;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcellus Tavares
 */
public interface ItemRecommendationRepository
	extends Repository<ItemRecommendation, String> {

	@Cacheable
	public long countByJobId(Long jobId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByJobId(@Param("jobId") Long jobId);

	@Cacheable
	public List<ItemRecommendation> findByJobId(Long jobId, Pageable pageable);

}