/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.select.internal;

import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.LocaleUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Renato Rego
 */
public class SelectDDMFormFieldValueAccessorTest {

	@Test
	public void testGetSelectValue() throws Exception {
		JSONArray expectedJSONArray = createJSONArray("value 1");

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Select", new UnlocalizedValue(expectedJSONArray.toString()));

		SelectDDMFormFieldValueAccessor selectDDMFormFieldValueAccessor =
			new SelectDDMFormFieldValueAccessor();

		selectDDMFormFieldValueAccessor.jsonFactory = _jsonFactory;

		JSONArray actualJSONArray = selectDDMFormFieldValueAccessor.getValue(
			ddmFormFieldValue, LocaleUtil.US);

		Assert.assertEquals(
			expectedJSONArray.toString(), actualJSONArray.toString());
	}

	protected JSONArray createJSONArray(String... strings) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (String string : strings) {
			jsonArray.put(string);
		}

		return jsonArray;
	}

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}