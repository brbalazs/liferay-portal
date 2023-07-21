/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.grid.internal;

import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pedro Queiroz
 */
public class GridDDMFormFieldContextHelperTest {

	@Test
	public void testGetOptions() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createOption("Label 1", "value 1"));
		expectedOptions.add(createOption("Label 2", "value 2"));
		expectedOptions.add(createOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	protected DDMFormFieldOptions createDDMFormFieldOptions() {
		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		ddmFormFieldOptions.addOptionLabel("value 1", LocaleUtil.US, "Label 1");
		ddmFormFieldOptions.addOptionLabel("value 2", LocaleUtil.US, "Label 2");
		ddmFormFieldOptions.addOptionLabel("value 3", LocaleUtil.US, "Label 3");

		return ddmFormFieldOptions;
	}

	protected Map<String, String> createOption(String label, String value) {
		Map<String, String> option = new HashMap<>();

		option.put("label", label);
		option.put("value", value);

		return option;
	}

	protected List<Object> getActualOptions(
		DDMFormFieldOptions ddmFormFieldOptions, Locale locale) {

		GridDDMFormFieldContextHelper gridDDMFormFieldContextHelper =
			new GridDDMFormFieldContextHelper(ddmFormFieldOptions, locale);

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

		return gridDDMFormFieldContextHelper.getOptions(
			ddmFormFieldRenderingContext);
	}

}