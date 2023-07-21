/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.tools.data.partitioning.sql.builder.exporter;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class InsertSQLBuilderTest {

	@Test
	public void testBuildInsert() {
		String[] fields = {"a", "b", "c", "d"};

		InsertSQLBuilder insertSQLBuilder = new InsertSQLBuilder();

		Assert.assertEquals(
			"insert into Foo values (a, b, c, d);\n",
			insertSQLBuilder.buildInsert("Foo", fields));
	}

}