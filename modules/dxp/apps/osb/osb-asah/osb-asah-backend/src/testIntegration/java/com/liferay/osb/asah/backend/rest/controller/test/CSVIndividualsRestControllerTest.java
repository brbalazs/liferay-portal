/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.CSVIndividualsRestController;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQCSVUserRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.List;

import org.json.JSONArray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vishal Reddy
 */
public class CSVIndividualsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@Test
	public void testPostCSVIndividuals() throws Exception {
		DataSource dataSource = new DataSource();

		dataSource.setId(123L);
		dataSource.setIsNew(Boolean.TRUE);

		_dataSourceRepository.save(dataSource);

		JSONArray jsonArray = JSONUtil.putAll(
			JSONUtil.put(
				"dataSourceId", "123"
			).put(
				"dataSourceIndividualPK", RandomTestUtil.randomUUID()
			).put(
				"fields", JSONUtil.put("foo", "bar")
			),
			JSONUtil.put(
				"dataSourceId", "123"
			).put(
				"dataSourceIndividualPK", RandomTestUtil.randomUUID()
			).put(
				"fields", JSONUtil.put("foo2", "bar2")
			));

		_csvIndividualsRestController.postCSVIndividuals(jsonArray.toString());

		Assertions.assertEquals(2, _bqCSVUserRepository.count());

		_assertBQCSVUsers(_bqCSVUserRepository.findAll());

		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks();

		Assertions.assertEquals(1, asahTasks.size(), asahTasks.toString());

		AsahTask asahTask = asahTasks.get(0);

		Assertions.assertEquals("CSVUsersNanite", asahTask.getClassName());
		Assertions.assertNull(asahTask.getCronExpression());
		Assertions.assertNotNull(asahTask.getId());

		JSONAssert.assertEquals(
			JSONUtil.put(
				"dataSourceId", "123"
			).put(
				"type", "reprocess"
			),
			asahTask.getContextJSONObject(), false);
	}

	private void _assertBQCSVUsers(Iterable<BQCSVUser> bqCSVUsers) {
		for (BQCSVUser bqCSVUser : bqCSVUsers) {
			Assertions.assertEquals(123L, bqCSVUser.getDataSourceId());
			Assertions.assertNotNull(bqCSVUser.getId());

			List<BQCSVUser.Field> fields = bqCSVUser.getFields();

			Assertions.assertEquals(1, fields.size());

			BQCSVUser.Field field = fields.get(0);

			Assertions.assertNotNull(field.getName());
			Assertions.assertNotNull(field.getValue());
		}
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BQCSVUserRepository _bqCSVUserRepository;

	@Autowired
	private CSVIndividualsRestController _csvIndividualsRestController;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}