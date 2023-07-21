/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet;

import aQute.bnd.annotation.ProviderType;

import javax.portlet.HeaderResponse;
import javax.portlet.MimeResponse;

/**
 * @author Neil Griffin
 */
@ProviderType
public interface LiferayHeaderResponse
	extends HeaderResponse, LiferayPortletResponse, MimeResponse {

	public void writeToHead();

}