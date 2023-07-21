/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.liferay;

import org.osgi.framework.Bundle;

/**
 * Represents the whole information about an application exported scope into
 * OAuth2 provider framework Liferay environment.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public interface LiferayOAuth2Scope {

	/**
	 * Name of the application that provides the scope.<br /> Usually refers to
	 * JAX-RS application name.
	 *
	 * @return non-<code>null</code> application name
	 * @review
	 */
	public String getApplicationName();

	/**
	 * OSGi bundle context from which the application and scope is published.
	 *
	 * @return non-<code>null</code> OSGi bundle
	 * @review
	 */
	public Bundle getBundle();

	/**
	 * Scope name as registered into OAuth2 Provider framework.
	 *
	 * @return non-<code>null</code> scope name
	 */
	public String getScope();

}