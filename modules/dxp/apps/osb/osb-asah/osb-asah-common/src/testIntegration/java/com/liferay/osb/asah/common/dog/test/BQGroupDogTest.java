/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQGroupDog;
import com.liferay.osb.asah.common.entity.BQGroup;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQGroupRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Marcos Martins
 */
@BQSQLResource(resourcePath = "test_bq_group_dog.sql")
public class BQGroupDogTest extends BaseBQDXPEntityDogTestCase {

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testGetBQGroupPage() {
		Page<BQGroup> bqGroupPage = _bqGroupDog.getBQGroupPage(
			11L, null, 10, Sort.asc("name"), 0);

		Assertions.assertEquals(2, bqGroupPage.getTotalElements());

		List<BQGroup> bqGroups = bqGroupPage.getContent();

		Assertions.assertEquals(2, bqGroups.size(), bqGroups.toString());

		bqGroupPage = _bqGroupDog.getBQGroupPage(
			11L, "Test", 10, Sort.asc("name"), 0);

		Assertions.assertEquals(1, bqGroupPage.getTotalElements());

		bqGroups = bqGroupPage.getContent();

		Assertions.assertEquals(1, bqGroups.size(), bqGroups.toString());

		BQGroup bqGroup = bqGroups.get(0);

		Assertions.assertEquals("Liferay Brazil", bqGroup.getDataSourceName());
	}

	@Autowired
	private BQGroupDog _bqGroupDog;

	@Autowired
	private BQGroupRepository _bqGroupRepository;

}