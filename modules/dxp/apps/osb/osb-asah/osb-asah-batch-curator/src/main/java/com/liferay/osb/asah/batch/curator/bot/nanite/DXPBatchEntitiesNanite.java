/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;
import com.liferay.osb.asah.common.storage.impl.GoogleStorageArchiver;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.IOException;

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
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@ConditionalOnGoogleApplicationCredentials
public class DXPBatchEntitiesNanite extends BaseNanite {

	public DXPBatchEntitiesNanite() {
		_entities.put(
			"com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity",
			"entity");
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

		Path startPath = Paths.get(dxpBatchEntitiesStoragePath);

		if (!Files.exists(startPath)) {
			return;
		}

		Files.walkFileTree(
			startPath,
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(
					Path path, BasicFileAttributes basicFileAttributes) {

					File file = path.toFile();

					if (StringUtils.contains(file.getName(), ".zip") &&
						basicFileAttributes.isRegularFile() &&
						(basicFileAttributes.size() > _EMPTY_ZIP_FILE_LENGTH)) {

						String bucketName = StringUtils.replace(
							_dxpBatchEntitiesBucketTemplate,
							"{googleProjectId}", _gcloudProjectId);

						String absolutePath = file.getAbsolutePath();

						String[] split = absolutePath.split("/");

						int length = split.length;

						String dataSourceId = split[length - 4];
						String resourceName = split[length - 3];
						String uploadType = split[length - 2];

						String folderName = String.format(
							"%s/%s/%s", dataSourceId, resourceName, uploadType);

						_archiveFile(bucketName, file, folderName);

						_triggerDAG(
							resourceName,
							String.format(
								"gs://%s/%s/%s/%s", bucketName,
								ProjectIdThreadLocal.getProjectId(), folderName,
								file.getName()));
					}

					return FileVisitResult.CONTINUE;
				}

			});
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private void _archiveFile(String bucketName, File file, String folderName) {
		if (_log.isInfoEnabled()) {
			_log.info("Archiving file " + file.getAbsolutePath());
		}

		_googleStorageArchiver.archiveSync(
			bucketName, folderName, file, file.getName(),
			ProjectIdThreadLocal.getProjectId());
	}

	private void _triggerDAG(String resourceName, String zipFilePath) {
		String entity = _entities.get(resourceName);

		if (entity == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unregistered entity ingestion for resource " +
						resourceName);
			}

			return;
		}

		String dagId = String.format(
			"dxp_%s_ingestion_dataflow_trigger_%s", entity,
			ProjectIdThreadLocal.getProjectId());

		if (_log.isInfoEnabled()) {
			_log.info("Scheduling DAG " + dagId);
		}

		try {
			GoogleCredentials credentials =
				GoogleCredentials.getApplicationDefault();

			credentials = credentials.createScoped(
				"https://www.googleapis.com/auth/cloud-platform");

			NetHttpTransport netHttpTransport = new NetHttpTransport();

			HttpRequestFactory requestFactory =
				netHttpTransport.createRequestFactory(
					new HttpCredentialsAdapter(credentials));

			HttpRequest httpRequest = requestFactory.buildPostRequest(
				new GenericUrl(
					_composerEndpoint + "/api/v1/dags/" + dagId + "/dagRuns"),
				ByteArrayContent.fromString(
					"application/json",
					JSONUtil.put(
						"conf", JSONUtil.put("zipFilePath", zipFilePath)
					).put(
						"logical_date", DateUtil.newDateString()
					).toString()));

			HttpHeaders httpHeaders = httpRequest.getHeaders();

			httpHeaders.setContentType("application/json");

			HttpResponse httpResponse = httpRequest.execute();

			if (httpResponse.getStatusCode() != 200) {
				_log.error(
					String.format(
						"Unexpected error after triggering DAG %s and ZIP " +
							"path %s. Status code: %s",
						dagId, zipFilePath, httpResponse.getStatusCode()));
			}
		}
		catch (IOException ioException) {
			_log.error(
				String.format(
					"Unable to trigger DAG %s and ZIP path %s", dagId,
					zipFilePath),
				ioException);
		}
	}

	private static final long _EMPTY_ZIP_FILE_LENGTH = 140;

	private static final Log _log = LogFactory.getLog(
		DXPBatchEntitiesNanite.class);

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

}