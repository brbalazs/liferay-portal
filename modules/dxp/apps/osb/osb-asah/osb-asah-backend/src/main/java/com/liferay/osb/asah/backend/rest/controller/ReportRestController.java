/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dog.ReportDog;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.model.TimeRange;

import java.io.File;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcos Martins
 */
@RequestMapping(produces = "application/json", value = "/reports")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.ReportRestController"
)
public class ReportRestController {

	@GetMapping("/export/csv/{type}")
	public ResponseEntity<FileSystemResource> getCSVReport(
			@RequestParam(required = false) String assetId,
			@RequestParam(required = false) String assetType,
			@RequestParam String channelId,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(required = false) String query,
			@RequestParam(name = "sort", required = false) String[] sorts,
			@RequestParam(name = "toDate", required = false) String toDate,
			@PathVariable String type)
		throws Exception {

		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

		bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

		LocalDateTime localDateTime = LocalDateTime.now();

		ZonedDateTime zonedDateTime = localDateTime.atZone(
			TimeZoneDogUtil.getZoneId());

		Instant instant = zonedDateTime.toInstant();

		String fileName = String.format(
			"%s-report-%s.csv", StringUtils.lowerCase(type),
			instant.toEpochMilli());

		bodyBuilder.header(
			HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + fileName + "\"");

		if (!StringUtils.equals(type, "individual")) {
			_validateDateRange(fromDate, toDate);
		}

		File file = _reportDog.getCSVReport(
			assetId, assetType, Long.valueOf(channelId), query, sorts,
			TimeRange.of(
				LocalDateTime.parse(toDate, _dateTimeFormatter),
				LocalDateTime.parse(fromDate, _dateTimeFormatter)),
			type);

		return bodyBuilder.body(new FileSystemResource(file.getAbsolutePath()));
	}

	private void _validateDateRange(String fromDate, String toDate) {
		if ((fromDate == null) || (toDate == null)) {
			throw new IllegalArgumentException("Date range is mandatory");
		}

		Date fromUTCDate = null;
		Date toUTCDate = null;

		try {
			fromUTCDate = DateUtil.toUTCDate(fromDate);
			toUTCDate = DateUtil.toUTCDate(toDate);
		}
		catch (Exception exception) {
			throw new IllegalArgumentException(
				"Unable to convert to UTC date", exception);
		}

		if (fromUTCDate.after(toUTCDate)) {
			throw new IllegalArgumentException("From date is after to date");
		}
	}

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss.SSS'Z']");

	@Autowired
	private ReportDog _reportDog;

}