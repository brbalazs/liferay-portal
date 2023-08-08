/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.repository.BQOrganizationRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Rachael Koestartyo
 */
@Import(JDBCTestConfiguration.class)
public class BQOrganizationRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		BQOrganization bqOrganization = new BQOrganization();

		bqOrganization.setCreateDate(new Date());

		bqOrganization.setDataSourceId(1L);
		bqOrganization.setId("12345");
		bqOrganization.setModifiedDate(new Date());
		bqOrganization.setName("Organization 1");
		bqOrganization.setOrganizationId(123L);
		bqOrganization.setParentOrganizationId(0L);

		_bqOrganizationRepository.insert(bqOrganization);
	}

	@AfterEach
	public void tearDown() {
		_bqOrganizationRepository.deleteById("12345");
	}

	@Test
	public void testCountByName() {
		Assertions.assertEquals(
			1, _bqOrganizationRepository.countByName("Org"));
	}

	@Test
	public void testCountyByDataSourceIdsAndName() {
		Assertions.assertEquals(
			1,
			_bqOrganizationRepository.countByDataSourceIdsAndName(
				Collections.singletonList(1L), "Org"));
	}

	@Test
	public void testFindByDataSourceIdAndOrganizationId() {
		Assertions.assertNotNull(
			_bqOrganizationRepository.findByDataSourceIdAndOrganizationId(
				1L, 123L));
	}

	@Test
	public void testFindByDataSourceIdAndOrganizationIdIn() {
		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.findByDataSourceIdAndOrganizationIdIn(
				1L, Collections.singleton(123L));

		Assertions.assertEquals(1, bqOrganizations.size());
	}

	@Test
	public void testFindByName() {
		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.findByName("Org", PageRequest.of(0, 10));

		Assertions.assertEquals(1, bqOrganizations.size());
	}

	@Test
	public void testSearchByDataSourceIdsAndName() {
		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.searchByDataSourceIdsAndName(
				Collections.singletonList(1L), "Org", PageRequest.of(0, 10));

		Assertions.assertEquals(1, bqOrganizations.size());
	}

	@Autowired
	private BQOrganizationRepository _bqOrganizationRepository;

}