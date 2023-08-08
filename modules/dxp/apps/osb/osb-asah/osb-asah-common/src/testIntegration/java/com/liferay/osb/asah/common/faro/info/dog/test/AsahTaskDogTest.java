/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.faro.info.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vishal Reddy
 */
public class AsahTaskDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void test() {
		_asahTaskDog.scheduleAsahTask("TestNanite", JSONUtil.put("foo", "bar"));

		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks();

		Assertions.assertEquals(1, asahTasks.size(), asahTasks.toString());

		AsahTask asahTask = asahTasks.get(0);

		Assertions.assertEquals("TestNanite", asahTask.getClassName());

		JSONObject contextJSONObject = asahTask.getContextJSONObject();

		Assertions.assertEquals("bar", contextJSONObject.getString("foo"));
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

}