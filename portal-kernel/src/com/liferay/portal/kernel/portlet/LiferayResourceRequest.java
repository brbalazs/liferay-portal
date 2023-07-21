/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet;

import aQute.bnd.annotation.ProviderType;

import javax.portlet.ClientDataRequest;
import javax.portlet.ResourceRequest;

/**
 * @author Neil Griffin
 */
@ProviderType
public interface LiferayResourceRequest
	extends ClientDataRequest, LiferayPortletRequest, ResourceRequest {
}