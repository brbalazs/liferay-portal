/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.exception;

/**
 * @author     Amos Fong
 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
 *             ContactNameException.MustHaveValidFullName}
 */
@Deprecated
public class ContactFullNameException extends PortalException {

	public ContactFullNameException() {
	}

	public ContactFullNameException(String msg) {
		super(msg);
	}

	public ContactFullNameException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ContactFullNameException(Throwable cause) {
		super(cause);
	}

}