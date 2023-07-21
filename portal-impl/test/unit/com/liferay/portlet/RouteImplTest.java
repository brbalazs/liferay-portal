/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet;

import com.liferay.portal.kernel.portlet.Route;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portlet.internal.RouteImpl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Connor McKay
 */
public class RouteImplTest {

	@Test
	public void testNonmatchingRoute() {
		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(new HttpImpl());

		Map<String, String> parameters = new HashMap<>();

		parameters.put("action", "view");
		parameters.put("id", "bob");

		Map<String, String> originalParameters = new HashMap<>(parameters);

		Route route = new RouteImpl("{action}/{id:\\d+}");

		String url = route.parametersToUrl(parameters);

		Assert.assertNull(url);

		Assert.assertEquals(originalParameters, parameters);
	}

}