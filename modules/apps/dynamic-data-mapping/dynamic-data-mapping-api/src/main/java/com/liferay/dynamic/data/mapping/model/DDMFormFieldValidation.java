/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.model;

import com.liferay.petra.lang.HashUtil;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class DDMFormFieldValidation implements Serializable {

	public DDMFormFieldValidation() {
	}

	public DDMFormFieldValidation(
		DDMFormFieldValidation ddmFormFieldValidation) {

		_expression = ddmFormFieldValidation._expression;
		_errorMessageLocalizedValue =
			ddmFormFieldValidation._errorMessageLocalizedValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormFieldValidation)) {
			return false;
		}

		DDMFormFieldValidation ddmFormFieldValidation =
			(DDMFormFieldValidation)obj;

		if (Objects.equals(
				_errorMessageLocalizedValue,
				ddmFormFieldValidation._errorMessageLocalizedValue) &&
			Objects.equals(_expression, ddmFormFieldValidation._expression)) {

			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getErrorMessageLocalizedValue()}
	 */
	@Deprecated
	public String getErrorMessage() {
		return _errorMessageLocalizedValue.getString(
			_errorMessageLocalizedValue.getDefaultLocale());
	}

	public LocalizedValue getErrorMessageLocalizedValue() {
		return _errorMessageLocalizedValue;
	}

	public String getExpression() {
		return _expression;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _errorMessageLocalizedValue);

		return HashUtil.hash(hash, _expression);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #setErrorMessageLocalizedValue(LocalizedValue)}
	 */
	@Deprecated
	public void setErrorMessage(String errorMessage) {
		LocalizedValue errorMessageLocalizedValue = new LocalizedValue();

		errorMessageLocalizedValue.addString(
			errorMessageLocalizedValue.getDefaultLocale(), errorMessage);

		setErrorMessageLocalizedValue(errorMessageLocalizedValue);
	}

	public void setErrorMessageLocalizedValue(
		LocalizedValue errorMessageLocalizedValue) {

		_errorMessageLocalizedValue = errorMessageLocalizedValue;
	}

	public void setExpression(String expression) {
		_expression = expression;
	}

	private LocalizedValue _errorMessageLocalizedValue;
	private String _expression;

}