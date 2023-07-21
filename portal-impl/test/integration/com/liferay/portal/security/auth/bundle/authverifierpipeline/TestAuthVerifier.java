/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authverifierpipeline;

import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"service.ranking:Integer=" + Integer.MAX_VALUE,
		"urls.includes=/TestAuthVerifier/*,/TestAuthVerifierTest/*"
	},
	service = AuthVerifier.class
)
public class TestAuthVerifier implements AuthVerifier {

	@Override
	public String getAuthType() {
		return HttpServletRequest.BASIC_AUTH;
	}

	@Override
	public AuthVerifierResult verify(
		AccessControlContext accessControlContext, Properties properties) {

		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		authVerifierResult.setPassword("best_password_ever");

		Map<String, Object> settings = new HashMap<>();

		settings.put("auth.type", HttpServletRequest.BASIC_AUTH);

		authVerifierResult.setSettings(settings);

		authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
		authVerifierResult.setUserId(1);

		return authVerifierResult;
	}

}