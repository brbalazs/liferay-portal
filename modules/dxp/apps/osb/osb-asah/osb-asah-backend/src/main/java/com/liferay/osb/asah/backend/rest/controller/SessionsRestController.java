/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.UserSessionDog;
import com.liferay.osb.asah.common.json.JSONUtil;

import org.apache.commons.lang3.StringUtils;

import org.jooq.tools.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rachael Koestartyo
 */
@RequestMapping("/sessions")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.SessionsRestController"
)
public class SessionsRestController extends BaseRestController {

	@GetMapping("/values")
	public PageDTO<String> getBQSessionFieldValuePageDTO(
		@RequestParam String fieldName,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(required = false) String value) {

		Page<String> bqSessionFieldValuePage =
			_userSessionDog.getBQSessionFieldValuePage(
				StringUtils.substringAfter(fieldName, "/"), filterString, page,
				size, value);

		return new PageDTO(
			"_embedded",
			JSONUtil.put(
				"session-values",
				new JSONArray(bqSessionFieldValuePage.getContent())),
			bqSessionFieldValuePage.getNumber(),
			bqSessionFieldValuePage.getSize(),
			bqSessionFieldValuePage.getTotalElements(),
			bqSessionFieldValuePage.getTotalPages());
	}

	@Autowired
	private UserSessionDog _userSessionDog;

}