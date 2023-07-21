/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rafael Praxedes
 */
@RunWith(Arquillian.class)
public class DDMDataProviderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		setUpDDMDataProviderTracker();
		setUpTestDDMDataProvider();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetDDMDataProviderByInstanceId() {
		DDMDataProvider testDataProvider =
			_ddmDataProviderTracker.getDDMDataProviderByInstanceId("test");

		Assert.assertNotNull(testDataProvider);
	}

	@Test
	public void testInvokeDataProvider() throws Exception {
		DDMDataProvider testDataProvider =
			_ddmDataProviderTracker.getDDMDataProviderByInstanceId("test");

		DDMDataProviderRequest ddmDataProviderRequest =
			new DDMDataProviderRequest(null, null);

		DDMDataProviderResponse ddmDataProviderResponse =
			testDataProvider.getData(ddmDataProviderRequest);

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get("Default-Output");

		Assert.assertNotNull(ddmDataProviderResponseOutput);

		List<KeyValuePair> data = ddmDataProviderResponseOutput.getValue(
			List.class);

		Assert.assertEquals(data.toString(), 2, data.size());
	}

	protected static void setUpDDMDataProviderTracker() {
		Registry registry = RegistryUtil.getRegistry();

		_ddmDataProviderTracker = registry.getService(
			DDMDataProviderTracker.class);
	}

	protected static void setUpTestDDMDataProvider() {
		Map<String, Object> properties = new HashMap<>();

		properties.put("ddm.data.provider.instance.id", "test");

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			DDMDataProvider.class, new DDMTestDataProvider(), properties);
	}

	private static DDMDataProviderTracker _ddmDataProviderTracker;
	private static ServiceRegistration<DDMDataProvider> _serviceRegistration;

	private static class DDMTestDataProvider implements DDMDataProvider {

		@Override
		public List<KeyValuePair> getData(
				DDMDataProviderContext ddmDataProviderContext)
			throws DDMDataProviderException {

			return ListUtil.fromArray(
				new KeyValuePair("1", "A"), new KeyValuePair("2", "B"));
		}

		@Override
		public Class<?> getSettings() {
			return null;
		}

	}

}