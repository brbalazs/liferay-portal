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
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Marcos Martins
 */
@Import(JDBCTestConfiguration.class)
@TestPropertySource(
	properties = "osb.asah.dxp.batch.entities.storage.path=/tmp"
)
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

		List<String> exportedLines = _extractZipFile(_dataSourceId);

		JSONObject jsonObject = new JSONObject(exportedLines.get(0));

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

	private List<String> _extractZipFile(Long dataSourceId) throws Exception {
		List<String> lines = new ArrayList<>();

		Files.walkFileTree(
			Paths.get("/tmp/test/" + dataSourceId),
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(
						Path path, BasicFileAttributes basicFileAttributes)
					throws IOException {

					File file = path.toFile();

					if (StringUtils.contains(file.getName(), ".zip")) {
						ZipInputStream zipInputStream = new ZipInputStream(
							new FileInputStream(file));

						zipInputStream.getNextEntry();

						try (BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(
									zipInputStream, StandardCharsets.UTF_8))) {

							lines.add(bufferedReader.readLine());
						}

						file.delete();
					}

					return FileVisitResult.CONTINUE;
				}

			});

		return lines;
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

	private Long _dataSourceId;

	@Autowired
	private DXPEntitiesNanite _dxpEntitiesNanite;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

}