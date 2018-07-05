/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.initializer.beryl.internal.tools.util;

import com.liferay.commerce.initializer.beryl.internal.tools.BerylSampleForecastsBuilder;
import com.liferay.petra.string.StringPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;

/**
 * @author Andrea Di Giorgi
 */
public class BerylToolsUtil {

	public static String read(String name) throws IOException {
		StringBuilder sb = new StringBuilder();

		ClassLoader classLoader =
			BerylSampleForecastsBuilder.class.getClassLoader();

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
					classLoader.getResourceAsStream(name),
					StandardCharsets.UTF_8))) {

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
		}

		return sb.toString();
	}

	public static void write(Path path, JSONArray jsonArray)
		throws IOException {

		String json = jsonArray.toString(StringPool.FOUR_SPACES.length());

		json = json.replace(StringPool.FOUR_SPACES, StringPool.TAB);

		Files.createDirectories(path.getParent());

		Files.write(path, json.getBytes(StandardCharsets.UTF_8));
	}

}