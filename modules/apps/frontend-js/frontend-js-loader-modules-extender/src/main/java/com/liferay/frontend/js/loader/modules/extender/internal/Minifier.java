/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal;

import com.liferay.portal.minifier.MinifierUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Iván Zaera Avellón
 */
@Component(service = Minifier.class)
public class Minifier {

	public String minify(String resourceName, String content) {
		return MinifierUtil.minifyJavaScript(resourceName, content);
	}

}