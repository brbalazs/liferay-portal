/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.radio.internal;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldOptionsValidationTestCase;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueValidator;

/**
 * @author Marcellus Tavares
 */
public class RadioDDMFormFieldValueValidatorTest
	extends BaseDDMFormFieldOptionsValidationTestCase {

	@Override
	protected DDMFormFieldValueValidator getDDMFormFieldValueValidator() {
		return _radioDDMFormFieldValueValidator;
	}

	private final RadioDDMFormFieldValueValidator
		_radioDDMFormFieldValueValidator =
			new RadioDDMFormFieldValueValidator();

}