/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.hibernate.session;

import com.liferay.portal.dao.orm.hibernate.PortletSessionFactoryImpl;
import com.liferay.portal.spring.extender.internal.context.ModuleApplicationContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Miguel Pastor
 */
public class ModuleSessionFactory
	extends PortletSessionFactoryImpl implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ModuleApplicationContext moduleApplicationContext =
			(ModuleApplicationContext)applicationContext;

		BundleContext bundleContext =
			moduleApplicationContext.getBundleContext();

		Bundle bundle = bundleContext.getBundle();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		ClassLoader classLoader = bundleWiring.getClassLoader();

		setSessionFactoryClassLoader(classLoader);
	}

}