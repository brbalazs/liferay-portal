/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_3;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.BaseUpgradeDBColumnSizeTestCase;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;

/**
 * @author Preston Crary
 */
public class UpgradeSQLServerTest extends BaseUpgradeDBColumnSizeTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		DB db = DBManagerUtil.getDB();

		Assume.assumeTrue(DBType.SQLSERVER.equals(db.getDBType()));
	}

	@Override
	protected String getCreateTestTableSQL() {
		return "create table TestTable (testTableId int not null primary " +
			"key, testValue varchar(2000) null)";
	}

	@Override
	protected int getInitialSize() {
		return 2000;
	}

	@Override
	protected String getTypeName() {
		return "nvarchar";
	}

	@Override
	protected UpgradeSQLServer getUpgradeProcess() {
		return new UpgradeSQLServer();
	}

}