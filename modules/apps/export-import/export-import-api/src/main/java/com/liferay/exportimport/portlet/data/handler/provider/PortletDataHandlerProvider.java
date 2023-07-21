/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.portlet.data.handler.provider;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.model.Portlet;

/**
 * @author Máté Thurzó
 */
@ProviderType
public interface PortletDataHandlerProvider {

	public PortletDataHandler provide(long companyId, String portletId);

	public PortletDataHandler provide(Portlet portlet);

	public PortletDataHandler provide(String portletId);

}