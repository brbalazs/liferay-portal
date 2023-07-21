/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.verifier.test.internal.activator;

import com.liferay.portal.kernel.security.access.control.AccessControlThreadLocal;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Validator;

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
public class TrackerAuthVerifierBundleActivator
	extends BaseAuthVerifierBundleActivator {

	@Override
	public void doStart(BundleContext bundleContext) {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("auth.verifier.guest.allowed", true);
		properties.put(JaxrsWhiteboardConstants.JAX_RS_NAME, "filter-enabled");

		registerServletContextHelper(
			"auth-verifier-filter-tracker-enabled-test", properties);

		properties = new HashMapDictionary<>();

		properties.put("liferay.auth.verifier", false);

		registerServletContextHelper(
			"auth-verifier-filter-tracker-disabled-test", properties);

		properties = new HashMapDictionary<>();

		registerServletContextHelper(
			"auth-verifier-filter-tracker-default-test", properties);

		properties = new HashMapDictionary<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"(auth-verifier-tracker-test-servlet-context-helper=true)");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			"cxf-servlet");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN,
			"/remoteUser");

		registerServlet(properties, RemoteUserHttpServlet::new);

		properties = new HashMapDictionary<>();

		properties.put(JaxrsWhiteboardConstants.JAX_RS_NAME, "filter-enabled");

		registerServletContextHelper(
			"auth-verifier-filter-tracker-remote-access-test", properties);

		properties = new HashMapDictionary<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			"cxf-servlet");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN,
			"/remoteAccess");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"auth-verifier-filter-tracker-remote-access-test");

		registerServlet(properties, RemoteAccessHttpServlet::new);
	}

	public class RemoteAccessHttpServlet extends HttpServlet {

		@Override
		protected void doGet(
				HttpServletRequest request, HttpServletResponse response)
			throws IOException {

			PrintWriter printWriter = response.getWriter();

			printWriter.write(
				String.valueOf(AccessControlThreadLocal.isRemoteAccess()));
		}

	}

	public class RemoteUserHttpServlet extends HttpServlet {

		@Override
		protected void doGet(
				HttpServletRequest request, HttpServletResponse response)
			throws IOException {

			PrintWriter printWriter = response.getWriter();

			String remoteUser = request.getRemoteUser();

			if (Validator.isNull(remoteUser)) {
				printWriter.write("no-remote-user");
			}
			else {
				printWriter.write("remote-user-set");
			}
		}

	}

	@Override
	protected void registerServletContextHelper(
		String servletContextName, Dictionary<String, Object> properties) {

		properties.put(
			"auth-verifier-tracker-test-servlet-context-helper", true);

		super.registerServletContextHelper(servletContextName, properties);
	}

}