/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQFieldMapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Robson Pastor
 */
public interface CustomBQFieldMappingRepository {

	public long count();

	public long countByFilterString(String filterString);

	public long countIndividualBQFieldMappings(@Nullable String displayName);

	public Optional<BQFieldMapping> findByDisplayNameAndFieldType(
		String displayName, String fieldType);

	public Optional<BQFieldMapping> findByFieldName(String fieldName);

	public List<BQFieldMapping> findByFieldNameIn(
		Collection<String> fieldNames);

	public List<BQFieldMapping> searchByFilterString(
		String filterString, Pageable pageable);

	public List<BQFieldMapping> searchIndividualBQFieldMappings(
		@Nullable String displayName, Pageable pageable);

}