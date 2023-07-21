/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class DDMExpressionException extends PortalException {

	public DDMExpressionException() {
	}

	public DDMExpressionException(String msg) {
		super(msg);
	}

	public DDMExpressionException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DDMExpressionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public static class FunctionNotAllowed extends DDMExpressionException {

		public FunctionNotAllowed(String functionName) {
			super(
				String.format(
					"The function name \"%s\" is not allowed", functionName));

			_functionName = functionName;
		}

		public String getFunctionName() {
			return _functionName;
		}

		private final String _functionName;

	}

	public static class FunctionNotDefined extends DDMExpressionException {

		public FunctionNotDefined(Set<String> undefinedFunctionNames) {
			super(
				String.format(
					"The functions \"%s\" were not defined",
					StringUtil.merge(undefinedFunctionNames)));

			_undefinedFunctionNames = undefinedFunctionNames;
		}

		public Set<String> getUndefinedFunctionNames() {
			return _undefinedFunctionNames;
		}

		private final Set<String> _undefinedFunctionNames;

	}

	public static class IncompatipleReturnType extends DDMExpressionException {

		public IncompatipleReturnType() {
			super(
				"The evaluation return type differs from DDM Expression type");
		}

	}

	public static class InvalidSyntax extends DDMExpressionException {

		public InvalidSyntax(Throwable cause) {
			super("The expression syntax is invalid", cause);
		}

	}

	public static class NumberExceedsSupportedRange
		extends DDMExpressionException {

		public NumberExceedsSupportedRange() {
			super("The number entered exceeds the supported range");
		}

	}

}