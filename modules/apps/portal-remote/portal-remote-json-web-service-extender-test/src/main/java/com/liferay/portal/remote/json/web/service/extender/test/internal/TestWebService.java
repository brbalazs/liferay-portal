/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.json.web.service.extender.test.internal;

import com.liferay.portal.kernel.jsonwebservice.JSONWebService;

/**
 * @author Carlos Sierra Andrés
 */
@JSONWebService
public class TestWebService {

	public int sum(int a, int b) {
		return a + b;
	}

}