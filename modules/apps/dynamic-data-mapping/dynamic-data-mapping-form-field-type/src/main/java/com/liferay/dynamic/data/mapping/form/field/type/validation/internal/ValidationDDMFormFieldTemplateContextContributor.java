/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.validation.internal;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=validation",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		ValidationDDMFormFieldTemplateContextContributor.class
	}
)
public class ValidationDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("value", getValue(ddmFormFieldRenderingContext));

		return parameters;
	}

	protected Map<String, Object> getValue(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> value = new HashMap<>();

		String valueString = ddmFormFieldRenderingContext.getValue();

		if (Validator.isNotNull(valueString)) {
			try {
				JSONObject valueJSONObject = jsonFactory.createJSONObject(
					valueString);

				value.put(
					"errorMessage",
					valueJSONObject.getJSONObject("errorMessage"));

				value.put(
					"expression", valueJSONObject.getString("expression"));
			}
			catch (JSONException jsone) {
				if (_log.isWarnEnabled()) {
					_log.warn(jsone, jsone);
				}
			}
		}
		else {
			value.put("errorMessage", jsonFactory.createJSONObject());
			value.put("expression", StringPool.BLANK);
		}

		return value;
	}

	@Reference
	protected JSONFactory jsonFactory;

	private static final Log _log = LogFactoryUtil.getLog(
		ValidationDDMFormFieldTemplateContextContributor.class);

}