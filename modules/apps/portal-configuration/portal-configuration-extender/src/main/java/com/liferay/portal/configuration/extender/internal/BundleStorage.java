/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal;

import java.io.IOException;

import java.net.URL;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * @author Carlos Sierra Andrés
 */
public interface BundleStorage {

	public Enumeration<URL> findEntries(
		String root, String pattern, boolean recurse);

	public long getBundleId();

	public URL getEntry(String name);

	public Enumeration<String> getEntryPaths(String name);

	public Dictionary<String, String> getHeaders();

	public String getLocation();

	public URL getResource(String name);

	public Enumeration<URL> getResources(String name) throws IOException;

	public String getSymbolicName();

}