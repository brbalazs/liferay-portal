/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.url.builder;

import javax.servlet.http.HttpServletRequest;

/**
 * A service that creates new AbsolutePortalURLBuilder instances.
 *
 * @author Iván Zaera Avellón
 * @review
 */
public interface AbsolutePortalURLBuilderFactory {

	/**
	 * Get a new AbsolutePortalURLBuilder instance tied to the given request.
	 *
	 * @param  request the servlet request
	 * @return an instance of AbsolutePortalURLBuilder
	 * @review
	 */
	public AbsolutePortalURLBuilder getAbsolutePortalURLBuilder(
		HttpServletRequest request);

}