/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.rest.controller.BaseContextRestController;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 */
@RestController
public class ContextRestController extends BaseContextRestController {

	@Override
	protected String getExternalURL() {
		return ServiceConstants.URL_PUBLISHER;
	}

}