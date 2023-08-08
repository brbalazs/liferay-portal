/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.rest.controller;

import com.liferay.osb.asah.common.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leslie Wong
 */
@RestController
public class ErrorRestController extends AbstractErrorController {

	public ErrorRestController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping(produces = "application/json", value = "/error")
	public String handleError(HttpServletRequest httpServletRequest) {
		HttpStatus httpStatus = getStatus(httpServletRequest);

		return JSONUtil.put(
			"error", httpStatus.getReasonPhrase()
		).put(
			"message",
			"Encountered error with status code " + httpStatus.value()
		).put(
			"status", httpStatus.value()
		).toString();
	}

}