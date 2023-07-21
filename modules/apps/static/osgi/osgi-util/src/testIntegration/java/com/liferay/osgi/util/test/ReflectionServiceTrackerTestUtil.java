/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.util.test;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Carlos Sierra Andrés
 */
public class ReflectionServiceTrackerTestUtil {

	protected static <T> ServiceRegistration<T> registerServiceWithRanking(
		BundleContext bundleContext, Class<T> clazz, T service, int ranking) {

		Dictionary<String, Integer> properties = new Hashtable<>();

		properties.put("service.ranking", ranking);

		return bundleContext.registerService(clazz, service, properties);
	}

}