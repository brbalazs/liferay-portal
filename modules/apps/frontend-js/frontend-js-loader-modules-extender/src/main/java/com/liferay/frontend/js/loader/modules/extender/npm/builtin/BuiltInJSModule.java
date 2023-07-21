/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.npm.builtin;

import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;
import com.liferay.frontend.js.loader.modules.extender.npm.ModuleNameUtil;
import com.liferay.frontend.js.loader.modules.extender.npm.model.JSModuleAdapter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.util.Collection;

/**
 * Provides an incomplete implementation of {@link JSModule} that lets its
 * contents be retrieved with an HTTP request to the portal.
 *
 * <p>
 * This class assumes that the {@link BuiltInJSModuleServlet} and {@link
 * BuiltInJSResolvedModuleServlet} are installed and running in the portal.
 * These servlets are responsible for exporting the contents returned by the
 * {@link JSModule#getInputStream()} method implemented by subclasses inheriting
 * from this class.
 * </p>
 *
 * @author Iván Zaera
 */
public abstract class BuiltInJSModule extends JSModuleAdapter {

	/**
	 * Constructs a <code>BuiltInJSModule</code> with the module's JS package,
	 * name, and dependencies.
	 *
	 * @param jsPackage the package containing the module
	 * @param name the module's name
	 * @param dependencies the module's dependencies
	 */
	public BuiltInJSModule(
		JSPackage jsPackage, String name, Collection<String> dependencies) {

		super(
			jsPackage, name, _getURL(jsPackage, name),
			_getResolvedURL(jsPackage, name), _getResolvedId(jsPackage, name),
			dependencies);
	}

	/**
	 * Composes a resolved ID given the package and module name.
	 *
	 * @param  jsPackage the NPM package
	 * @param  moduleName the module's name
	 * @return a resolved ID
	 */
	private static String _getResolvedId(
		JSPackage jsPackage, String moduleName) {

		StringBundler sb = new StringBundler(5);

		sb.append(jsPackage.getName());
		sb.append(StringPool.AT);
		sb.append(jsPackage.getVersion());
		sb.append(StringPool.SLASH);
		sb.append(moduleName);

		return sb.toString();
	}

	/**
	 * Composes a resolved URL given the package and module name.
	 *
	 * @param  jsPackage the NPM package
	 * @param  moduleName the module's name
	 * @return a resolved URL
	 */
	private static String _getResolvedURL(
		JSPackage jsPackage, String moduleName) {

		StringBundler sb = new StringBundler(2);

		sb.append("/o/js/resolved-module/");
		sb.append(_getResolvedId(jsPackage, moduleName));

		return sb.toString();
	}

	/**
	 * Composes a canonical URL given the package and module name.
	 *
	 * @param jsPackage the NPM package
	 * @param moduleName the module's name
	 */
	private static String _getURL(JSPackage jsPackage, String moduleName) {
		StringBundler sb = new StringBundler(2);

		sb.append("/o/js/module/");
		sb.append(ModuleNameUtil.getModuleId(jsPackage, moduleName));

		return sb.toString();
	}

}