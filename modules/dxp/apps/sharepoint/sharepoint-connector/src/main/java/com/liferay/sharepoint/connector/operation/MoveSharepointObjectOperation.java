/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.schema.batch.Batch;
import com.liferay.sharepoint.connector.schema.batch.BatchField;
import com.liferay.sharepoint.connector.schema.batch.BatchMethod;

import java.net.URL;

/**
 * @author Iván Zaera
 */
public class MoveSharepointObjectOperation extends BaseOperation {

	@Override
	public void afterPropertiesSet() {
		_batchOperation = getOperation(BatchOperation.class);
		_checkInFileOperation = getOperation(CheckInFileOperation.class);
		_checkOutFileOperation = getOperation(CheckOutFileOperation.class);
		_copySharepointObjectOperation = getOperation(
			CopySharepointObjectOperation.class);
		_deleteSharepointObjectOperation = getOperation(
			DeleteSharepointObjectOperation.class);
		_getSharepointObjectByPathOperation = getOperation(
			GetSharepointObjectByPathOperation.class);
	}

	public void execute(String path, String newPath)
		throws SharepointException {

		SharepointObject sharepointObject =
			_getSharepointObjectByPathOperation.execute(path);

		if (isRename(path, newPath)) {
			String oldExtension = pathHelper.getExtension(path);

			String newExtension = pathHelper.getExtension(newPath);

			if (!oldExtension.equals(newExtension)) {
				throw new SharepointException(
					"Sharepoint does not support changing file extensions");
			}

			URL url = sharepointObject.getURL();
			String newName = pathHelper.getNameWithoutExtension(newPath);

			_batchOperation.execute(
				new Batch(
					Batch.OnError.RETURN, null,
					new BatchMethod(
						SharepointConstants.BATCH_METHOD_ID_DEFAULT,
						BatchMethod.Command.UPDATE,
						new BatchField(
							"ID", sharepointObject.getSharepointObjectId()),
						new BatchField("FileRef", url.toString()),
						new BatchField("BaseName", newName))));
		}
		else {
			_copySharepointObjectOperation.execute(path, newPath);
			_deleteSharepointObjectOperation.execute(path);

			_checkInFileOperation.execute(
				newPath, StringPool.BLANK,
				SharepointConnection.CheckInType.MAJOR);

			if (Validator.isNotNull(sharepointObject.getCheckedOutBy())) {
				_checkOutFileOperation.execute(newPath);
			}
		}
	}

	protected boolean isRename(String path, String newPath) {
		String parentFolderPath = pathHelper.getParentFolderPath(path);
		String newParentFolderPath = pathHelper.getParentFolderPath(newPath);

		return parentFolderPath.equals(newParentFolderPath);
	}

	private BatchOperation _batchOperation;
	private CheckInFileOperation _checkInFileOperation;
	private CheckOutFileOperation _checkOutFileOperation;
	private CopySharepointObjectOperation _copySharepointObjectOperation;
	private DeleteSharepointObjectOperation _deleteSharepointObjectOperation;
	private GetSharepointObjectByPathOperation
		_getSharepointObjectByPathOperation;

}