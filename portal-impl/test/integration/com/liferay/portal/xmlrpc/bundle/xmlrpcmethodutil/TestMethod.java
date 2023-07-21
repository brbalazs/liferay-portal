/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.xmlrpc.bundle.xmlrpcmethodutil;

import com.liferay.portal.kernel.xmlrpc.Method;
import com.liferay.portal.kernel.xmlrpc.Response;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = Method.class
)
public class TestMethod implements Method {

	public static String METHOD_NAME = "METHOD_NAME";

	public static String TOKEN = "TOKEN";

	@Override
	public Response execute(long companyId) {
		return null;
	}

	@Override
	public String getMethodName() {
		return METHOD_NAME;
	}

	@Override
	public String getToken() {
		return TOKEN;
	}

	@Override
	public boolean setArguments(Object[] arguments) {
		return false;
	}

}