/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.theme.contributor.extender.internal;

import com.liferay.frontend.theme.contributor.extender.BundleWebResources;

import java.util.Collection;

/**
 * @author Carlos Sierra Andrés
 */
public class BundleWebResourcesImpl implements BundleWebResources {

	public BundleWebResourcesImpl(
		Collection<String> cssResourcePaths,
		Collection<String> jsResourcePaths) {

		_cssResourcePaths = cssResourcePaths;
		_jsResourcePaths = jsResourcePaths;
	}

	@Override
	public Collection<String> getCssResourcePaths() {
		return _cssResourcePaths;
	}

	@Override
	public Collection<String> getJsResourcePaths() {
		return _jsResourcePaths;
	}

	@Override
	public String getServletContextPath() {
		return _servletContextPath;
	}

	public void setServletContextPath(String servletContextPath) {
		_servletContextPath = servletContextPath;
	}

	private final Collection<String> _cssResourcePaths;
	private final Collection<String> _jsResourcePaths;
	private String _servletContextPath;

}