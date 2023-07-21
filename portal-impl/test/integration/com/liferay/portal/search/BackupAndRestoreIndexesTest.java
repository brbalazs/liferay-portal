/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.IndexAdminHelperUtil;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PortalInstances;

import java.util.HashMap;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Cristina González
 */
public class BackupAndRestoreIndexesTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testBackupAndRestore() throws Exception {
		Map<Long, String> backupNames = new HashMap<>();

		for (long companyId : PortalInstances.getCompanyIds()) {
			String backupName = StringUtil.lowerCase(
				BackupAndRestoreIndexesTest.class.getName());

			backupName = backupName + "-" + System.currentTimeMillis();

			IndexAdminHelperUtil.backup(
				companyId, SearchEngineHelper.SYSTEM_ENGINE_ID, backupName);

			backupNames.put(companyId, backupName);
		}

		_group = GroupTestUtil.addGroup();

		for (Map.Entry<Long, String> entry : backupNames.entrySet()) {
			String backupName = entry.getValue();

			IndexAdminHelperUtil.restore(entry.getKey(), backupName);

			IndexAdminHelperUtil.removeBackup(entry.getKey(), backupName);
		}
	}

	@DeleteAfterTestRun
	private Group _group;

}