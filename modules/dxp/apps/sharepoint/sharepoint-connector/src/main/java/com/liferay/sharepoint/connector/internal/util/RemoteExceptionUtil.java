/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.internal.util;

import com.liferay.portal.kernel.repository.AuthenticationRepositoryException;
import com.liferay.sharepoint.connector.SharepointException;

import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

/**
 * @author Adolfo Pérez
 */
public class RemoteExceptionUtil {

	public static void handleRemoteException(RemoteException re)
		throws SharepointException {

		if (re instanceof AxisFault) {
			AxisFault axisFault = (AxisFault)re;

			String faultString = axisFault.getFaultString();

			if (faultString.startsWith("(401)Unauthorized")) {
				throw new AuthenticationRepositoryException(re);
			}
		}

		throw new SharepointException(
			"Unable to communicate with the Sharepoint server", re);
	}

}