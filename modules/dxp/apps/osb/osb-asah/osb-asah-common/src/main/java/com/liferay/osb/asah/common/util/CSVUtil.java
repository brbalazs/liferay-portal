/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.File;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

/**
 * @author Matthew Kong
 */
public class CSVUtil {

	public static File createCSVFile(
			Map<String, String> fieldNames, String fileNamePrefix, File folder,
			List<JSONObject> jsonObjects)
		throws Exception {

		File file = File.createTempFile(fileNamePrefix, ".csv", folder);

		file.deleteOnExit();

		CsvWriter csvWriter = new CsvWriter(file, new CsvWriterSettings());

		csvWriter.writeHeaders(fieldNames.values());

		Set<String> keys = fieldNames.keySet();

		Stream<JSONObject> jsonObjectsStream = jsonObjects.stream();

		jsonObjectsStream.forEach(
			jsonObject -> {
				Stream<String> keysStream = keys.stream();

				csvWriter.writeRow(
					keysStream.map(
						jsonObject::get
					).collect(
						Collectors.toList()
					));
			});

		csvWriter.close();

		return file;
	}

}