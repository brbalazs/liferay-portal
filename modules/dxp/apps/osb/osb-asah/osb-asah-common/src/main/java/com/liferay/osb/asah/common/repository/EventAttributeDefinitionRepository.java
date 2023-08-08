/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;

/**
 * @author Leslie Wong
 */
public interface EventAttributeDefinitionRepository
	extends CustomEventAttributeDefinitionRepository,
			Repository<EventAttributeDefinition, Long> {

	@Cacheable
	public Optional<EventAttributeDefinition> findByDisplayNameIgnoreCase(
		String displayName);

	@Cacheable
	public List<EventAttributeDefinition> findByEventDefinitionId(
		@Param("eventDefinitionId") Long eventDefinitionId);

	@Cacheable
	public Optional<EventAttributeDefinition> findByName(String name);

	@Cacheable
	public List<EventAttributeDefinition> findByType(
		EventAttributeDefinition.Type type);

}