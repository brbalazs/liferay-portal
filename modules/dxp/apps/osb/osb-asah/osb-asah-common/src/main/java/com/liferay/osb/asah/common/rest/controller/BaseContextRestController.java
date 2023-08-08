/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.rest.controller;

import com.liferay.osb.asah.common.json.JSONUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 */
@RequestMapping("/context")
@RestController
public abstract class BaseContextRestController {

	@GetMapping
	public String get() {
		return JSONUtil.put(
			"environment",
			JSONUtil.put(
				"EXTERNAL_URL", getExternalURL()
			).put(
				"LABEL_BUILD_DATE", System.getenv("LABEL_BUILD_DATE")
			).put(
				"LABEL_VCS_REF", System.getenv("LABEL_VCS_REF")
			)
		).toString();
	}

	protected String getExternalURL() {
		return null;
	}

}