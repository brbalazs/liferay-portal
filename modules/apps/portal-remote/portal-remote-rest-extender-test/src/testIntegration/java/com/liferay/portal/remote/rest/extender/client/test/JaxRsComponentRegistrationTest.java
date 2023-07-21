/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.rest.extender.client.test;

import com.liferay.portal.kernel.util.StringUtil;

import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class JaxRsComponentRegistrationTest {

	@Test
	public void testIsRegistered() throws Exception {
		URL url = new URL(_url, "/o/rest-test/testApp/sayHello");

		Assert.assertEquals("Hello.", StringUtil.read(url.openStream()));
	}

	@ArquillianResource
	private URL _url;

}