/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQOrganizationRepository {

	public long count();

	public long countByDataSourceIdsAndName(
		List<Long> dataSourceIds, @Nullable String name);

	public long countByName(@Nullable String name);

	public long countOrganizationFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString);

	public void deleteById(String id);

	public List<BQOrganization> findAll();

	public Optional<BQOrganization> findByDataSourceIdAndOrganizationId(
		Long dataSourceId, Long organizationId);

	public List<BQOrganization> findByDataSourceIdAndOrganizationIdIn(
		Long dataSourceId, Collection<Long> organizationIds);

	public Optional<BQOrganization> findById(String bqOrganizationId);

	public List<BQOrganization> findByIdIn(Collection<String> ids);

	@Cacheable
	public List<BQOrganization> findByName(
		@Nullable String name, Pageable pageable);

	public BQOrganization insert(BQOrganization bqOrganization);

	@Cacheable
	public List<BQOrganization> searchBQOrganizations(
		FilterHelper filterHelper, Pageable pageable);

	public List<BQOrganization> searchByDataSourceIdsAndName(
		List<Long> dataSourceIds, @Nullable String name, Pageable pageable);

	public List<String> searchOrganizationFieldValuesCustom(
		@Nullable Long channelId, String fieldName,
		@Nullable String filterString, Pageable pageable);

}