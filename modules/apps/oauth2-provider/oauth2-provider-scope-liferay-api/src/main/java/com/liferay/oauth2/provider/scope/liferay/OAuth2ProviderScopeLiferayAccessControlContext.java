/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.liferay;

import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class OAuth2ProviderScopeLiferayAccessControlContext {

	public static boolean isOAuth2AuthVerified() {
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
				authType.equals(
					OAuth2ProviderScopeLiferayConstants.
						AUTH_VERIFIER_OAUTH2_TYPE)) {

				return true;
			}
		}

		return false;
	}

}