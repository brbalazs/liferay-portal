/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.data.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.data.provider.configuration.DDMDataProviderConfiguration;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTestUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheHelperUtil;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcellus Tavares
 */
@RunWith(Arquillian.class)
public class DDMRESTDataProviderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		ConfigurationTestUtil.saveConfiguration(
			DDMDataProviderConfiguration.class.getName(),
			new HashMapDictionary() {
				{
					put("accessLocalNetwork", true);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		ConfigurationTestUtil.saveConfiguration(
			DDMDataProviderConfiguration.class.getName(),
			new HashMapDictionary() {
				{
					put("accessLocalNetwork", false);
				}
			});
	}

	@Test
	public void testGetData() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, null, outputParameterId,
				"nameCurrentValue;name", "list", null, null,
				_GET_COUNTRIES_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null, null,
					null, null, null));

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertTrue(
			keyValuePairs.containsAll(
				ListUtil.fromArray(
					new KeyValuePair("france", "France"),
					new KeyValuePair("spain", "Spain"),
					new KeyValuePair("united-states", "United States"),
					new KeyValuePair("brazil", "Brazil"))));
	}

	@Test
	public void testGetDataWithCache() throws Exception {
		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				true, false, StringPool.BLANK, "name",
				StringUtil.randomString(), "nameCurrentValue;name", "list",
				null, null, _GET_COUNTRY_BY_NAME_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderRequest ddmDataProviderRequest =
			_createDDMDataProviderRequest(
				ddmDataProviderId, ddmDataProviderDDMFormValues, null, "name",
				"brazil", null, null);

		_ddmDataProvider.getData(ddmDataProviderRequest);

		String cacheKey = StringBundler.concat(
			ddmDataProviderRequest.getDDMDataProviderInstanceId(),
			StringPool.AT, _GET_COUNTRY_BY_NAME_URL, "?name=brazil");

		Class<?> clazz = _ddmDataProvider.getClass();

		PortalCache<String, DDMDataProviderResponse> portalCache =
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.MULTI_VM, clazz.getName());

		Assert.assertNotNull(portalCache.get(cacheKey));

		portalCache.remove(cacheKey);
	}

	@Test
	public void testGetDataWithFilterParameter() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, true, "name", null, outputParameterId,
				"nameCurrentValue;name", "list", null, null,
				_GET_COUNTRY_BY_NAME_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, "brazil",
					null, null, null, null));

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertEquals(keyValuePairs.toString(), 1, keyValuePairs.size());

		KeyValuePair keyValuePair = keyValuePairs.get(0);

		Assert.assertEquals("brazil", keyValuePair.getKey());
		Assert.assertEquals("Brazil", keyValuePair.getValue());
	}

	@Test
	public void testGetDataWithInputParameters() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, "name", outputParameterId,
				"nameCurrentValue", "list", null, null,
				_GET_COUNTRY_BY_NAME_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null,
					"name", "brazil", null, null));

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertEquals(keyValuePairs.toString(), 1, keyValuePairs.size());

		KeyValuePair keyValuePair = keyValuePairs.get(0);

		Assert.assertEquals("Brazil", keyValuePair.getKey());
		Assert.assertEquals("Brazil", keyValuePair.getValue());
	}

	@Test
	public void testGetDataWithInputParametersInURL() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, "name", outputParameterId,
				"nameCurrentValue", "list", null, null,
				_GET_COUNTRY_BY_NAME_URL + "?name={name}");

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null,
					"name", "brazil", null, null));

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertEquals(keyValuePairs.toString(), 1, keyValuePairs.size());

		KeyValuePair keyValuePair = keyValuePairs.get(0);

		Assert.assertEquals("Brazil", keyValuePair.getKey());
		Assert.assertEquals("Brazil", keyValuePair.getValue());
	}

	@Test
	public void testGetDataWithNumberOutput() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, null, null, outputParameterId, "$.length()",
				"number", null, null, _GET_COUNTRIES_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null, null,
					null, null, null));

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		Assert.assertEquals(
			249, ddmDataProviderResponseOutput.getValue(Number.class));
	}

	@Test
	public void testGetDataWithoutOutputParameters() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, null, outputParameterId, null,
				null, null, null, _GET_COUNTRIES_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null, null,
					null, null, null));

		Assert.assertEquals(
			DDMDataProviderResponse.Status.OK,
			ddmDataProviderResponse.getStatus());

		Assert.assertEquals(
			ddmDataProviderResponse.toString(), 0,
			ddmDataProviderResponse.size());
	}

	@Test
	public void testGetDataWithPagination() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, null, outputParameterId,
				"nameCurrentValue", "list", "7", "2", _GET_COUNTRIES_URL);

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null, null,
					null, "7", "2"));

		Assert.assertEquals(
			DDMDataProviderResponse.Status.OK,
			ddmDataProviderResponse.getStatus());

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertEquals(keyValuePairs.toString(), 5, keyValuePairs.size());
	}

	@Test
	public void testGetDataWithTextOutput() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, "name", outputParameterId,
				"$.nameCurrentValue", "text", null, null,
				_GET_COUNTRY_BY_NAME_URL + "?name={name}");

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null,
					"name", "brazil", null, null));

		Assert.assertNotNull(ddmDataProviderResponse);

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		Assert.assertEquals(
			"Brazil", ddmDataProviderResponseOutput.getValue(String.class));
	}

	@Test
	public void testGetDataWithWebServiceError() throws Exception {
		String outputParameterId = StringUtil.randomString();

		DDMFormValues ddmDataProviderDDMFormValues =
			_createDDMDataProviderDDMFormValues(
				false, false, StringPool.BLANK, null, outputParameterId,
				"nameCurrentValue;name", "list", null, null,
				"http://localhost");

		long ddmDataProviderId = _addDDMDataProviderInstance(
			ddmDataProviderDDMFormValues);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmDataProvider.getData(
				_createDDMDataProviderRequest(
					ddmDataProviderId, ddmDataProviderDDMFormValues, null, null,
					null, null, null));

		Assert.assertEquals(
			DDMDataProviderResponse.Status.SERVICE_UNAVAILABLE,
			ddmDataProviderResponse.getStatus());

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<KeyValuePair> keyValuePairs =
			ddmDataProviderResponseOutput.getValue(List.class);

		Assert.assertEquals(keyValuePairs.toString(), 0, keyValuePairs.size());
	}

	private long _addDDMDataProviderInstance(DDMFormValues ddmFormValues)
		throws Exception {

		DDMDataProviderInstance ddmDataProviderInstance =
			_ddmDataProviderInstanceLocalService.addDataProviderInstance(
				TestPropsValues.getUserId(), TestPropsValues.getGroupId(),
				HashMapBuilder.put(
					LocaleUtil.US, "Test"
				).build(),
				null, ddmFormValues, "rest", new ServiceContext());

		return ddmDataProviderInstance.getDataProviderInstanceId();
	}

	private DDMFormValues _createDDMDataProviderDDMFormValues(
		boolean cacheable, boolean filterable, String filterParameterName,
		String inputParameterName, String outputParameterId,
		String outputParameterPath, String outputParameterType,
		String paginationEnd, String paginationStart, String url) {

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			DDMFormFactory.create(_ddmDataProvider.getSettings()));

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"cacheable", String.valueOf(cacheable)));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"filterable", String.valueOf(filterable)));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"filterParameterName", filterParameterName));

		if (Validator.isNotNull(inputParameterName)) {
			DDMFormFieldValue inputParameters =
				DDMFormValuesTestUtil.createDDMFormFieldValue(
					"inputParameters", null);

			ddmFormValues.addDDMFormFieldValue(inputParameters);

			inputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"inputParameterLabel", "input"));
			inputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"inputParameterName", inputParameterName));
			inputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"inputParameterRequired", Boolean.FALSE.toString()));
			inputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"inputParameterType", "[\"text\"]"));
		}

		if (Validator.isNotNull(outputParameterId) &&
			Validator.isNotNull(outputParameterPath) &&
			Validator.isNotNull(outputParameterType)) {

			DDMFormFieldValue outputParameters =
				DDMFormValuesTestUtil.createDDMFormFieldValue(
					"outputParameters", null);

			ddmFormValues.addDDMFormFieldValue(outputParameters);

			outputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"outputParameterId", outputParameterId));
			outputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"outputParameterName", "output"));
			outputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"outputParameterPath", outputParameterPath));
			outputParameters.addNestedDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"outputParameterType",
					"[\"" + outputParameterType + "\"]"));
		}

		if (Validator.isNotNull(paginationEnd) &&
			Validator.isNotNull(paginationStart)) {

			ddmFormValues.addDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"pagination", Boolean.TRUE.toString()));
			ddmFormValues.addDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"paginationEnd", paginationEnd));
			ddmFormValues.addDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					"paginationStart", paginationStart));
		}

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"password", "test"));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"timeout", "1000"));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"url", url));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"username", "test@liferay.com"));

		return ddmFormValues;
	}

	private DDMDataProviderRequest _createDDMDataProviderRequest(
		long ddmDataProviderId, DDMFormValues ddmFormValues,
		String filterParameterValue, String inputParameterName,
		String inputParameterValue, String paginationEnd,
		String paginationStart) {

		DDMDataProviderRequest ddmDataProviderRequest =
			new DDMDataProviderRequest(String.valueOf(ddmDataProviderId), null);

		ddmDataProviderRequest.setDDMDataProviderContext(
			new DDMDataProviderContext(ddmFormValues));

		if (filterParameterValue != null) {
			ddmDataProviderRequest.queryString(
				"filterParameterValue", filterParameterValue);
		}

		if (Validator.isNotNull(inputParameterName)) {
			ddmDataProviderRequest.queryString(
				inputParameterName, GetterUtil.getString(inputParameterValue));
		}

		if (Validator.isNotNull(paginationEnd) &&
			Validator.isNotNull(paginationStart)) {

			ddmDataProviderRequest.queryString("paginationEnd", paginationEnd);
			ddmDataProviderRequest.queryString(
				"paginationStart", paginationStart);
		}

		return ddmDataProviderRequest;
	}

	private static final String _GET_COUNTRIES_URL =
		"http://localhost:8080/api/jsonws/country/get-countries";

	private static final String _GET_COUNTRY_BY_NAME_URL =
		"http://localhost:8080/api/jsonws/country/get-country-by-name";

	@Inject(
		filter = "ddm.data.provider.type=rest", type = DDMDataProvider.class
	)
	private DDMDataProvider _ddmDataProvider;

	@Inject(type = DDMDataProviderInstanceLocalService.class)
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;

	@Inject(type = RoleLocalService.class)
	private RoleLocalService _roleLocalService;

	@Inject(type = UserLocalService.class)
	private UserLocalService _userLocalService;

}