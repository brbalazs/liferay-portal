/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import aQute.bnd.annotation.ProviderType;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
@ProviderType
public interface InactiveRequestHandler {

	public default boolean isShowInactiveRequestMessage() {
		return false;
	}

	public void processInactiveRequest(
			HttpServletRequest request, HttpServletResponse response,
			String messageKey)
		throws IOException;

}