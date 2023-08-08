/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
public abstract class BaseRepositoryTestCase<T extends Persistable<ID>, ID>
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		pagingAndSortingRepository.deleteAll();
	}

	@Test
	public void testCount() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels.size(), pagingAndSortingRepository.count());
	}

	@Test
	public void testDelete() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		pagingAndSortingRepository.delete(entityModels.get(0));

		Assertions.assertEquals(
			entityModels.size() - 1, pagingAndSortingRepository.count());
	}

	@Test
	public void testDeleteAll1() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		pagingAndSortingRepository.deleteAll();

		Assertions.assertEquals(0, pagingAndSortingRepository.count());
	}

	@Test
	public void testDeleteAll2() {
		PagingAndSortingRepository<T, ?> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		pagingAndSortingRepository.deleteAll(entityModels);

		Assertions.assertEquals(0, pagingAndSortingRepository.count());
	}

	@Test
	public void testDeleteById() {
		T model = entityModels.get(0);

		ID id = model.getId();

		Assertions.assertNotNull(id);

		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		pagingAndSortingRepository.deleteById(id);

		Assertions.assertEquals(
			entityModels.size() - 1, pagingAndSortingRepository.count());
	}

	@Test
	public void testExistsById() {
		T model = entityModels.get(0);

		ID id = model.getId();

		Assertions.assertNotNull(id);

		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertTrue(pagingAndSortingRepository.existsById(id));
	}

	@Test
	public void testFindAll1() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels, pagingAndSortingRepository.findAll());
	}

	@Test
	public void testFindAll2() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Page<T> page = pagingAndSortingRepository.findAll(
			PageRequest.of(0, entityModels.size(), Sort.by("id")));

		Assertions.assertEquals(entityModels, page.getContent());
	}

	@Test
	public void testFindAll3() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels, pagingAndSortingRepository.findAll(Sort.by("id")));
	}

	@Test
	public void testFindAllById() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels,
			pagingAndSortingRepository.findAllById(
				ListUtil.map(entityModels, T::getId)));
	}

	@Test
	public void testFindById() {
		T model = entityModels.get(0);

		ID id = model.getId();

		Assertions.assertNotNull(id);

		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Optional<T> modelOptional = pagingAndSortingRepository.findById(id);

		Assertions.assertTrue(modelOptional.isPresent());
	}

	@Test
	public void testSave() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels.get(0),
			pagingAndSortingRepository.save(entityModels.get(0)));
	}

	@Test
	public void testSaveAll() {
		PagingAndSortingRepository<T, ID> pagingAndSortingRepository =
			getPagingAndSortingRepository();

		Assertions.assertEquals(
			entityModels, pagingAndSortingRepository.saveAll(entityModels));
	}

	protected abstract PagingAndSortingRepository<T, ID>
		getPagingAndSortingRepository();

	protected void setUpRepository(T... entityModels) {
		PagingAndSortingRepository<T, ID> repository =
			getPagingAndSortingRepository();

		this.entityModels = IterableUtils.toList(
			repository.saveAll(Arrays.asList(entityModels)));
	}

	protected List<T> entityModels;

}