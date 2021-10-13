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

package com.liferay.dynamic.data.mapping.data.provider.internal.rest;

import com.jayway.jsonpath.DocumentContext;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.dynamic.data.mapping.util.DDMFormInstanceFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.HtmlImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Rafael Praxedes
 */
@PrepareForTest(ResourceBundleUtil.class)
@RunWith(PowerMockRunner.class)
public class DDMRESTDataProviderTest {

	@Before
	public void setUp() throws Exception {
		setUpHtmlUtil();
		setUpLanguageUtil();
		setUpPortalUtil();
		setUpResourceBundleUtil();
		setUpJSONUtil();
	}

	@Test
	public void testBuildURL() {
		String url = _ddmRESTDataProvider.buildURL(
			createDDMDataProviderRequest(),
			createDDMRESTDataProviderSettings());

		Assert.assertEquals(
			"http://someservice.com/api/countries/1/regions", url);
	}

	@Test
	public void testGetPathParameters() {
		Map<String, String> pathParameters =
			_ddmRESTDataProvider.getPathParameters(
				createDDMDataProviderRequest(),
				createDDMRESTDataProviderSettings());

		Assert.assertEquals(
			pathParameters.toString(), 1, pathParameters.size());
		Assert.assertEquals("1", pathParameters.get("countryId"));
	}

	@Test
	public void testGetQueryParameters() {
		Map<String, String> queryParameters =
			_ddmRESTDataProvider.getQueryParameters(
				createDDMDataProviderRequest(),
				createDDMRESTDataProviderSettings());

		Assert.assertEquals(
			queryParameters.toString(), 1, queryParameters.size());
		Assert.assertEquals("Region", queryParameters.get("regionName"));
	}

	@Test
	public void testListWithVariousTypes() {
		DocumentContext documentContext = PowerMockito.mock(
			DocumentContext.class);

		DDMDataProviderRequest ddmDataProviderRequest =
			new DDMDataProviderRequest(null, null);

		String outputParameterId = StringUtil.randomString();

		DDMRESTDataProviderSettings ddmRESTDataProviderSettings =
			_createSettingsWithOutputParameter(
				outputParameterId, "list output", false, "value;key", "list");

		PowerMockito.when(
			documentContext.read(".value", List.class)
		).thenReturn(
			new ArrayList() {
				{
					add("Moreno");
					add(42);
					add(3.14);
				}
			}
		);

		PowerMockito.when(
			documentContext.read(".key")
		).thenReturn(
			new ArrayList() {
				{
					add("5");
					add("6");
					add("7");
				}
			}
		);

		DDMDataProviderResponse ddmDataProviderResponse =
			_ddmRESTDataProvider.createDDMDataProviderResponse(
				documentContext, ddmDataProviderRequest,
				ddmRESTDataProviderSettings);

		DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
			ddmDataProviderResponse.get(outputParameterId);

		List<?> value = ddmDataProviderResponseOutput.getValue(List.class);

		List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>() {
			{
				add(new KeyValuePair("5", "Moreno"));
				add(new KeyValuePair("6", "42"));
				add(new KeyValuePair("7", "3.14"));
			}
		};

		Assert.assertEquals(keyValuePairs.toString(), keyValuePairs, value);
	}

	protected DDMDataProviderRequest createDDMDataProviderRequest() {
		DDMDataProviderRequest ddmDataProviderRequest =
			new DDMDataProviderRequest(StringPool.BLANK, null);

		ddmDataProviderRequest.queryString("countryId", "1");

		ddmDataProviderRequest.queryString("regionName", "Region");

		return ddmDataProviderRequest;
	}

	protected DDMRESTDataProviderSettings createDDMRESTDataProviderSettings() {
		DDMForm ddmForm = DDMFormFactory.create(
			DDMRESTDataProviderSettings.class);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"cacheable", Boolean.FALSE.toString()));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"filterable", Boolean.FALSE.toString()));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"filterParameterName", StringPool.BLANK));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"password", "1234"));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"url",
				"http://someservice.com/api/countries/{countryId}/regions"));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"username", "Joe"));

		DDMFormFieldValue inputParameters =
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameters", StringPool.BLANK);

		ddmFormValues.addDDMFormFieldValue(inputParameters);

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterLabel", "Country Id"));

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterName", "countryId"));

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterType", "[\"number\"]"));

		inputParameters =
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameters", StringPool.BLANK);

		ddmFormValues.addDDMFormFieldValue(inputParameters);

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterLabel", "Region Name"));

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterName", "regionName"));

		inputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"inputParameterType", "[\"text\"]"));

		return DDMFormInstanceFactory.create(
			DDMRESTDataProviderSettings.class, ddmFormValues);
	}

	protected void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	protected void setUpJSONUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		Language language = PowerMockito.mock(Language.class);

		languageUtil.setLanguage(language);
	}

	protected void setUpPortalUtil() {
		PortalUtil portalUtil = new PortalUtil();

		Portal portal = PowerMockito.mock(Portal.class);

		ResourceBundle resourceBundle = PowerMockito.mock(ResourceBundle.class);

		PowerMockito.when(
			portal.getResourceBundle(Matchers.any(Locale.class))
		).thenReturn(
			resourceBundle
		);

		portalUtil.setPortal(portal);
	}

	protected void setUpResourceBundleUtil() {
		PowerMockito.mockStatic(ResourceBundleUtil.class);

		PowerMockito.when(
			ResourceBundleUtil.getBundle(
				Matchers.anyString(), Matchers.any(Locale.class),
				Matchers.any(ClassLoader.class))
		).thenReturn(
			ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE
		);
	}

	private DDMRESTDataProviderSettings _createSettingsWithOutputParameter(
		String id, String name, boolean pagination, String path, String type) {

		DDMForm ddmForm = DDMFormFactory.create(
			DDMRESTDataProviderSettings.class);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"url", "http://someservice.com/api"));

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"pagination", Boolean.toString(pagination)));

		DDMFormFieldValue outputParameters =
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"outputParameters", StringPool.BLANK);

		ddmFormValues.addDDMFormFieldValue(outputParameters);

		outputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"outputParameterName", name));

		outputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"outputParameterPath", path));

		outputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"outputParameterType", String.format("[\"%s\"]", type)));

		outputParameters.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"outputParameterId", id));

		return DDMFormInstanceFactory.create(
			DDMRESTDataProviderSettings.class, ddmFormValues);
	}

	private final DDMRESTDataProvider _ddmRESTDataProvider =
		new DDMRESTDataProvider();

}