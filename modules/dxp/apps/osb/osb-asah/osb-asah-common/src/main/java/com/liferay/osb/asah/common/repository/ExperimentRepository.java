/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.model.ExperimentStatus;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcos Martins
 */
public interface ExperimentRepository
	extends CustomExperimentRepository, Repository<Experiment, Long> {

	@CacheEvict(allEntries = true)
	@Modifying
	@Query("DELETE FROM Experiment WHERE channelId IN (:channelIds)")
	public void deleteByChannelIdIn(@Param("channelIds") Set<Long> channelIds);

	@Cacheable
	public List<Experiment> findByExperimentStatus(
		ExperimentStatus experimentStatus);

}