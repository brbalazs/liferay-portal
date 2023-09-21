/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.storage.impl.GoogleStorageArchiver;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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
public class DXPBatchEntitiesNanite extends BaseNanite {

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		if (_googleStorageArchiver == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Skipping DXP entities Cloud Storage upload");
			}

			return;
		}

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

		String[] split = absolutePath.split("/");

		int length = split.length;

		String dataSourceId = split[length - 4];
		String fileName = split[length - 1];
		String resourceName = split[length - 3];
		String uploadType = split[length - 2];

		_googleStorageArchiver.archiveSync(
			StringUtils.replace(
				_dxpBatchEntitiesBucketTemplate, "{googleProjectId}",
				_gcloudProjectId),
			String.format("%s/%s/%s", dataSourceId, resourceName, uploadType),
			file, fileName.substring(0, fileName.lastIndexOf(".")),
			ProjectIdThreadLocal.getProjectId());
	}

	private static final Log _log = LogFactory.getLog(
		DXPBatchEntitiesNanite.class);

	@Value(
		"${osb.asah.dxp.batch.entities.google.bucket:{googleProjectId}-dxp-entities}"
	)
	private String _dxpBatchEntitiesBucketTemplate;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage}")
	private String _dxpBatchEntitiesStoragePath;

	@Value("${osb.asah.gcloud.project.id:liferaycloud-customer-ac}")
	private String _gcloudProjectId;

	@Autowired(required = false)
	private GoogleStorageArchiver _googleStorageArchiver;

}