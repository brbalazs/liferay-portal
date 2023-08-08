/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
@NoRepositoryBean
public interface Repository<T, ID> extends PagingAndSortingRepository<T, ID> {

	@Cacheable
	@Override
	public long count();

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public void delete(T entity);

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public void deleteAll();

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public void deleteAll(Iterable<? extends T> entities);

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public void deleteById(ID id);

	@Cacheable
	@Override
	public boolean existsById(ID id);

	@Cacheable
	@Override
	public default Iterable<T> findAll() {
		return findAll(Sort.by("id"));
	}

	@Cacheable
	@Override
	public Page<T> findAll(Pageable pageable);

	@Cacheable
	@Override
	public Iterable<T> findAll(Sort sort);

	@Cacheable
	@Override
	public Iterable<T> findAllById(Iterable<ID> ids);

	@Cacheable
	@Override
	public Optional<T> findById(ID id);

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public <S extends T> S save(S entity);

	@CacheEvict(allEntries = true)
	@Modifying
	@Override
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities);

}