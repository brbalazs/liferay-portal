/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQUserGroupDog;
import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.model.Sort;
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
@BQSQLResource(resourcePath = "test_bq_user_group_dog.sql")
public class BQUserGroupDogTest extends BaseBQDXPEntityDogTestCase {

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testGetBQUserGroupPage() {
		Page<BQUserGroup> bqUserGroupPage = _bqUserGroupDog.getBQUserGroupPage(
			11L, null, 10, Sort.asc("name"), 0);

		Assertions.assertEquals(2, bqUserGroupPage.getTotalElements());

		List<BQUserGroup> bqUserGroups = bqUserGroupPage.getContent();

		Assertions.assertEquals(
			2, bqUserGroups.size(), bqUserGroups.toString());

		bqUserGroupPage = _bqUserGroupDog.getBQUserGroupPage(
			11L, "Test", 10, Sort.asc("name"), 0);

		Assertions.assertEquals(1, bqUserGroupPage.getTotalElements());

		bqUserGroups = bqUserGroupPage.getContent();

		Assertions.assertEquals(
			1, bqUserGroups.size(), bqUserGroups.toString());

		BQUserGroup bqUserGroup = bqUserGroups.get(0);

		Assertions.assertEquals(
			"Liferay Brazil", bqUserGroup.getDataSourceName());
	}

	@Autowired
	private BQUserGroupDog _bqUserGroupDog;

}