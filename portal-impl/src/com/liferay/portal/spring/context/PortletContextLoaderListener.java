/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.context;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletContextClassLoaderPool;
import com.liferay.portal.kernel.util.MethodKey;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Brian Wing Shun Chan
 * @see    PortletApplicationContext
 */
public class PortletContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext servletContext = servletContextEvent.getServletContext();

		ClassLoader classLoader = ServletContextClassLoaderPool.getClassLoader(
			servletContext.getServletContextName());

		if (classLoader == null) {
			throw new IllegalStateException(
				"Unable to find the class loader for servlet context " +
					servletContext.getServletContextName());
		}

		try {
			Class<?> beanLocatorUtilClass = Class.forName(
				"com.liferay.util.bean.PortletBeanLocatorUtil", true,
				classLoader);

			Method setBeanLocatorMethod = beanLocatorUtilClass.getMethod(
				"setBeanLocator", new Class<?>[] {BeanLocator.class});

			setBeanLocatorMethod.invoke(
				beanLocatorUtilClass, new Object[] {null});

			PortletBeanLocatorUtil.setBeanLocator(
				servletContext.getServletContextName(), null);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		super.contextDestroyed(servletContextEvent);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		MethodKey.resetCache();

		ServletContext servletContext = servletContextEvent.getServletContext();

		Object previousApplicationContext = servletContext.getAttribute(
			WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		servletContext.removeAttribute(
			WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		ClassLoader classLoader = ServletContextClassLoaderPool.getClassLoader(
			servletContext.getServletContextName());

		if (classLoader == null) {
			throw new IllegalStateException(
				"Unable to find the class loader for servlet context " +
					servletContext.getServletContextName());
		}

		super.contextInitialized(servletContextEvent);

		PortletBeanFactoryCleaner.readBeans();

		ApplicationContext applicationContext =
			WebApplicationContextUtils.getWebApplicationContext(servletContext);

		BeanLocatorImpl beanLocatorImpl = new BeanLocatorImpl(
			classLoader, applicationContext);

		try {
			Class<?> beanLocatorUtilClass = Class.forName(
				"com.liferay.util.bean.PortletBeanLocatorUtil", true,
				classLoader);

			Method setBeanLocatorMethod = beanLocatorUtilClass.getMethod(
				"setBeanLocator", new Class<?>[] {BeanLocator.class});

			setBeanLocatorMethod.invoke(
				beanLocatorUtilClass, new Object[] {beanLocatorImpl});

			PortletBeanLocatorUtil.setBeanLocator(
				servletContext.getServletContextName(), beanLocatorImpl);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (previousApplicationContext == null) {
			servletContext.removeAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		}
		else {
			servletContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				previousApplicationContext);
		}
	}

	@Override
	protected void customizeContext(
		ServletContext servletContext,
		ConfigurableWebApplicationContext configurableWebApplicationContext) {

		String configLocation = servletContext.getInitParameter(
			_PORTAL_CONFIG_LOCATION_PARAM);

		configurableWebApplicationContext.setConfigLocation(configLocation);

		configurableWebApplicationContext.addBeanFactoryPostProcessor(
			new PortletBeanFactoryPostProcessor());
	}

	@Override
	protected Class<?> determineContextClass(ServletContext servletContext) {
		return PortletApplicationContext.class;
	}

	@Override
	protected ApplicationContext loadParentContext(
		ServletContext servletContext) {

		return super.loadParentContext(servletContext);
	}

	private static final String _PORTAL_CONFIG_LOCATION_PARAM =
		"portalContextConfigLocation";

	private static final Log _log = LogFactoryUtil.getLog(
		PortletContextLoaderListener.class);

}