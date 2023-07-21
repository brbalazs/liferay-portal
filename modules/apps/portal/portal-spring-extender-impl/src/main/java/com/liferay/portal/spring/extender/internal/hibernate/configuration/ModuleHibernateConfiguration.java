/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.hibernate.configuration;

import com.liferay.portal.spring.hibernate.PortletHibernateConfiguration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Miguel Pastor
 */
public class ModuleHibernateConfiguration
	extends PortletHibernateConfiguration implements ApplicationContextAware {

	public ModuleHibernateConfiguration() {
		this(null);
	}

	public ModuleHibernateConfiguration(ClassLoader classLoader) {
		_classLoader = classLoader;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {

		_classLoader = applicationContext.getClassLoader();
	}

	@Override
	protected ClassLoader getConfigurationClassLoader() {
		return _classLoader;
	}

	@Override
	protected String[] getConfigurationResources() {
		return new String[] {"META-INF/module-hbm.xml"};
	}

	private ClassLoader _classLoader;

}