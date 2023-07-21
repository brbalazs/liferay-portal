/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.liferay;

import java.util.Collection;

/**
 * This interface allows to list scope aliases and matching {@link
 * LiferayOAuth2Scope}s based on a portal instance configuration of OAuth2
 * Framework.<br /> Scope alias can match multiple {@link LiferayOAuth2Scope}s
 * based on particular portal instance configuration of OAuth2 Framework.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public interface ScopeLocator {

	/**
	 * Returns a collection of application exported scopes matching a {@code
	 * scopesAlias} in the given portal instance.
	 *
	 * @param  companyId the company for which the scopes are to be located
	 * @param  scopesAlias the scope alias the scopes are mapped to
	 * @return a collection of one or more matching scopes for the given company
	 *         and scope alias
	 * @review
	 */
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias);

	/**
	 * Returns a collection of application exported scopes matching a {@code
	 * scopesAlias} in the given portal instance, filtered by {@code
	 * applicationName}.
	 *
	 * @param  companyId the company for which the scopes are to be located
	 * @param  scopesAlias the scope alias the scopes are mapped to
	 * @param  applicationName the application for which the scopes are to be
	 *         located
	 * @return a collection of one or more matching scopes for the given company
	 *         and scope alias, filtered by {@code applicationName}
	 * @review
	 */
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias, String applicationName);

	/**
	 * Returns a list of scope aliases available for the given portal instance.
	 * <br />
	 *
	 * @param  companyId the portal instance context
	 * @return a non-<code>null</code> collection of scope aliases from the
	 *         portal instance
	 * @review
	 */
	public Collection<String> getScopeAliases(long companyId);

	/**
	 * Returns a list of scope aliases available for the given portal instance,
	 * filtered by application name.
	 *
	 * @param  companyId the portal instance context
	 * @param  applicationName name of application exporting the scopes
	 * @return a non-<code>null</code> collection of scope aliases from the
	 *         portal instance filtered by {@code applicationName}
	 * @review
	 */
	public Collection<String> getScopeAliases(
		long companyId, String applicationName);

}