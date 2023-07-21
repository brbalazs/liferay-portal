/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.osgi.web.servlet.context.helper.internal;

import com.liferay.portal.osgi.web.servlet.JSPServletFactory;
import com.liferay.portal.osgi.web.servlet.context.helper.ServletContextHelperRegistration;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.xml.parsers.SAXParserFactory;

import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Raymond Augé
 */
public class ServletContextHelperRegistrationServiceFactory
	implements ServiceFactory<ServletContextHelperRegistration> {

	public ServletContextHelperRegistrationServiceFactory(
		JSPServletFactory jspServletFactory, SAXParserFactory saxParserFactory,
		Logger logger, Map<String, Object> properties,
		ExecutorService executorService) {

		_jspServletFactory = jspServletFactory;
		_saxParserFactory = saxParserFactory;
		_logger = logger;
		_properties = properties;
		_executorService = executorService;
	}

	@Override
	public ServletContextHelperRegistration getService(
		Bundle bundle,
		ServiceRegistration<ServletContextHelperRegistration> registration) {

		return new ServletContextHelperRegistrationImpl(
			bundle, _jspServletFactory, _saxParserFactory, _logger, _properties,
			_executorService);
	}

	@Override
	public void ungetService(
		Bundle bundle,
		ServiceRegistration<ServletContextHelperRegistration> registration,
		ServletContextHelperRegistration servletContextHelperRegistration) {

		servletContextHelperRegistration.close();
	}

	private final ExecutorService _executorService;
	private final JSPServletFactory _jspServletFactory;
	private final Logger _logger;
	private final Map<String, Object> _properties;
	private final SAXParserFactory _saxParserFactory;

}