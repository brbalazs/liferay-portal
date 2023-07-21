/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.product.navigation.taglib.servlet;

import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuCategoryRegistry;
import com.liferay.product.navigation.control.menu.util.ProductNavigationControlMenuEntryRegistry;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 */
@Component(immediate = true, service = {})
public class ServletContextUtil {

	public static final String getContextPath() {
		ServletContext servletContext = _instance._getServletContext();

		return servletContext.getContextPath();
	}

	public static final ProductNavigationControlMenuCategoryRegistry
		getProductNavigationControlMenuCategoryRegistry() {

		return _instance._getProductNavigationControlMenuCategoryRegistry();
	}

	public static final ProductNavigationControlMenuEntryRegistry
		getProductNavigationControlMenuEntryRegistry() {

		return _instance._getProductNavigationControlMenuEntryRegistry();
	}

	public static final ServletContext getServletContext() {
		return _instance._getServletContext();
	}

	@Activate
	protected void activate() {
		_instance = this;
	}

	@Deactivate
	protected void deactivate() {
		_instance = null;
	}

	@Reference(unbind = "-")
	protected void setProductNavigationControlMenuCategoryRegistry(
		ProductNavigationControlMenuCategoryRegistry
			productNavigationControlMenuCategoryRegistry) {

		_productNavigationControlMenuCategoryRegistry =
			productNavigationControlMenuCategoryRegistry;
	}

	@Reference(unbind = "-")
	protected void setProductNavigationControlMenuEntryRegistry(
		ProductNavigationControlMenuEntryRegistry
			productNavigationControlMenuEntryRegistry) {

		_productNavigationControlMenuEntryRegistry =
			productNavigationControlMenuEntryRegistry;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.product.navigation.taglib)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private ProductNavigationControlMenuCategoryRegistry
		_getProductNavigationControlMenuCategoryRegistry() {

		return _productNavigationControlMenuCategoryRegistry;
	}

	private ProductNavigationControlMenuEntryRegistry
		_getProductNavigationControlMenuEntryRegistry() {

		return _productNavigationControlMenuEntryRegistry;
	}

	private ServletContext _getServletContext() {
		return _servletContext;
	}

	private static ServletContextUtil _instance;

	private ProductNavigationControlMenuCategoryRegistry
		_productNavigationControlMenuCategoryRegistry;
	private ProductNavigationControlMenuEntryRegistry
		_productNavigationControlMenuEntryRegistry;
	private ServletContext _servletContext;

}