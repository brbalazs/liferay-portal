/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.zip;

import com.liferay.osb.asah.common.function.UnsafeConsumer;

import java.io.File;
import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Matthew Kong
 */
public class ZipFileBuilder {

	public ZipFileBuilder(File file) {
		_file = file;
	}

	public ZipFileBuilder(String fileName) {
		_file = new File(fileName);
	}

	public void addToZip(
		String fileName,
		UnsafeConsumer<ZipOutputStream, Exception> unsafeConsumer) {

		_unsafeConsumers.put(fileName, unsafeConsumer);
	}

	public void build() throws Exception {
		try (FileOutputStream fileOutputStream = new FileOutputStream(
				_file.getAbsolutePath());
			ZipOutputStream zipOutputStream = new ZipOutputStream(
				fileOutputStream)) {

			for (Map.Entry<String, UnsafeConsumer<ZipOutputStream, Exception>>
					entry : _unsafeConsumers.entrySet()) {

				zipOutputStream.putNextEntry(new ZipEntry(entry.getKey()));

				UnsafeConsumer<ZipOutputStream, Exception> unsafeConsumer =
					entry.getValue();

				try {
					unsafeConsumer.accept(zipOutputStream);
				}
				catch (Exception exception) {
					_log.error(exception, exception);
				}

				zipOutputStream.closeEntry();
			}
		}
	}

	private static final Log _log = LogFactory.getLog(ZipFileBuilder.class);

	private final File _file;
	private final Map<String, UnsafeConsumer<ZipOutputStream, Exception>>
		_unsafeConsumers = new HashMap<>();

}