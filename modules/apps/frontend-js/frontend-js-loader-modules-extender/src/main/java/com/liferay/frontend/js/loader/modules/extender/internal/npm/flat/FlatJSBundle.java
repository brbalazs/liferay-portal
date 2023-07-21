/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal.npm.flat;

import com.liferay.frontend.js.loader.modules.extender.npm.model.JSBundleAdapter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.net.URL;

import java.util.Enumeration;

import org.osgi.framework.Bundle;

/**
 * Provides a complete implementation of {@link
 * com.liferay.frontend.js.loader.modules.extender.npm.JSBundle}.
 *
 * @author Iván Zaera
 */
public class FlatJSBundle extends JSBundleAdapter {

	/**
	 * Constructs a <code>FlatJSBundle</code> with the OSGi bundle.
	 *
	 * @param bundle the OSGi bundle to which this object refers
	 */
	public FlatJSBundle(Bundle bundle) {
		super(
			String.valueOf(bundle.getBundleId()), bundle.getSymbolicName(),
			bundle.getVersion().toString());

		_bundle = bundle;
	}

	/**
	 * Returns the entries inside an OSGi bundle path given the glob pattern.
	 *
	 * @param  path the path where the search must start
	 * @param  filePattern the glob pattern of files to look for
	 * @param  recurse whether to exclusively look for files in the path;
	 *         otherwise, look for files in the path and its subfolders
	 * @return the entries inside an OSGi bundle path
	 */
	public Enumeration<URL> findEntries(
		String path, String filePattern, boolean recurse) {

		return _bundle.findEntries(path, filePattern, recurse);
	}

	@Override
	public URL getResourceURL(String location) {
		return _bundle.getResource(location);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append(getId());
		sb.append(StringPool.COLON);
		sb.append(getName());
		sb.append(StringPool.AT);
		sb.append(getVersion());

		return sb.toString();
	}

	private final Bundle _bundle;

}