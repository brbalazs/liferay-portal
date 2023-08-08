/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class BQFieldMappingRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testCount() {
		Assertions.assertEquals(9, _bqFieldMappingRepository.count());
	}

	@Test
	public void testCountBQFieldMappings() {
		Assertions.assertEquals(
			0,
			_bqFieldMappingRepository.countByFilterString(
				"(context eq 'custom')"));
		Assertions.assertEquals(
			9,
			_bqFieldMappingRepository.countByFilterString(
				"(context eq 'demographics')"));
	}

	@Test
	public void testFindByDisplayName() {
		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByDisplayNameAndFieldType(
				"additionalName", "text");

		Assertions.assertNotNull(bqFieldMappingOptional.orElse(null));

		bqFieldMappingOptional =
			_bqFieldMappingRepository.findByDisplayNameAndFieldType(
				"name", "text");

		Assertions.assertNull(bqFieldMappingOptional.orElse(null));
	}

	@Test
	public void testFindByFieldName() {
		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName("middleName");

		Assertions.assertNotNull(bqFieldMappingOptional.orElse(null));

		bqFieldMappingOptional = _bqFieldMappingRepository.findByFieldName(
			"name");

		Assertions.assertNull(bqFieldMappingOptional.orElse(null));
	}

	@Test
	public void testSearchByFilterString() {
		List<BQFieldMapping> bqFieldMappings =
			_bqFieldMappingRepository.searchByFilterString(
				"((context eq 'demographics') and (ownerType eq 'individual'))",
				PageRequest.of(0, 20));

		Assertions.assertEquals(9, bqFieldMappings.size());
	}

	@Autowired
	private BQFieldMappingRepository _bqFieldMappingRepository;

}