/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.common.util.CSVUtil;

import java.io.File;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Matthew Kong
 */
@RequestMapping("/suppressions")
@RestController
public class SuppressionsRestController extends BaseRestController {

	@GetMapping("/logs")
	public ResponseEntity downloadLogs(
			@RequestParam(name = "filter", required = false) String
				filterString)
		throws Exception {

		File file = CSVUtil.createCSVFile(
			_fieldNames, "suppression-logs-", new File(_tempPath));

		return toDownloadResponse(file, "suppression-logs.csv");
	}

	private static final Map<String, String> _fieldNames =
		new LinkedHashMap<String, String>() {
			{
				put("createDate", "Suppression Date");
				put("dataControlTaskBatchId", "Request ID");
				put("dataControlTaskCreateDate", "Request Date");
				put("dataControlTaskStatus", "Request Status");
				put("emailAddress", "Email");
			}
		};

	@Value("${osb.asah.backend.temp.path:/temp}")
	private String _tempPath;

}