/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Marcellus Tavares
 */
public class OSBAsahVirusFoundException extends OSBAsahException {

	public OSBAsahVirusFoundException(String virusName) {
		super(
			HttpStatus.BAD_REQUEST,
			String.format("Virus %s was detected ", virusName));
	}

}