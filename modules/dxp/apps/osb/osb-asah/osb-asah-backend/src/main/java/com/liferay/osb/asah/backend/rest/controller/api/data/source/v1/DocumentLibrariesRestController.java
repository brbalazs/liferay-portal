/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1;

import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.dog.BQEventDog;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Matthew Kong
 */
@RequestMapping(
	produces = "application/json", value = "/api/1.0/document-libraries"
)
@RestController
public class DocumentLibrariesRestController extends BaseRestController {

	@GetMapping("/download-count")
	public String getDownloadsCount(
		String assetId, @RequestParam(required = false) String channelId,
		@RequestParam(required = false) String dataSourceId,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		return String.valueOf(
			_bqEventDog.countBQEvents(
				"Document", assetId, Long.valueOf(channelId),
				Long.valueOf(dataSourceId), endLocalDate, "documentDownloaded",
				startLocalDate));
	}

	@GetMapping("/preview-count")
	public String getPreviewsCount(
		String assetId, @RequestParam(required = false) String channelId,
		@RequestParam(required = false) String dataSourceId,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		return String.valueOf(
			_bqEventDog.countBQEvents(
				"Document", assetId, Long.valueOf(channelId),
				Long.valueOf(dataSourceId), endLocalDate, "documentPreviewed",
				startLocalDate));
	}

	@Autowired
	private BQEventDog _bqEventDog;

}