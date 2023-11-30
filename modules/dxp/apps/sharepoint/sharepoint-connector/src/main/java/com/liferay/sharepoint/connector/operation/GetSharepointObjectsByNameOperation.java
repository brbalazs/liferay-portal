/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.petra.string.StringPool;
import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.schema.query.Query;
import com.liferay.sharepoint.connector.schema.query.QueryField;
import com.liferay.sharepoint.connector.schema.query.QueryOptionsList;
import com.liferay.sharepoint.connector.schema.query.QueryValue;
import com.liferay.sharepoint.connector.schema.query.operator.ContainsOperator;
import com.liferay.sharepoint.connector.schema.query.option.BaseQueryOption;
import com.liferay.sharepoint.connector.schema.query.option.FolderQueryOption;
import com.liferay.sharepoint.connector.schema.query.option.ViewAttributesQueryOption;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iván Zaera
 */
public class GetSharepointObjectsByNameOperation extends BaseOperation {

	@Override
	public void afterPropertiesSet() {
		_getSharepointObjectsByQueryOperation = getOperation(
			GetSharepointObjectsByQueryOperation.class);
	}

	public List<SharepointObject> execute(String name)
		throws SharepointException {

		Query query = new Query(
			new ContainsOperator(
				new QueryField("FileRef"), new QueryValue(name)));

		List<BaseQueryOption> baseQueryOptions = new ArrayList<>();

		baseQueryOptions.add(new FolderQueryOption(StringPool.BLANK));

		if (sharepointConnectionInfo.getServerVersion() ==
				SharepointConnection.ServerVersion.SHAREPOINT_2013) {

			baseQueryOptions.add(new ViewAttributesQueryOption(true));
		}

		return _getSharepointObjectsByQueryOperation.execute(
			query,
			new QueryOptionsList(
				baseQueryOptions.toArray(new BaseQueryOption[0])));
	}

	private GetSharepointObjectsByQueryOperation
		_getSharepointObjectsByQueryOperation;

}