/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.classloader;

import java.io.IOException;

import java.net.URL;

import java.util.Enumeration;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.osgi.framework.Bundle;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Pastor
 */
@RunWith(PowerMockRunner.class)
public class BundleResolverClassLoaderTest extends PowerMockito {

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyClassLoaders() {
		new BundleResolverClassLoader();
	}

	@Test
	public void testGetResourceDefinedInFirstClassLoader() {
		ClassLoader classLoader = _buildClassLoader();

		_mockGetResource(_bundle1, _NAME, _url);

		classLoader.getResource(_NAME);

		_verifyGetResource(_bundle1, _NAME);
	}

	@Test
	public void testGetResourceDefinedInSecondClassLoader() {
		ClassLoader classLoader = _buildClassLoader();

		_mockGetResource(_bundle1, _NAME, null);
		_mockGetResource(_bundle2, _NAME, _url);

		classLoader.getResource(_NAME);

		_verifyGetResource(_bundle1, _NAME);
		_verifyGetResource(_bundle2, _NAME);
	}

	@Test
	public void testGetResourcesDefinedInFirstClassLoader() throws IOException {
		ClassLoader classLoader = _buildClassLoader();

		_mockGetResources(_bundle1, _NAME, _enumeration);

		classLoader.getResources(_NAME);

		_verifyGetResources(_bundle1, _NAME);
	}

	@Test
	public void testGetResourcesDefinedInSecondClassLoader()
		throws IOException {

		ClassLoader classLoader = _buildClassLoader();

		_mockGetResources(_bundle1, _NAME, null);
		_mockGetResources(_bundle2, _NAME, _enumeration);

		classLoader.getResources(_NAME);

		_verifyGetResources(_bundle1, _NAME);
		_verifyGetResources(_bundle2, _NAME);
	}

	private ClassLoader _buildClassLoader() {
		return new BundleResolverClassLoader(_bundle1, _bundle2);
	}

	private void _mockGetResource(Bundle bundle, String name, URL url) {
		when(
			bundle.getResource(name)
		).thenReturn(
			url
		);
	}

	private void _mockGetResources(
			Bundle bundle, String name, Enumeration<URL> enumeration)
		throws IOException {

		when(
			bundle.getResources(name)
		).thenReturn(
			enumeration
		);
	}

	private void _verifyGetResource(Bundle mockBundle, String name) {
		Bundle bundle = Mockito.verify(mockBundle);

		bundle.getResource(name);
	}

	private void _verifyGetResources(Bundle mockBundle, String name)
		throws IOException {

		Bundle bundle = Mockito.verify(mockBundle);

		bundle.getResources(name);
	}

	private static final String _NAME = "name";

	@Mock
	private Bundle _bundle1;

	@Mock
	private Bundle _bundle2;

	@Mock
	private Enumeration<URL> _enumeration;

	@Mock
	private URL _url;

}