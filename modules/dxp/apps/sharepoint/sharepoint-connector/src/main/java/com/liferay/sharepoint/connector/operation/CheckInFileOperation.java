/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.internal.util.RemoteExceptionUtil;

import java.net.URL;

import java.rmi.RemoteException;

/**
 * @author Iván Zaera
 */
public class CheckInFileOperation extends BaseOperation {

	public boolean execute(
			String filePath, String comment,
			SharepointConnection.CheckInType checkInType)
		throws SharepointException {

		try {
			URL filePathURL = toURL(filePath);

			String protocolValue = String.valueOf(
				checkInType.getProtocolValue());

			return listsSoap.checkInFile(
				filePathURL.toString(), comment, protocolValue);
		}
		catch (RemoteException re) {
			RemoteExceptionUtil.handleRemoteException(re);

			throw new IllegalStateException();
		}
	}

}