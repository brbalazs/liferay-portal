/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import aQute.bnd.annotation.ProviderType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eduardo García
 */
@ProviderType
public interface TermsOfUseContentProvider {

	public String getClassName();

	public void includeConfig(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception;

	public void includeView(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception;

}