/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.exception;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Wilberforce (7.0.x)
 */
@Deprecated
public class LayoutHiddenException extends PortalException {

	public LayoutHiddenException() {
	}

	public LayoutHiddenException(String msg) {
		super(msg);
	}

	public LayoutHiddenException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public LayoutHiddenException(Throwable cause) {
		super(cause);
	}

}