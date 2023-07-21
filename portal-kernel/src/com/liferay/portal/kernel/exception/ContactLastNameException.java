/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.exception;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
 *             ContactNameException.MustHaveLastName}
 */
@Deprecated
public class ContactLastNameException extends PortalException {

	public ContactLastNameException() {
	}

	public ContactLastNameException(String msg) {
		super(msg);
	}

	public ContactLastNameException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ContactLastNameException(Throwable cause) {
		super(cause);
	}

}