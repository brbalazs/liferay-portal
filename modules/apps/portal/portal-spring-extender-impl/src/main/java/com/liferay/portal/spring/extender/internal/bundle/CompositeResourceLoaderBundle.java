/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.bundle;

import java.io.File;
import java.io.InputStream;

import java.net.URL;

import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;

/**
 * @author Miguel Pastor
 */
public class CompositeResourceLoaderBundle implements Bundle {

	public CompositeResourceLoaderBundle(Bundle... bundles) {
		_bundles = bundles;
	}

	@Override
	public <A> A adapt(Class<A> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Bundle o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<URL> findEntries(
		String path, String filePattern, boolean recurse) {

		List<URL> urls = new ArrayList<>();

		for (int i = _bundles.length - 1; i >= 0; i--) {
			Enumeration<URL> enumeration = _bundles[i].findEntries(
				path, filePattern, recurse);

			if ((enumeration != null) && enumeration.hasMoreElements()) {
				urls.addAll(Collections.list(enumeration));
			}
		}

		return Collections.enumeration(urls);
	}

	@Override
	public BundleContext getBundleContext() {
		return _bundles[0].getBundleContext();
	}

	@Override
	public long getBundleId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public File getDataFile(String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getEntry(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getEntryPaths(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dictionary<String, String> getHeaders() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dictionary<String, String> getHeaders(String locale) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastModified() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceReference<?>[] getRegisteredServices() {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getResource(String name) {
		for (Bundle bundle : _bundles) {
			URL url = bundle.getResource(name);

			if (url != null) {
				return url;
			}
		}

		return null;
	}

	@Override
	public Enumeration<URL> getResources(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceReference<?>[] getServicesInUse() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(
		int signersType) {

		throw new UnsupportedOperationException();
	}

	@Override
	public int getState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSymbolicName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Version getVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(Object permission) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> loadClass(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start(int options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void stop() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void stop(int options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void uninstall() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(InputStream inputStream) {
		throw new UnsupportedOperationException();
	}

	private final Bundle[] _bundles;

}