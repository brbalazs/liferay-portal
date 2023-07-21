/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.soap.extender.test;

import com.liferay.arquillian.deploymentscenario.annotations.BndFile;
import com.liferay.portal.remote.soap.extender.test.internal.service.Greeter;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@BndFile("bnd-api.bnd")
@RunAsClient
@RunWith(Arquillian.class)
public class JaxWsApiRegistrationTest {

	@Test
	public void testGreeter() throws Exception {
		URL url = new URL(_url, "/o/soap-test/greeterApi?wsdl");

		QName qName = new QName(
			"http://service.internal.test.extender.soap.remote.portal." +
				"liferay.com/",
			"GreeterImplService");

		Service service = Service.create(url, qName);

		Greeter greeter = service.getPort(Greeter.class);

		Assert.assertEquals("Greetings.", greeter.greet());
	}

	@ArquillianResource
	private URL _url;

}