/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import java.io.File;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class DeleteTempFilesNanite extends BaseNanite {

	@Override
	public boolean isLogRunEnabled() {
		return true;
	}

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		Path contextPath = Paths.get(_tempPathName);

		if (!Files.exists(contextPath)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Skipping nanite because path " + contextPath.toString() +
						" does not exist");
			}

			return;
		}

		LocalDateTime localDateTime = LocalDateTime.now();

		localDateTime = localDateTime.minusDays(1);

		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

		DirectoryStream.Filter<Path> filter = file -> {
			FileTime fileTime = (FileTime)Files.getAttribute(
				file.toAbsolutePath(), "lastModifiedTime");

			Instant fileTimeInstant = fileTime.toInstant();

			if (fileTimeInstant.isBefore(instant)) {
				return true;
			}

			return false;
		};

		try (DirectoryStream directoryStream = Files.newDirectoryStream(
				contextPath, filter)) {

			directoryStream.forEach(
				path -> {
					File file = new File(path.toString());

					if (!file.delete()) {
						_log.error(
							"Unable to delete file " + file.getAbsolutePath());
					}
				});
		}
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private static final Log _log = LogFactory.getLog(
		DeleteTempFilesNanite.class);

	@Value("${osb.asah.backend.temp.path:/temp}")
	private String _tempPathName;

}