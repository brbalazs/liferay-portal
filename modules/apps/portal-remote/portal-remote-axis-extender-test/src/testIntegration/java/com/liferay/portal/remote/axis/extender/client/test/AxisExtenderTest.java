/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.axis.extender.client.test;

import com.liferay.portal.remote.axis.extender.test.service.http.CalcServiceSoap;
import com.liferay.portal.remote.axis.extender.test.service.http.CalcServiceSoapService;
import com.liferay.portal.remote.axis.extender.test.service.http.CalcServiceSoapServiceLocator;

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
public class AxisExtenderTest {

	@Test
	public void testGreeter() throws Exception {
		URL url = new URL(
			_url,
			"/o/com.liferay.portal.remote.axis.extender.test/api/axis" +
				"/CalcService?wsdl");

		CalcServiceSoapService calcServiceSoapService =
			new CalcServiceSoapServiceLocator();

		CalcServiceSoap calcServiceSoap =
			calcServiceSoapService.getCalcServiceSoapPort(url);

		Assert.assertEquals(5, calcServiceSoap.sum(2, 3));
	}

	@ArquillianResource
	private URL _url;

}