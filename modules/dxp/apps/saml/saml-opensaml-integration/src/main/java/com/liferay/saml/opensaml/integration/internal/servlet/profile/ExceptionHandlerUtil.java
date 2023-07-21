/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.saml.opensaml.integration.internal.servlet.profile;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.saml.runtime.SamlException;

/**
 * @author Michael C. Han
 */
public class ExceptionHandlerUtil {

	public static void handleException(Exception e) throws PortalException {
		if (e instanceof PortalException) {
			throw (PortalException)e;
		}
		else if (e instanceof SystemException) {
			throw (SystemException)e;
		}

		throw new SamlException(e);
	}

}