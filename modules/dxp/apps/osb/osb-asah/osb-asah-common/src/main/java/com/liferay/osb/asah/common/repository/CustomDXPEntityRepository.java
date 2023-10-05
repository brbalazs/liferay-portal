/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DXPEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomDXPEntityRepository {

	public long count();

	@Cacheable
	public long countByDataSourceIdsAndKeywordsAndType(
		List<Long> dataSourceIds, @Nullable String keywords,
		DXPEntity.Type type);

	public long countByModifiedDateBetweenAndType(
		Long dataSourceId, @Nullable Date modifiedDate1, Date modifiedDate2,
		DXPEntity.Type type);

	public void delete(DXPEntity dxpEntity);

	public void deleteAll(Iterable<? extends DXPEntity> dxpEntities);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByFieldNameAndFieldValueAndType(
		String fieldName, Object fieldValue, DXPEntity.Type type);

	public void deleteById(Long id);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByType(DXPEntity.Type type);

	public boolean existsById(Long id);

	public Iterable<DXPEntity> findAll();

	public Page<DXPEntity> findAll(Pageable pageable);

	public Iterable<DXPEntity> findAll(Sort sort);

	public Iterable<DXPEntity> findAllById(Iterable<Long> ids);

	@Cacheable
	public List<DXPEntity> findByAfterAndFieldsAndType(
		@Nullable Long after, Map<String, Object> fields, int size,
		DXPEntity.Type type);

	@Cacheable
	public List<DXPEntity> findByFieldsAndType(
		Map<String, Object> fields, DXPEntity.Type type);

	@Cacheable
	public List<DXPEntity> findByMembershipClassNameAndMembershipId(
		String memebershipClassName, Long membershipId);

	public List<DXPEntity> findByModifiedDateBetweenAndType(
		Long dataSourceId, @Nullable Date modifiedDate1, Date modifiedDate2,
		DXPEntity.Type type, Pageable pageable);

	@Cacheable
	public List<DXPEntity> searchByDataSourceIdsAndKeywordsAndType(
		List<Long> dataSourceIds, @Nullable String keywords,
		DXPEntity.Type type, Pageable pageable);

}