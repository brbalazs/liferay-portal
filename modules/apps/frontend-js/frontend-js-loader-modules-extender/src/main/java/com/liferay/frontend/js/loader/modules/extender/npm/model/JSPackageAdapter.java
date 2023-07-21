/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.npm.model;

import com.liferay.frontend.js.loader.modules.extender.npm.JSBundle;
import com.liferay.frontend.js.loader.modules.extender.npm.JSModule;
import com.liferay.frontend.js.loader.modules.extender.npm.JSModuleAlias;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackageDependency;
import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides an incomplete implementation of {@link JSPackage} that can be reused
 * by existing implementations.
 *
 * @author Iván Zaera
 */
public abstract class JSPackageAdapter implements JSPackage {

	/**
	 * Constructs a <code>JSPackageAdapter</code> with the JS bundle, name,
	 * version, and default module name.
	 *
	 * @param jsBundle the package's bundle
	 * @param name the package's name
	 * @param version the package's version
	 * @param mainModuleName the default module name
	 */
	public JSPackageAdapter(
		JSBundle jsBundle, String name, String version, String mainModuleName) {

		_jsBundle = jsBundle;
		_name = name;
		_version = version;
		_mainModuleName = mainModuleName;

		_resolvedId = name + StringPool.AT + version;

		_id = jsBundle.getId() + StringPool.SLASH + _resolvedId;
	}

	/**
	 * Adds the module to the package.
	 *
	 * @param jsModule the NPM module
	 */
	public void addJSModule(JSModule jsModule) {
		_jsModules.add(jsModule);
	}

	public void addJSModuleAlias(JSModuleAlias jsModuleAlias) {
		_jsModuleAliases.add(jsModuleAlias);
	}

	/**
	 * Adds the dependency to another NPM package.
	 *
	 * @param jsPackageDependency the NPM package dependency
	 */
	public void addJSPackageDependency(
		JSPackageDependency jsPackageDependency) {

		_jsPackageDependencies.put(
			jsPackageDependency.getPackageName(), jsPackageDependency);
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public JSBundle getJSBundle() {
		return _jsBundle;
	}

	@Override
	public Collection<JSModuleAlias> getJSModuleAliases() {
		return _jsModuleAliases;
	}

	@Override
	public Collection<JSModule> getJSModules() {
		return _jsModules;
	}

	@Override
	public Collection<JSPackageDependency> getJSPackageDependencies() {
		return _jsPackageDependencies.values();
	}

	@Override
	public JSPackageDependency getJSPackageDependency(String packageName) {
		return _jsPackageDependencies.get(packageName);
	}

	@Override
	public String getMainModuleName() {
		return _mainModuleName;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getResolvedId() {
		return _resolvedId;
	}

	@Override
	public String getVersion() {
		return _version;
	}

	private final String _id;
	private final JSBundle _jsBundle;
	private final List<JSModuleAlias> _jsModuleAliases = new ArrayList<>();
	private final List<JSModule> _jsModules = new ArrayList<>();
	private final Map<String, JSPackageDependency> _jsPackageDependencies =
		new HashMap<>();
	private final String _mainModuleName;
	private final String _name;
	private final String _resolvedId;
	private final String _version;

}