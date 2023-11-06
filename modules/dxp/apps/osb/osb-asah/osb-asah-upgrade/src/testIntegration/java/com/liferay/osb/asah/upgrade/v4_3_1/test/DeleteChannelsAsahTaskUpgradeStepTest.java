/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_1.test;

import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.upgrade.OSBAsahUpgradeSpringTestContext;
import com.liferay.osb.asah.upgrade.v4_3_1.DeleteChannelsAsahTaskUpgradeStep;

import java.util.List;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leslie Wong
 */
public class DeleteChannelsAsahTaskUpgradeStepTest
	implements OSBAsahTestExecutionListenersContext,
			   OSBAsahUpgradeSpringTestContext {

	@SQLResource(
		resourcePath = "delete_channels_asah_task_upgrade_step_test.sql"
	)
	@Test
	public void testUpgrade() throws Exception {
		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks(
			"DeleteChannelsNanite");

		for (AsahTask asahTask : asahTasks) {
			JSONObject contextJSONObject = asahTask.getContextJSONObject();

			Assertions.assertFalse(contextJSONObject.has("createDate"));
		}

		_deleteChannelsAsahTaskUpgradeStep.upgrade("");

		asahTasks = _asahTaskDog.getAsahTasks("DeleteChannelsNanite");

		for (AsahTask asahTask : asahTasks) {
			JSONObject contextJSONObject = asahTask.getContextJSONObject();

			Assertions.assertTrue(contextJSONObject.has("createDate"));
		}
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private DeleteChannelsAsahTaskUpgradeStep
		_deleteChannelsAsahTaskUpgradeStep;

}