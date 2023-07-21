/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.saml.opensaml.integration.internal.util;

import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.saml.opensaml.integration.internal.bootstrap.OpenSamlBootstrap;

import java.io.InputStream;

import net.shibboleth.utilities.java.support.xml.BasicParserPool;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mika Koivisto
 */
public class MetadataUtilTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		Thread currentThread = Thread.currentThread();

		ClassLoaderPool.register(
			"saml-portlet", currentThread.getContextClassLoader());

		PortletClassLoaderUtil.setServletContextName("saml-portlet");

		OpenSamlBootstrap.bootstrap();

		_metadataUtil = new MetadataUtilImpl();

		BasicParserPool parserPool = new BasicParserPool();

		parserPool.initialize();

		_metadataUtil.parserPool = parserPool;
	}

	@AfterClass
	public static void tearDownClass() {
		ClassLoaderPool.unregister("saml-portlet");
	}

	@Test
	public void testParseEntitiesDescriptor() throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/entities-descriptor.xml");

		String metadata = _metadataUtil.parseMetadataXml(
			inputStream, "https://saml.liferay.com/shibboleth");

		Assert.assertNotNull(metadata);
	}

	@Test
	public void testParseEntityDescriptor() throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/entity-descriptor.xml");

		String metadata = _metadataUtil.parseMetadataXml(
			inputStream, "liferaysamlidpdemo");

		Assert.assertNotNull(metadata);
	}

	private static MetadataUtilImpl _metadataUtil;

}