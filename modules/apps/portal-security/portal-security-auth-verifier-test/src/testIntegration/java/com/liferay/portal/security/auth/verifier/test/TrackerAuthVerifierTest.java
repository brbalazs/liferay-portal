/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.verifier.test;

import com.liferay.arquillian.deploymentscenario.annotations.BndFile;
import com.liferay.portal.kernel.util.StringUtil;

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
@BndFile("bnd-tracker.bnd")
@RunAsClient
@RunWith(Arquillian.class)
public class TrackerAuthVerifierTest {

	@Test
	public void testRemoteAccess() throws Exception {
		URL url = new URL(
			_url,
			"/o/auth-verifier-filter-tracker-remote-access-test/remoteAccess");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("true", StringUtil.read(inputStream));
		}
	}

	@Test
	public void testRemoteUser() throws Exception {
		URL url = new URL(
			_url, "/o/auth-verifier-filter-tracker-enabled-test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals(
				"remote-user-set", StringUtil.read(inputStream));
		}

		url = new URL(
			_url, "/o/auth-verifier-filter-tracker-disabled-test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("no-remote-user", StringUtil.read(inputStream));
		}

		url = new URL(
			_url, "/o/auth-verifier-filter-tracker-default-test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals(
				"remote-user-set", StringUtil.read(inputStream));
		}
	}

	@ArquillianResource
	private URL _url;

}