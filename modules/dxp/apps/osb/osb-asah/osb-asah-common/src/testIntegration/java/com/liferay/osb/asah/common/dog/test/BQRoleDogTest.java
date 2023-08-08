/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQRoleDog;
import com.liferay.osb.asah.common.entity.BQRole;
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
@BQSQLResource(resourcePath = "test_bq_role_dog.sql")
public class BQRoleDogTest extends BaseBQDXPEntityDogTestCase {

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testGetBQRolePage() {
		Page<BQRole> bqRolePage = _bqRoleDog.getBQRolePage(
			11L, null, 10, Sort.asc("name"), 0);

		Assertions.assertEquals(2, bqRolePage.getTotalElements());

		List<BQRole> bqRoles = bqRolePage.getContent();

		Assertions.assertEquals(2, bqRoles.size(), bqRoles.toString());

		bqRolePage = _bqRoleDog.getBQRolePage(
			11L, "Test", 10, Sort.asc("name"), 0);

		Assertions.assertEquals(1, bqRolePage.getTotalElements());

		bqRoles = bqRolePage.getContent();

		Assertions.assertEquals(1, bqRoles.size(), bqRoles.toString());

		BQRole bqRole = bqRoles.get(0);

		Assertions.assertEquals("Liferay Brazil", bqRole.getDataSourceName());
	}

	@Autowired
	private BQRoleDog _bqRoleDog;

}