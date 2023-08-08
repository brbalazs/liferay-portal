/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.BQCSVUserDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vishal Reddy
 */
@Disabled
public class BQCSVUserDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddBQCSVUsers() {
		DataSource dataSource = new DataSource();

		dataSource.setId(123L);
		dataSource.setIsNew(Boolean.TRUE);

		_dataSourceRepository.save(dataSource);

		_bqCSVUserDog.addBQCSVUsers(
			Arrays.asList(new BQCSVUser(123L), new BQCSVUser(123L)));

		List<BQCSVUser> bqCSVUsers = _bqCSVUserDog.getBQCSVUsers(
			123L, 0, 20, Sort.asc("id"));

		Assertions.assertEquals(2, bqCSVUsers.size(), bqCSVUsers.toString());

		bqCSVUsers.forEach(
			bqCSVUser -> Assertions.assertNotNull(bqCSVUser.getId()));

		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks();

		Assertions.assertEquals(1, asahTasks.size(), asahTasks.toString());

		AsahTask asahTask = asahTasks.get(0);

		Assertions.assertEquals("CSVUsersNanite", asahTask.getClassName());
		Assertions.assertNotNull(asahTask.getId());

		JSONAssert.assertEquals(
			JSONUtil.put(
				"dataSourceId", "123"
			).put(
				"type", "reprocess"
			),
			asahTask.getContextJSONObject(), false);
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BQCSVUserDog _bqCSVUserDog;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}