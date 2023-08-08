/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQUserDog;
import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.model.ExpandoField;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author Marcos Martins
 */
public class BQUserDogTest extends BaseBQDXPEntityDogTestCase {

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
	}

	@BQSQLResource(resourcePath = "test_bq_user_dog.sql")
	@Test
	public void testGetBQUserPage() {
		Page<BQUser> bqUserPage = _bqUserDog.getBQUserPage(
			11L, null, 10, Sort.asc("firstName"), 0);

		Assertions.assertEquals(2, bqUserPage.getTotalElements());

		List<BQUser> bqUsers = bqUserPage.getContent();

		Assertions.assertEquals(2, bqUsers.size(), bqUsers.toString());

		bqUserPage = _bqUserDog.getBQUserPage(
			11L, "Test", 10, Sort.asc("firstName"), 0);

		Assertions.assertEquals(1, bqUserPage.getTotalElements());

		bqUsers = bqUserPage.getContent();

		Assertions.assertEquals(1, bqUsers.size(), bqUsers.toString());

		BQUser bqUser = bqUsers.get(0);

		Assertions.assertEquals("Liferay Brazil", bqUser.getDataSourceName());
	}

	@BQSQLResource(resourcePath = "test_bq_user_dog.sql")
	@Test
	public void testGetBQUsers() {
		List<BQUser> bqUsers = _bqUserDog.getBQUsers(
			Collections.singletonMap("dxpUserId", 2), PageRequest.of(0, 10));

		Assertions.assertEquals(1, bqUsers.size(), bqUsers.toString());

		BQUser bqUser = bqUsers.get(0);

		List<ExpandoField> expandoFields = bqUser.getExpandoFields();

		Assertions.assertEquals(
			1, expandoFields.size(), expandoFields.toString());

		ExpandoField expandoField = expandoFields.get(0);

		Assertions.assertEquals("1", expandoField.getColumnId());
		Assertions.assertEquals("column", expandoField.getName());
		Assertions.assertEquals("test", expandoField.getValue());
	}

	@Autowired
	private BQUserDog _bqUserDog;

}