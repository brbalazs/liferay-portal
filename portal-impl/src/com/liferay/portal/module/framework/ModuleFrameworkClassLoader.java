/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.module.framework;

import java.io.IOException;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel Pastor
 */
public class ModuleFrameworkClassLoader extends URLClassLoader {

	public ModuleFrameworkClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public URL getResource(String name) {
		URL url = findResource(name);

		if (url == null) {
			url = super.getResource(name);
		}

		return url;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		final List<URL> urls = new ArrayList<>();

		urls.addAll(_buildURLs(null));

		Enumeration<URL> localURLs = findResources(name);

		urls.addAll(_buildURLs(localURLs));

		Enumeration<URL> parentURLs = null;

		ClassLoader parentClassLoader = getParent();

		if (parentClassLoader != null) {
			parentURLs = parentClassLoader.getResources(name);
		}

		urls.addAll(_buildURLs(parentURLs));

		return new Enumeration<URL>() {

			@Override
			public boolean hasMoreElements() {
				return _iterator.hasNext();
			}

			@Override
			public URL nextElement() {
				return _iterator.next();
			}

			private final Iterator<URL> _iterator = urls.iterator();

		};
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException {

		Object lock = getClassLoadingLock(name);

		synchronized (lock) {
			Class<?> clazz = findLoadedClass(name);

			if (clazz == null) {
				try {
					clazz = findClass(name);
				}
				catch (ClassNotFoundException cnfe) {
					clazz = super.loadClass(name, resolve);
				}
			}

			if (resolve) {
				resolveClass(clazz);
			}

			return clazz;
		}
	}

	private List<URL> _buildURLs(Enumeration<URL> url) {
		if (url == null) {
			return new ArrayList<>();
		}

		List<URL> urls = new ArrayList<>();

		while (url.hasMoreElements()) {
			urls.add(url.nextElement());
		}

		return urls;
	}

	static {
		ClassLoader.registerAsParallelCapable();
	}

}