/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.exception;

import com.liferay.portal.kernel.model.Company;

/**
 * @author Brian Wing Shun Chan
 * @author José Manuel Navarro
 */
public class SendPasswordException extends PortalException {

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by the inner classes
	 */
	@Deprecated
	public SendPasswordException() {
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by the inner classes
	 */
	@Deprecated
	public SendPasswordException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by the inner classes
	 */
	@Deprecated
	public SendPasswordException(Throwable cause) {
		super(cause);
	}

	public static class MustBeEnabled extends SendPasswordException {

		public MustBeEnabled(Company company) {
			super(
				String.format(
					"The Forgot Password notification must be enabled for " +
						"company %s",
					company));

			this.company = company;
		}

		public final Company company;

	}

	protected SendPasswordException(String msg) {
		super(msg);
	}

}