/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.io.internal;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.io.DDMFormFieldTypesJSONSerializer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Marcellus Tavares
 */
public class DDMFormFieldTypesJSONSerializerTest extends BaseDDMTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setUpDDMFormFieldTypesJSONSerializer();
	}

	@Test
	public void testSerializationWithEmptyParameterList() throws Exception {
		List<DDMFormFieldType> ddmFormFieldTypes = Collections.emptyList();

		String actualJSON = _ddmFormFieldTypesJSONSerializer.serialize(
			ddmFormFieldTypes);

		Assert.assertEquals("[]", actualJSON);
	}

	@Test
	public void testSerializationWithNonemptyParameterList() throws Exception {
		List<DDMFormFieldType> ddmFormFieldTypes = new ArrayList<>();

		DDMFormFieldType ddmFormFieldType = getMockedDDMFormFieldType();

		ddmFormFieldTypes.add(ddmFormFieldType);

		String actualJSON = _ddmFormFieldTypesJSONSerializer.serialize(
			ddmFormFieldTypes);

		JSONAssert.assertEquals(createExpectedJSON(), actualJSON, false);
	}

	protected String createExpectedJSON() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("icon", "my-icon");
		jsonObject.put("javaScriptClass", "myJavaScriptClass");
		jsonObject.put("javaScriptModule", "myJavaScriptModule");
		jsonObject.put("name", "Text");

		jsonArray.put(jsonObject);

		return jsonArray.toString();
	}

	protected DDMFormFieldType getMockedDDMFormFieldType() {
		DDMFormFieldType ddmFormFieldType = mock(DDMFormFieldType.class);

		whenDDMFormFieldTypeGetName(ddmFormFieldType, "Text");

		return ddmFormFieldType;
	}

	protected DDMFormFieldTypeServicesTracker
		getMockedDDMFormFieldTypeServicesTracker() {

		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker = mock(
			DDMFormFieldTypeServicesTracker.class);

		DDMFormFieldRenderer ddmFormFieldRenderer = mock(
			DDMFormFieldRenderer.class);

		when(
			ddmFormFieldTypeServicesTracker.getDDMFormFieldRenderer(
				Matchers.anyString())
		).thenReturn(
			ddmFormFieldRenderer
		);

		Map<String, Object> properties = new HashMap<>();

		properties.put("ddm.form.field.type.icon", "my-icon");
		properties.put(
			"ddm.form.field.type.js.class.name", "myJavaScriptClass");
		properties.put("ddm.form.field.type.js.module", "myJavaScriptModule");

		when(
			ddmFormFieldTypeServicesTracker.getDDMFormFieldTypeProperties(
				Matchers.anyString())
		).thenReturn(
			properties
		);

		return ddmFormFieldTypeServicesTracker;
	}

	protected void setUpDDMFormFieldTypesJSONSerializer() throws Exception {
		Field field = ReflectionUtil.getDeclaredField(
			DDMFormFieldTypesJSONSerializerImpl.class,
			"_ddmFormFieldTypeServicesTracker");

		field.set(
			_ddmFormFieldTypesJSONSerializer,
			getMockedDDMFormFieldTypeServicesTracker());

		field = ReflectionUtil.getDeclaredField(
			DDMFormFieldTypesJSONSerializerImpl.class, "_jsonFactory");

		field.set(_ddmFormFieldTypesJSONSerializer, new JSONFactoryImpl());
	}

	protected void whenDDMFormFieldTypeGetName(
		DDMFormFieldType ddmFormFieldType, String returnName) {

		when(
			ddmFormFieldType.getName()
		).thenReturn(
			returnName
		);
	}

	private final DDMFormFieldTypesJSONSerializer
		_ddmFormFieldTypesJSONSerializer =
			new DDMFormFieldTypesJSONSerializerImpl();

}