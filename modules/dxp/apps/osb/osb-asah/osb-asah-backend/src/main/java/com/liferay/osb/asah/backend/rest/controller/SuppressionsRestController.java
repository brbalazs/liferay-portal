/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.util.CSVUtil;

import java.io.File;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
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

		List<Suppression> suppressions = _suppressionDog.getSuppressions(
			filterString);

		Stream<Suppression> suppressionsStream = suppressions.stream();

		File file = CSVUtil.createCSVFile(
			_fieldNames, "suppression-logs-", new File(_tempPath),
			suppressionsStream.map(
				dataControlTask -> _objectMapper.convertValue(
					dataControlTask, JSONObject.class)
			).collect(
				Collectors.toList()
			));

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

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SuppressionDog _suppressionDog;

	@Value("${osb.asah.backend.temp.path:/temp}")
	private String _tempPath;

}