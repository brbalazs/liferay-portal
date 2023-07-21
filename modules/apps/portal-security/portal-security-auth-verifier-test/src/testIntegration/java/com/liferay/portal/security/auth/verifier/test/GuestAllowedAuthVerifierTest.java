/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.verifier.test;

import com.liferay.arquillian.deploymentscenario.annotations.BndFile;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@BndFile("bnd-guest-allowed.bnd")
@RunAsClient
@RunWith(Arquillian.class)
public class GuestAllowedAuthVerifierTest {

	@Test
	public void testAllowGuest() throws Exception {
		URL url = new URL(
			_url, "/o/auth-verifier-guest-allowed-false-test/guestAllowed");

		try (InputStream inputStream = url.openStream()) {
			Assert.fail();
		}
		catch (IOException ioe) {
			String message = ioe.getMessage();

			Assert.assertTrue(
				message.startsWith("Server returned HTTP response code: 403"));
		}

		url = new URL(
			_url, "/o/auth-verifier-guest-allowed-true-test/guestAllowed");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("guest-allowed", StringUtil.read(inputStream));
		}

		url = new URL(
			_url, "/o/auth-verifier-guest-allowed-default-test/guestAllowed");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("guest-allowed", StringUtil.read(inputStream));
		}
	}

	@ArquillianResource
	private URL _url;

}