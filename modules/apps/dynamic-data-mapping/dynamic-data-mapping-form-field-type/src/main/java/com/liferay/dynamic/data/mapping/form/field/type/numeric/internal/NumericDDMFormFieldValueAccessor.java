/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.numeric.internal;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueAccessor;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=numeric",
	service = {
		DDMFormFieldValueAccessor.class, NumericDDMFormFieldValueAccessor.class
	}
)
public class NumericDDMFormFieldValueAccessor
	implements DDMFormFieldValueAccessor<Number> {

	@Override
	public Number getValue(DDMFormFieldValue ddmFormFieldValue, Locale locale) {
		Value value = ddmFormFieldValue.getValue();

		try {
			NumberFormat formatter = NumericDDMFormFieldUtil.getNumberFormat(
				locale);

			return formatter.parse(value.getString(locale));
		}
		catch (ParseException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		NumericDDMFormFieldValueAccessor.class);

}