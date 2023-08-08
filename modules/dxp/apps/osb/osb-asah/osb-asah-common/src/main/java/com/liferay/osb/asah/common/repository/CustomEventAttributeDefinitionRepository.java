/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomEventAttributeDefinitionRepository {

	@Cacheable
	public long countEventAttributeDefinitions(
		@Nullable Long eventDefinitionId, @Nullable String keyword,
		@Nullable EventAttributeDefinition.Type type);

	@Cacheable
	public List<EventAttributeDefinition> searchEventAttributeDefinitions(
		@Nullable Long eventDefinitionId, @Nullable String keyword,
		Pageable pageable, @Nullable EventAttributeDefinition.Type type);

}