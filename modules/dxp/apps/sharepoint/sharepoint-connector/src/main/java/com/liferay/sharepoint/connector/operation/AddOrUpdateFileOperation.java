/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointResultException;
import com.liferay.sharepoint.connector.internal.util.RemoteExceptionUtil;

import com.microsoft.schemas.sharepoint.soap.CopyErrorCode;
import com.microsoft.schemas.sharepoint.soap.CopyResult;
import com.microsoft.schemas.sharepoint.soap.FieldInformation;
import com.microsoft.schemas.sharepoint.soap.holders.CopyResultCollectionHolder;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.rmi.RemoteException;

import org.apache.axis.holders.UnsignedIntHolder;

/**
 * @author Iván Zaera
 */
public class AddOrUpdateFileOperation extends BaseOperation {

	@Override
	public void afterPropertiesSet() {
		_checkInFileOperation = getOperation(CheckInFileOperation.class);
	}

	public void execute(
			String filePath, String changeLog, InputStream inputStream)
		throws SharepointException {

		URL filePathURL = toURL(filePath);

		byte[] bytes = null;

		try {
			bytes = FileUtil.getBytes(inputStream);
		}
		catch (IOException ioe) {
			throw new SharepointException("Unable to read input stream", ioe);
		}

		CopyResultCollectionHolder copyResultCollectionHolder =
			new CopyResultCollectionHolder();

		try {
			copySoap.copyIntoItems(
				SharepointConstants.URL_SOURCE_NONE,
				new String[] {filePathURL.toString()},
				_EMPTY_FIELD_INFORMATIONS, bytes, new UnsignedIntHolder(),
				copyResultCollectionHolder);
		}
		catch (RemoteException re) {
			RemoteExceptionUtil.handleRemoteException(re);
		}

		CopyResult copyResult = copyResultCollectionHolder.value[0];

		CopyErrorCode copyErrorCode = copyResult.getErrorCode();

		if (copyErrorCode != CopyErrorCode.Success) {
			throw new SharepointResultException(
				copyErrorCode.toString(), copyResult.getErrorMessage());
		}

		if (changeLog != null) {
			_checkInFileOperation.execute(
				filePath, changeLog, SharepointConnection.CheckInType.MAJOR);
		}
	}

	private static final FieldInformation[] _EMPTY_FIELD_INFORMATIONS =
		new FieldInformation[0];

	private CheckInFileOperation _checkInFileOperation;

}