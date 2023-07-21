/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.bundle.ssoutil;

import com.liferay.portal.kernel.security.sso.SSO;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = SSO.class
)
public class TestSSOImpl implements SSO {

	@Override
	public String getSessionExpirationRedirectUrl(long companyId) {
		return "getSessionExpirationRedirectUrl:" + companyId;
	}

	@Override
	public String getSignInURL(long companyId, String defaultSignInURL) {
		return defaultSignInURL + ":" + companyId;
	}

	@Override
	public boolean isLoginRedirectRequired(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isRedirectRequired(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isSessionRedirectOnExpire(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

}