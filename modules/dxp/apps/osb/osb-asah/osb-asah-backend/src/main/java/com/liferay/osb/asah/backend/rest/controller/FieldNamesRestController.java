/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author David Bhasme
 * @author Shinn Lok
 */
@RequestMapping("/field-names")
@RestController
public class FieldNamesRestController extends BaseRestController {

	@GetMapping
	public String getFieldNames(
		@RequestParam(required = false) String label,
		@RequestParam(required = false) String ownerType,
		@RequestParam(required = false) String[] values) {

		// TODO Implement operation

		return null;
	}

}