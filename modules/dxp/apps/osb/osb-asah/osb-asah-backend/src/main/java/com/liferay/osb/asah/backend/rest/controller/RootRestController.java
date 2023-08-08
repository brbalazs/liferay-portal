/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.common.servlet.util.ServletRequestUtil;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 */
@RequestMapping("/")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.RootRestController"
)
public class RootRestController extends BaseRestController {

	@GetMapping("/")
	public String get(HttpServletRequest httpServletRequest) throws Exception {
		String content = ResourceUtil.readResourceToString(
			"endpoints.json", this);

		return content.replaceAll(
			_PLACEHOLDER_URL,
			ServletRequestUtil.getOriginalURL(httpServletRequest));
	}

	private static final String _PLACEHOLDER_URL = Pattern.quote("{url}");

}