/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.select.internal;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldOptionsValidationTestCase;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueValidator;
import com.liferay.portal.json.JSONFactoryImpl;

import org.junit.Before;

/**
 * @author Marcellus Tavares
 */
public class SelectDDMFormFieldValueValidatorTest
	extends BaseDDMFormFieldOptionsValidationTestCase {

	@Before
	public void setUp() {
		setUpDDMFormFieldValueValidator();
	}

	@Override
	protected DDMFormFieldValueValidator getDDMFormFieldValueValidator() {
		return _selectDDMFormFieldValueValidator;
	}

	protected void setUpDDMFormFieldValueValidator() {
		_selectDDMFormFieldValueValidator.jsonFactory = new JSONFactoryImpl();
	}

	private final SelectDDMFormFieldValueValidator
		_selectDDMFormFieldValueValidator =
			new SelectDDMFormFieldValueValidator();

}