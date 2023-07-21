/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.SharepointResultException;
import com.liferay.sharepoint.connector.internal.util.RemoteExceptionUtil;

import com.microsoft.webservices.SharePoint.QueryService.QueryServiceSoap;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Iván Zaera
 */
public class GetObjectsByQueryPacketOperation extends BaseOperation {

	@Override
	public void afterPropertiesSet() {
		_getSharepointObjectByPathOperation = getOperation(
			GetSharepointObjectByPathOperation.class);

		_searchPrefix =
			sharepointConnectionInfo.getServiceURL() +
				sharepointConnectionInfo.getLibraryPath();

		_searchPrefixLength = _searchPrefix.length();
	}

	public List<SharepointObject> execute(String queryPacket)
		throws SharepointException {

		try {
			String queryServiceSoapResultString = _queryServiceSoap.query(
				queryPacket);

			QueryServiceSoapResult queryServiceSoapResult =
				new QueryServiceSoapResult(queryServiceSoapResultString);

			if (!queryServiceSoapResult.isSuccess()) {
				throw new SharepointResultException(
					queryServiceSoapResult.getStatus(),
					queryServiceSoapResult.getDebugErrorMessage());
			}

			if (queryServiceSoapResult.isEmpty()) {
				return Collections.emptyList();
			}

			List<String> queryServiceSoapResultLinkURLs =
				queryServiceSoapResult.getLinkURLs();

			List<SharepointObject> sharepointObjects = new ArrayList<>();

			for (String queryServiceSoapResultLinkURL :
					queryServiceSoapResultLinkURLs) {

				if (!queryServiceSoapResultLinkURL.startsWith(_searchPrefix)) {
					continue;
				}

				String path = queryServiceSoapResultLinkURL.substring(
					_searchPrefixLength);

				SharepointObject sharepointObject =
					_getSharepointObjectByPathOperation.execute(path);

				if (sharepointObject == null) {
					if (_log.isWarnEnabled()) {
						_log.warn("Ignored Sharepoint object at path " + path);
					}

					continue;
				}

				sharepointObjects.add(sharepointObject);
			}

			return sharepointObjects;
		}
		catch (RemoteException re) {
			RemoteExceptionUtil.handleRemoteException(re);

			throw new IllegalStateException();
		}
	}

	public void setQueryServiceSoap(QueryServiceSoap queryServiceSoap) {
		_queryServiceSoap = queryServiceSoap;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetObjectsByQueryPacketOperation.class);

	private GetSharepointObjectByPathOperation
		_getSharepointObjectByPathOperation;
	private QueryServiceSoap _queryServiceSoap;
	private String _searchPrefix;
	private int _searchPrefixLength;

}