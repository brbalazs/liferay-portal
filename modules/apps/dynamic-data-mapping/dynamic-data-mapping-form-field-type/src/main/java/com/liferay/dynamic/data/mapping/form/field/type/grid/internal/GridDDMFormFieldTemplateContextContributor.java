/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.grid.internal;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pedro Queiroz
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=grid",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		GridDDMFormFieldTemplateContextContributor.class
	}
)
public class GridDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put(
			"columns",
			getOptions("columns", ddmFormField, ddmFormFieldRenderingContext));
		parameters.put(
			"rows",
			getOptions("rows", ddmFormField, ddmFormFieldRenderingContext));
		parameters.put(
			"value",
			jsonFactory.looseDeserialize(
				ddmFormFieldRenderingContext.getValue()));

		return parameters;
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(
		String optionType, DDMFormField ddmFormField) {

		return (DDMFormFieldOptions)ddmFormField.getProperty(optionType);
	}

	protected List<Object> getOptions(
		String key, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		GridDDMFormFieldContextHelper gridDDMFormFieldContextHelper =
			new GridDDMFormFieldContextHelper(
				getDDMFormFieldOptions(key, ddmFormField),
				ddmFormFieldRenderingContext.getLocale());

		return gridDDMFormFieldContextHelper.getOptions(
			ddmFormFieldRenderingContext);
	}

	@Reference
	protected JSONFactory jsonFactory;

}