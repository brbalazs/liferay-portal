/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.petra.string.StringPool;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointResultException;
import com.liferay.sharepoint.connector.internal.util.RemoteExceptionUtil;
import com.liferay.sharepoint.connector.schema.batch.Batch;

import com.microsoft.schemas.sharepoint.soap.UpdateListItemsResponseUpdateListItemsResult;
import com.microsoft.schemas.sharepoint.soap.UpdateListItemsUpdates;

import java.rmi.RemoteException;

import org.apache.axis.message.MessageElement;

import org.w3c.dom.Element;

/**
 * @author Iván Zaera
 */
public class BatchOperation extends BaseOperation {

	public void execute(Batch batch) throws SharepointException {
		UpdateListItemsUpdates updateListItemsUpdates =
			new UpdateListItemsUpdates();

		Element element = xmlHelper.toElement(batch);

		MessageElement messageElement = new MessageElement(element);

		updateListItemsUpdates.set_any(new MessageElement[] {messageElement});

		UpdateListItemsResponseUpdateListItemsResult
			updateListItemsResponseUpdateListItemsResult = null;

		try {
			updateListItemsResponseUpdateListItemsResult =
				listsSoap.updateListItems(
					sharepointConnectionInfo.getLibraryName(),
					updateListItemsUpdates);
		}
		catch (RemoteException re) {
			RemoteExceptionUtil.handleRemoteException(re);
		}

		parseUpdateListItemsResponseUpdateListItemsResult(
			updateListItemsResponseUpdateListItemsResult);
	}

	protected void parseUpdateListItemsResponseUpdateListItemsResult(
			UpdateListItemsResponseUpdateListItemsResult
				updateListItemsResponseUpdateListItemsResult)
		throws SharepointException {

		Element updateListItemsResponseUpdateListItemsResultElement =
			xmlHelper.getElement(updateListItemsResponseUpdateListItemsResult);

		Element resultElement = xmlHelper.getElement(
			"Result", updateListItemsResponseUpdateListItemsResultElement);

		Element errorCodeElement = xmlHelper.getElement(
			"ErrorCode", resultElement);

		String errorCode = errorCodeElement.getTextContent();

		if (!errorCode.equals(SharepointConstants.NUMERIC_STATUS_SUCCESS)) {
			Element errorTextElement = xmlHelper.getElement(
				"ErrorText", resultElement);

			String errorText = errorTextElement.getTextContent();

			errorText = errorText.replaceAll(
				StringPool.NEW_LINE, StringPool.PIPE);

			throw new SharepointResultException(errorCode, errorText);
		}
	}

}