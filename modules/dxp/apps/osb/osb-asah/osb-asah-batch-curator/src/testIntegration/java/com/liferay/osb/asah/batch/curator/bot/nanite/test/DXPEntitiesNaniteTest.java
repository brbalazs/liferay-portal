/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.test;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.batch.curator.bot.nanite.DXPEntitiesNanite;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Map;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

/**
 * @author Marcos Martins
 */
@Import(JDBCTestConfiguration.class)
public class DXPEntitiesNaniteTest
	implements OSBAsahBatchCuratorSpringTestContext {

	@BeforeEach
	public void setUp() {
		ProjectIdThreadLocal.setProjectId("test");

		DataSource dataSource = new DataSource("Liferay Brazil");

		dataSource.setCredentialType("Token Authentication");
		dataSource.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("READY");
		dataSource.setStatus("STARTED");
		dataSource.setURL("");

		dataSource = _dataSourceDog.addDataSource(dataSource);

		_dataSourceId = dataSource.getId();
	}

	@Test
	public void testRun() throws Exception {
		DXPEntity dxpEntity = new DXPEntity();

		dxpEntity.setDataSourceId(_dataSourceId);
		dxpEntity.setFieldsJSONObject(
			JSONUtil.put(
				"expando",
				JSONUtil.put(
					"type-Number", "[1,2,3]"
				).put(
					"type-Text", "[apple,banana,orange]"
				)
			).put(
				"firstName", "Test"
			).put(
				"lastName", "Test"
			).put(
				"userId", 123
			));

		_dxpEntityDog.addDXPEntity(dxpEntity, DXPEntity.Type.USER);

		_dxpEntitiesNanite.run(null);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.any()
		);

		JSONObject jsonObject = new JSONObject(argumentCaptor.getValue());

		Assertions.assertTrue(jsonObject.has("expandoFields"));
		Assertions.assertTrue(jsonObject.has("fields"));
		Assertions.assertTrue(jsonObject.has("modifiedDate"));
		Assertions.assertTrue(jsonObject.has("type"));

		Map<String, JSONObject> map = JSONUtil.toJSONObjectMap(
			jsonObject.getJSONArray("expandoFields"), "columnId");

		jsonObject = map.get("type-Text");

		Assertions.assertEquals(
			"[\"apple\",\"banana\",\"orange\"]", jsonObject.get("value"));

		jsonObject = map.get("type-Number");

		Assertions.assertEquals("[1,2,3]", jsonObject.get("value"));
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

	private Long _dataSourceId;

	@Autowired
	private DXPEntitiesNanite _dxpEntitiesNanite;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@MockBean
	private MessageBus _messageBus;

}