/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Job;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author Marcellus Tavares
 */
public interface JobRepository extends Repository<Job, Long> {

	@Cacheable
	public long countByNameContainingIgnoreCase(String name);

	@Cacheable
	public List<Job> findByNameContainingIgnoreCase(
		String name, Pageable pageable);

	@Cacheable
	public Optional<Job> findFirstByName(String name);

}