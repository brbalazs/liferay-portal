/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.connector.operation;

import com.liferay.sharepoint.connector.SharepointConnectionInfo;

import com.microsoft.schemas.sharepoint.soap.CopySoap;
import com.microsoft.schemas.sharepoint.soap.ListsSoap;
import com.microsoft.schemas.sharepoint.soap.VersionsSoap;

import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public interface Operation {

	public void afterPropertiesSet();

	public void setCopySoap(CopySoap copySoap);

	public void setListsSoap(ListsSoap listsSoap);

	public void setOperations(Map<Class<?>, Operation> operations);

	public void setSharepointConnectionInfo(
		SharepointConnectionInfo sharepointConnectionInfo);

	public void setVersionsSoap(VersionsSoap versionsSoap);

}