/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.schema.query.Query;
import com.liferay.sharepoint.connector.schema.query.QueryField;
import com.liferay.sharepoint.connector.schema.query.QueryOptionsList;
import com.liferay.sharepoint.connector.schema.query.QueryValue;
import com.liferay.sharepoint.connector.schema.query.operator.EqOperator;
import com.liferay.sharepoint.connector.schema.query.option.FolderQueryOption;

import java.util.List;

/**
 * @author Iván Zaera
 */
public class GetSharepointObjectsByFolderOperation extends BaseOperation {

	@Override
	public void afterPropertiesSet() {
		_getSharepointObjectsByQueryOperation = getOperation(
			GetSharepointObjectsByQueryOperation.class);
	}

	public List<SharepointObject> execute(
			String folderPath,
			SharepointConnection.ObjectTypeFilter objectTypeFilter)
		throws SharepointException {

		Query query = null;

		if (objectTypeFilter.equals(
				SharepointConnection.ObjectTypeFilter.ALL)) {

			query = new Query(null);
		}
		else if (objectTypeFilter.equals(
					SharepointConnection.ObjectTypeFilter.FILES)) {

			query = new Query(
				new EqOperator(
					new QueryField("FSObjType"),
					new QueryValue(
						QueryValue.Type.LOOKUP,
						SharepointConstants.FS_OBJ_TYPE_FILE)));
		}
		else if (objectTypeFilter.equals(
					SharepointConnection.ObjectTypeFilter.FOLDERS)) {

			query = new Query(
				new EqOperator(
					new QueryField("FSObjType"),
					new QueryValue(
						QueryValue.Type.LOOKUP,
						SharepointConstants.FS_OBJ_TYPE_FOLDER)));
		}
		else {
			throw new UnsupportedOperationException(
				"Unsupported object type filter " + objectTypeFilter);
		}

		String folderFullPath = toFullPath(folderPath);

		QueryOptionsList queryOptionsList = new QueryOptionsList(
			new FolderQueryOption(folderFullPath));

		return _getSharepointObjectsByQueryOperation.execute(
			query, queryOptionsList);
	}

	private GetSharepointObjectsByQueryOperation
		_getSharepointObjectsByQueryOperation;

}