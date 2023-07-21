/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.verifier.test.internal.activator;

import com.liferay.portal.kernel.util.HashMapDictionary;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Dictionary;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Marta Medio
 */
public class GuestAllowedAuthVerifierBundleActivator
	extends BaseAuthVerifierBundleActivator {

	@Override
	public void doStart(BundleContext bundleContext) {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put(
			JaxrsWhiteboardConstants.JAX_RS_NAME, "guest-no-allowed");
		properties.put("auth.verifier.guest.allowed", false);

		registerServletContextHelper(
			"auth-verifier-guest-allowed-false-test", properties);

		properties = new HashMapDictionary<>();

		properties.put(JaxrsWhiteboardConstants.JAX_RS_NAME, "guest-allowed");
		properties.put("auth.verifier.guest.allowed", true);

		registerServletContextHelper(
			"auth-verifier-guest-allowed-true-test", properties);

		properties = new HashMapDictionary<>();

		properties.put(JaxrsWhiteboardConstants.JAX_RS_NAME, "guest-default");

		registerServletContextHelper(
			"auth-verifier-guest-allowed-default-test", properties);

		properties = new HashMapDictionary<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			"cxf-servlet");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN,
			"/guestAllowed");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"(auth-verifier-guest-allowed-test-servlet-context-helper=true)");

		registerServlet(properties, GuestAllowedHttpServlet::new);
	}

	public class GuestAllowedHttpServlet extends HttpServlet {

		@Override
		protected void doGet(
				HttpServletRequest request, HttpServletResponse response)
			throws IOException {

			PrintWriter printWriter = response.getWriter();

			printWriter.write("guest-allowed");
		}

	}

	@Override
	protected void registerServletContextHelper(
		String servletContextName, Dictionary<String, Object> properties) {

		properties.put(
			"auth-verifier-guest-allowed-test-servlet-context-helper", true);

		super.registerServletContextHelper(servletContextName, properties);
	}

}