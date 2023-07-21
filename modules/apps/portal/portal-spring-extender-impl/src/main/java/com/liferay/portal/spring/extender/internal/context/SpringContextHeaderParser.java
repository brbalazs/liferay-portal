/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.context;

import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class SpringContextHeaderParser {

	public SpringContextHeaderParser(Bundle bundle) {
		_bundle = bundle;
	}

	public String[] getBeanDefinitionFileNames() {
		List<String> beanDefinitionFileNames = new ArrayList<>();

		Dictionary<String, String> headers = _bundle.getHeaders(
			StringPool.BLANK);

		String liferayService = headers.get("Liferay-Service");

		if (liferayService != null) {
			beanDefinitionFileNames.add("META-INF/spring/parent");
		}

		String springContext = headers.get("Liferay-Spring-Context");

		if (springContext != null) {
			Collections.addAll(
				beanDefinitionFileNames, springContext.split(","));
		}

		return beanDefinitionFileNames.toArray(new String[0]);
	}

	private final Bundle _bundle;

}