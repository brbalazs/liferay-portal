/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.context;

import com.liferay.portal.spring.bean.LiferayBeanFactory;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * @author Miguel Pastor
 */
public class ModuleApplicationContext extends ClassPathXmlApplicationContext {

	public ModuleApplicationContext(
		Bundle bundle, ClassLoader classLoader, String[] configLocations) {

		super(configLocations, false, null);

		_bundle = bundle;

		setClassLoader(classLoader);
	}

	public BundleContext getBundleContext() {
		return _bundle.getBundleContext();
	}

	@Override
	public Resource[] getResources(String locationPattern) {
		Enumeration<URL> enumeration = _bundle.findEntries(
			locationPattern, "*.xml", false);

		List<Resource> resources = new ArrayList<>();

		while (enumeration.hasMoreElements()) {
			resources.add(new UrlResource(enumeration.nextElement()));
		}

		return resources.toArray(new Resource[0]);
	}

	@Override
	protected DefaultListableBeanFactory createBeanFactory() {
		return new LiferayBeanFactory(getInternalParentBeanFactory());
	}

	private final Bundle _bundle;

}