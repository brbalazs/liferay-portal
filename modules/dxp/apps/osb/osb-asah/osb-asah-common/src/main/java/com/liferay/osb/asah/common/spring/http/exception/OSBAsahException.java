/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpStatus;

/**
 * @author Marcellus Tavares
 */
public class OSBAsahException extends RuntimeException {

	public OSBAsahException(HttpStatus httpStatus, String message) {
		this(httpStatus, message, null);
	}

	public OSBAsahException(
		HttpStatus httpStatus, String message, String messageKey,
		Throwable throwable) {

		super(message, throwable);

		_httpStatus = httpStatus;
		_messageKey = messageKey;
	}

	public OSBAsahException(
		HttpStatus httpStatus, String message, Throwable throwable) {

		this(
			httpStatus, message,
			StringUtils.replaceAll(StringUtils.lowerCase(message), "\\s+", "-"),
			throwable);
	}

	public HttpStatus getHttpStatus() {
		return _httpStatus;
	}

	public String getMessageKey() {
		return _messageKey;
	}

	private final HttpStatus _httpStatus;
	private final String _messageKey;

}