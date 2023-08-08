/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DataSource;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * @author Inácio Nery
 */
public interface DataSourceRepository
	extends CustomDataSourceRepository, Repository<DataSource, Long> {

	@Cacheable
	public boolean existsByFaroBackendSecuritySignature(
		String faroBackendSecuritySignature);

	@Cacheable
	public boolean existsByIdNotAndName(Long id, String name);

	@Cacheable
	public boolean existsByName(String name);

	@Cacheable
	public boolean existsByProviderType(
		@Param("providerType") String providerType);

	@Cacheable
	public List<DataSource> findByCredentialType(
		String credentialType, Pageable pageable);

	@Cacheable
	public List<DataSource> findByCredentialTypeAndProviderType(
		String credentialType, String providerType, Pageable pageable);

	@Cacheable
	public List<DataSource> findByProviderType(String providerType);

	@Cacheable
	public List<DataSource> findByProviderType(
		String providerType, Pageable pageable);

	@Cacheable
	public List<DataSource> findByProviderTypeAndStatus(
		String providerType, String status);

}