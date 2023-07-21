/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.lpkg.deployer.log;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class LPKGIndexValidatorLogScannerTest {

	@Test
	public void testScanLogForValidation() throws IOException {
		String liferayHome = System.getProperty("liferay.home");

		Assert.assertNotNull(
			"Missing system property \"liferay.home\"", liferayHome);

		Path logsPath = Paths.get(
			System.getProperty("liferay.log.dir", liferayHome.concat("/logs")));

		boolean hasLog = false;

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
				logsPath, "liferay*.log")) {

			for (Path logPath : directoryStream) {
				hasLog = true;

				String logContent = new String(
					Files.readAllBytes(logPath), StandardCharsets.UTF_8);

				Assert.assertFalse(
					"Running validation because of mismatched checksum, see " +
						"log for details",
					logContent.contains(
						"Running validation because of mismatched checksum"));
			}
		}

		Assert.assertTrue(
			"Unable to find any log file under " + logsPath, hasLog);
	}

}