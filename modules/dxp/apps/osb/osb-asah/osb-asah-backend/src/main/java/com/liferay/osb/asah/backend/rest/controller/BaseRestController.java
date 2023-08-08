/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.common.dog.AsahMarkerDog;
import com.liferay.osb.asah.common.json.JSONUtil;

import java.io.File;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class BaseRestController {

	protected JSONObject getPageJSONObject(
		int page, int size, long totalElements) {

		return JSONUtil.put(
			"number", page
		).put(
			"size", size
		).put(
			"totalElements", totalElements
		).put(
			"totalPages", ((totalElements - 1) / Math.max(size, 1)) + 1
		);
	}

	protected ResponseEntity toDownloadResponse(File file, String fileName) {
		if (!file.exists()) {
			return toNotFoundResponse();
		}

		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

		bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);
		bodyBuilder.header(
			HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + fileName + "\"");

		return bodyBuilder.body(new FileSystemResource(file.getAbsolutePath()));
	}

	protected ResponseEntity toNotFoundResponse() {
		ResponseEntity.HeadersBuilder headersBuilder =
			ResponseEntity.notFound();

		return headersBuilder.build();
	}

	@Autowired
	protected AsahMarkerDog asahMarkerDog;

}