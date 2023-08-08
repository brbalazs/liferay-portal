/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.test;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.batch.curator.bot.nanite.DeleteTempFilesNanite;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Matthew Kong
 */
public class DeleteTempFilesNaniteTest
	implements OSBAsahBatchCuratorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() throws Exception {
		_tempPath = Files.createTempDirectory("temp");

		ReflectionTestUtils.setField(
			_deleteTempFilesNanite, "_tempPathName", _tempPath.toString());
	}

	@AfterEach
	public void tearDown() {
		File folder = _tempPath.toFile();

		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (!file.delete()) {
					_log.error(
						"Unable to delete file " + file.getAbsolutePath());
				}
			}
		}

		if (!folder.delete()) {
			_log.error("Unable to delete folder " + folder.getAbsolutePath());
		}
	}

	@Test
	public void testDelete() throws Exception {
		Path newCSVPath = Files.write(
			Paths.get(_tempPath.toString(), "new.csv"), new byte[0]);
		Path oldCSVPath = Files.write(
			Paths.get(_tempPath.toString(), "old.csv"), new byte[0]);

		LocalDateTime localDateTime = LocalDateTime.now();

		localDateTime = localDateTime.minusDays(2);

		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

		Files.setAttribute(
			oldCSVPath, "lastModifiedTime", FileTime.from(instant));

		_deleteTempFilesNanite.run(null);

		File oldCSVFile = oldCSVPath.toFile();

		Assertions.assertFalse(oldCSVFile.exists());

		File newCSVFile = newCSVPath.toFile();

		Assertions.assertTrue(newCSVFile.exists());
	}

	private static final Log _log = LogFactory.getLog(
		DeleteTempFilesNaniteTest.class);

	@Autowired
	private DeleteTempFilesNanite _deleteTempFilesNanite;

	private Path _tempPath;

}