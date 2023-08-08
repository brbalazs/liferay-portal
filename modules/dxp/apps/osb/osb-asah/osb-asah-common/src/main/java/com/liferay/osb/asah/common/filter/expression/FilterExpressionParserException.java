/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression;

/**
 * @author Marcellus Tavares
 */
public class FilterExpressionParserException extends RuntimeException {

	public FilterExpressionParserException(String message) {
		super(message);
	}

	public FilterExpressionParserException(
		String message, Throwable throwable) {

		super(message, throwable);
	}

}