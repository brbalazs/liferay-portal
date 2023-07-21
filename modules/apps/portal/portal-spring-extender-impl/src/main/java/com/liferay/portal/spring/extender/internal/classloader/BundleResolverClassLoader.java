/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.classloader;

import java.io.IOException;

import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class BundleResolverClassLoader extends ClassLoader {

	public BundleResolverClassLoader(Bundle... bundles) {
		if (bundles.length == 0) {
			throw new IllegalArgumentException(
				"At least one bundle is required");
		}

		_bundles = bundles;
	}

	@Override
	public URL getResource(String name) {
		return findResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) {
		return findResources(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		for (Bundle bundle : _bundles) {
			try {
				return bundle.loadClass(name);
			}
			catch (ClassNotFoundException cnfe) {
			}
		}

		throw new ClassNotFoundException(name);
	}

	@Override
	protected URL findResource(String name) {
		for (Bundle bundle : _bundles) {
			URL url = bundle.getResource(name);

			if (url != null) {
				return url;
			}
		}

		return null;
	}

	@Override
	protected Enumeration<URL> findResources(String name) {
		for (Bundle bundle : _bundles) {
			try {
				Enumeration<URL> resources = bundle.getResources(name);

				if ((resources != null) && resources.hasMoreElements()) {
					return resources;
				}
			}
			catch (IOException ioe) {
			}
		}

		return Collections.enumeration(Collections.<URL>emptyList());
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException {

		Class<?> clazz = findClass(name);

		if (resolve) {
			resolveClass(clazz);
		}

		return clazz;
	}

	private final Bundle[] _bundles;

}