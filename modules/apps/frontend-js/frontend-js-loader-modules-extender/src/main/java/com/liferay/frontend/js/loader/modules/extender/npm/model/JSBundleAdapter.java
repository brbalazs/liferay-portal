/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.npm.model;

import com.liferay.frontend.js.loader.modules.extender.npm.JSBundle;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides an incomplete implementation of {@link JSBundle} that can be reused
 * by existing implementations.
 *
 * @author Iván Zaera
 */
public abstract class JSBundleAdapter implements JSBundle {

	/**
	 * Constructs a <code>JSBundleAdapter</code> with the ID, name, and version.
	 *
	 * @param id the OSGi bundle's ID
	 * @param name the OSGi bundle's name
	 * @param version the OSGi bundle's version
	 */
	public JSBundleAdapter(String id, String name, String version) {
		_id = id;
		_name = name;
		_version = version;
	}

	/**
	 * Adds the NPM package description to the bundle.
	 *
	 * @param jsPackage the NPM package
	 */
	public void addJSPackage(JSPackage jsPackage) {
		_jsPackages.add(jsPackage);
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public Collection<JSPackage> getJSPackages() {
		return _jsPackages;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getVersion() {
		return _version;
	}

	private final String _id;
	private final Collection<JSPackage> _jsPackages = new ArrayList<>();
	private final String _name;
	private final String _version;

}