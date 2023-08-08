/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Inácio Nery
 */
public class OSBAsahNameException extends OSBAsahException {

	public OSBAsahNameException() {
		super(HttpStatus.BAD_REQUEST, "Name cannot be blank");
	}

}