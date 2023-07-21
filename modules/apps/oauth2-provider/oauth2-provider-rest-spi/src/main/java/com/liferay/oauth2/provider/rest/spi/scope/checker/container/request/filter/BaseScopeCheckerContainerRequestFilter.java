/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.rest.spi.scope.checker.container.request.filter;

import com.liferay.oauth2.provider.scope.liferay.OAuth2ProviderScopeLiferayAccessControlContext;
import com.liferay.oauth2.provider.scope.liferay.OAuth2ProviderScopeLiferayConstants;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * @author Marta Medio
 */
public abstract class BaseScopeCheckerContainerRequestFilter
	implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		if (OAuth2ProviderScopeLiferayAccessControlContext.
				isOAuth2AuthVerified() &&
			!isContainerRequestContextAllowed(containerRequestContext)) {

			containerRequestContext.abortWith(
				Response.status(
					Response.Status.FORBIDDEN
				).build());
		}
	}

	protected abstract boolean isContainerRequestContextAllowed(
		ContainerRequestContext containerRequestContext);

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 * OAuth2ProviderScopeLiferayAccessControlContext#isOAuth2AuthVerified()}
	 */
	@Deprecated
	protected boolean isOAuth2AuthVerified() {
		AccessControlContext accessControlContext =
			AccessControlUtil.getAccessControlContext();

		AuthVerifierResult authVerifierResult =
			accessControlContext.getAuthVerifierResult();

		if ((authVerifierResult != null) &&
			AuthVerifierResult.State.SUCCESS.equals(
				authVerifierResult.getState())) {

			Map<String, Object> settings = authVerifierResult.getSettings();

			String authType = MapUtil.getString(settings, "auth.type");

			if (Validator.isNotNull(authType) &&
				!authType.equals(
					OAuth2ProviderScopeLiferayConstants.
						AUTH_VERIFIER_OAUTH2_TYPE)) {

				return false;
			}
		}

		return true;
	}

}