/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.EventDefinition;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author Leslie Wong
 */
public interface EventDefinitionRepository
	extends CustomEventDefinitionRepository, Repository<EventDefinition, Long> {

	@Cacheable
	public Optional<EventDefinition> findByDisplayNameIgnoreCase(
		String displayName);

	@Cacheable
	public Optional<EventDefinition> findByName(String name);

	@CacheEvict(allEntries = true)
	@Modifying
	public void updateEventDefinitions(
		@Param("ids") List<Long> eventDefinitionIds,
		@Param("hidden") Boolean hidden);

}