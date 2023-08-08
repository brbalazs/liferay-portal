/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.resource;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StreamUtils;

/**
 * @author Brian Wing Shun Chan
 */
public class ResourceUtil {

	public static List<String> readResourcesToList(String locationPattern)
		throws IOException {

		PathMatchingResourcePatternResolver
			pathMatchingResourcePatternResolver =
				new PathMatchingResourcePatternResolver();

		Resource[] resources = pathMatchingResourcePatternResolver.getResources(
			locationPattern);

		List<String> list = new ArrayList<>(resources.length);

		for (Resource resource : resources) {
			list.add(readResourceToString(resource));
		}

		return list;
	}

	public static JSONArray readResourceToJSONArray(String absolutePath)
		throws Exception {

		return new JSONArray(readResourceToString(absolutePath));
	}

	public static JSONArray readResourceToJSONArray(
			String relativePath, Object relativeToClassOfObject)
		throws Exception {

		return new JSONArray(
			readResourceToString(relativePath, relativeToClassOfObject));
	}

	public static JSONObject readResourceToJSONObject(String absolutePath)
		throws Exception {

		return new JSONObject(readResourceToString(absolutePath));
	}

	public static JSONObject readResourceToJSONObject(
			String relativePath, Object relativeToClassOfObject)
		throws Exception {

		return new JSONObject(
			readResourceToString(relativePath, relativeToClassOfObject));
	}

	public static String readResourceToString(Resource resource)
		throws IOException {

		return StreamUtils.copyToString(
			resource.getInputStream(), StandardCharsets.UTF_8);
	}

	public static String readResourceToString(String absolutePath)
		throws Exception {

		if (absolutePath.startsWith("/")) {
			throw new IllegalArgumentException(
				"Invalid absolute path " + absolutePath);
		}

		ClassPathResource classPathResource = new ClassPathResource(
			absolutePath);

		return readResourceToString(classPathResource);
	}

	public static String readResourceToString(
			String relativePath, Class<?> relativeToClass)
		throws Exception {

		if (relativePath.startsWith("/")) {
			throw new IllegalArgumentException(
				"Invalid relative path " + relativePath);
		}

		ClassPathResource classPathResource = new ClassPathResource(
			relativePath, relativeToClass);

		return readResourceToString(classPathResource);
	}

	public static String readResourceToString(
			String relativePath, Object relativeToClassOfObject)
		throws Exception {

		return readResourceToString(
			relativePath, relativeToClassOfObject.getClass());
	}

}