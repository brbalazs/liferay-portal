/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.util.taglib.compat;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class UtilTaglibCompatTest {

	@Test
	public void testNoConflictingImportedFiles() throws Exception {
		File importedFilesPropertiesFile = new File(
			"imported-files.properties");

		Assert.assertTrue(importedFilesPropertiesFile.exists());

		File srcDir = new File("../../../util-taglib/src/");

		srcDir = srcDir.getCanonicalFile();

		Assert.assertTrue(srcDir + "is not util-taglib/src", srcDir.exists());

		try (FileReader fileReader = new FileReader(
				importedFilesPropertiesFile);
			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(fileReader)) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				if (line.indexOf('=') >= 0) {
					Matcher matcher = _dependencyPattern.matcher(line);

					Assert.assertTrue(matcher.matches());
				}
				else if (Validator.isNotNull(line)) {
					line = line.trim();

					if (line.endsWith(",\\")) {
						line = line.substring(0, line.length() - 2);
					}

					if (line.endsWith(".class")) {
						line = line.substring(0, line.length() - 6) + ".java";
					}

					File file = new File(srcDir, line);

					Assert.assertFalse(
						file.getPath() + " should not exist", file.exists());
				}
			}
		}
	}

	private static final Pattern _dependencyPattern = Pattern.compile(
		"com.liferay.portal\\\\:com.liferay.util.taglib\\\\:" +
			"\\d+\\.\\d+\\.\\d+=\\\\");

}