/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.http.Http;
import com.liferay.osb.asah.common.storage.impl.GoogleStorageArchiver;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class DXPBatchEntitiesNanite extends BaseNanite {

	public DXPBatchEntitiesNanite() {
		_entities.put(
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Order",
			"order");
		_entities.put(
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Product",
			"product");
	}

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		String dxpBatchEntitiesStoragePath =
			_dxpBatchEntitiesStoragePath + "/" +
				ProjectIdThreadLocal.getProjectId();

		Files.walkFileTree(
			Paths.get(dxpBatchEntitiesStoragePath),
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(
					Path path, BasicFileAttributes basicFileAttributes) {

					File file = path.toFile();

					if (StringUtils.contains(file.getName(), ".zip") &&
						basicFileAttributes.isRegularFile() &&
						(basicFileAttributes.size() > 0)) {

						_archiveFile(file);
					}

					return FileVisitResult.CONTINUE;
				}

			});
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private void _archiveFile(File file) {
		String absolutePath = file.getAbsolutePath();

		if (_log.isInfoEnabled()) {
			_log.info("Archiving file " + absolutePath);
		}

		String bucketName = StringUtils.replace(
			_dxpBatchEntitiesBucketTemplate, "{googleProjectId}",
			_gcloudProjectId);

		String[] split = absolutePath.split("/");

		int length = split.length;

		String dataSourceId = split[length - 4];
		String resourceName = split[length - 3];
		String uploadType = split[length - 2];

		String folderName = String.format(
			"%s/%s/%s", dataSourceId, resourceName, uploadType);

		String fileName = split[length - 1];

		fileName = fileName.substring(0, fileName.lastIndexOf("."));

		_googleStorageArchiver.archiveSync(
			bucketName, folderName, file, fileName,
			ProjectIdThreadLocal.getProjectId());

		String dagId = String.format(
			"dxp_%s_ingestion_dataflow_trigger_%s", _entities.get(resourceName),
			ProjectIdThreadLocal.getProjectId());

		if (_log.isInfoEnabled()) {
			_log.info("Scheduling DAG " + dagId);
		}

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HttpHeaders.ACCEPT, "application/json");

		httpHeaders.setBearerAuth(_composerAuthToken);

		ResponseEntity<String> responseEntity = _http.exchangeResponseEntity(
			_composerEndpoint, "/api/v1/dags/" + dagId + "/dagRuns",
			HttpMethod.POST,
			JSONUtil.put(
				"conf",
				JSONUtil.put(
					"zipFilePath", bucketName + folderName + "/" + fileName)
			).put(
				"logical_date", DateUtil.newDateString()
			),
			httpHeaders);

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			_log.error(
				"Unable to schedule DXP ingestion DAG. Code: " +
					responseEntity.getStatusCodeValue());
		}
	}

	private static final Log _log = LogFactory.getLog(
		DXPBatchEntitiesNanite.class);

	@Value("${osb.asah.composer.auth.token}")
	private String _composerAuthToken;

	@Value("${osb.asah.composer.endpoint}")
	private String _composerEndpoint;

	@Value(
		"${osb.asah.dxp.batch.entities.google.bucket:{googleProjectId}-dxp-entities}"
	)
	private String _dxpBatchEntitiesBucketTemplate;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage}")
	private String _dxpBatchEntitiesStoragePath;

	private final Map<String, String> _entities = new HashMap<>();

	@Value("${osb.asah.gcloud.project.id:liferaycloud-customer-ac}")
	private String _gcloudProjectId;

	@Autowired
	private GoogleStorageArchiver _googleStorageArchiver;

	@Autowired
	private Http _http;

}