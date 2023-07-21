/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.deploy.hot.bundle.customjspbagregistryutil;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.deploy.hot.CustomJspBag;
import com.liferay.portal.kernel.url.URLContainer;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"context.id=TestGlobalCustomJspBag",
		"context.name=Test Global Custom JSP Bag",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = CustomJspBag.class
)
public class TestGlobalCustomJspBag implements CustomJspBag {

	@Override
	public String getCustomJspDir() {
		return StringPool.SLASH;
	}

	@Override
	public List<String> getCustomJsps() {
		return _customJsps;
	}

	@Override
	public URLContainer getURLContainer() {
		return _urlContainer;
	}

	@Override
	public boolean isCustomJspGlobal() {
		return true;
	}

	private final List<String> _customJsps = new ArrayList<>();

	private final URLContainer _urlContainer = new URLContainer() {

		@Override
		public URL getResource(String name) {
			Class<?> clazz = getClass();

			return clazz.getResource("dependencies/bottom-ext.jsp");
		}

		@Override
		public Set<String> getResources(String path) {
			return Collections.singleton("/html/common/themes/bottom-ext.jsp");
		}

	};

}