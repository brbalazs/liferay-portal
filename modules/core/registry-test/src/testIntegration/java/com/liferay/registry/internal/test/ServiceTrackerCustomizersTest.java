/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.registry.internal.test;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerCustomizers;
import com.liferay.registry.collections.ServiceTrackerMap;

import java.util.Hashtable;
import java.util.Map;

import org.jboss.arquillian.junit.Arquillian;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@RunWith(Arquillian.class)
public class ServiceTrackerCustomizersTest {

	@Test
	public void testServiceWrapper() {
		ServiceTrackerMap
			<String, ServiceTrackerCustomizers.ServiceWrapper<TrackedOne>>
				serviceTrackerMap =
					ServiceTrackerCollections.openSingleValueMap(
						TrackedOne.class, "target",
						ServiceTrackerCustomizers.<TrackedOne>serviceWrapper());

		try {
			Map<String, Object> properties = new Hashtable<>();

			properties.put("property", "aProperty");
			properties.put("target", "aTarget");

			TrackedOne trackedOne = new TrackedOne();

			Registry registry = RegistryUtil.getRegistry();

			ServiceRegistration<TrackedOne> serviceRegistration =
				registry.registerService(
					TrackedOne.class, trackedOne, properties);

			ServiceTrackerCustomizers.ServiceWrapper<TrackedOne>
				serviceWrapper = serviceTrackerMap.getService("aTarget");

			Assert.assertEquals(trackedOne, serviceWrapper.getService());

			Map<String, Object> serviceWrapperProperties =
				serviceWrapper.getProperties();

			Assert.assertTrue(serviceWrapperProperties.containsKey("property"));
			Assert.assertTrue(serviceWrapperProperties.containsKey("target"));
			Assert.assertEquals(
				"aProperty", serviceWrapperProperties.get("property"));
			Assert.assertEquals(
				"aTarget", serviceWrapperProperties.get("target"));

			serviceRegistration.unregister();
		}
		finally {
			serviceTrackerMap.close();
		}
	}

	private static class TrackedOne {
	}

}