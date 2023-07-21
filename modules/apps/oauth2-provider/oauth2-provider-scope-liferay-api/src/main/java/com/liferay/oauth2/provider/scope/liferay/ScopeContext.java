/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.liferay;

import org.osgi.framework.Bundle;

/**
 * This interface represents the context surrounding per-request scope check
 * security procedure.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public interface ScopeContext {

	/**
	 * Reset state of the context
	 */
	public void clear();

	/**
	 * Sets access token string into the context to be used during security
	 * check
	 *
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken);

	/**
	 * Sets application name into the context to be used during security check
	 *
	 * @param  applicationName
	 * @review
	 */
	public void setApplicationName(String applicationName);

	/**
	 * Sets OSGi bundle into the context to be used during security check
	 *
	 * @param  bundle
	 * @review
	 */
	public void setBundle(Bundle bundle);

	/**
	 * Sets request companyId into the context to be used during security check
	 *
	 * @param companyId
	 */
	public void setCompanyId(long companyId);

}