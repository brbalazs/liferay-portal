/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.opensso.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.security.sso.SSO;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.opensso.configuration.OpenSSOConfiguration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * Enables the OpenSSO module to participate in significant portal session
 * lifecycle changes.
 *
 * @author Michael C. Han
 */
@Component(
	configurationPid = "com.liferay.portal.security.sso.opensso.configuration.OpenSSOConfiguration",
	immediate = true, service = SSO.class
)
public class SSOImpl implements SSO {

	@Override
	public String getSessionExpirationRedirectUrl(long companyId) {
		if (isSessionRedirectOnExpire(companyId)) {
			return PrefsPropsUtil.getString(
				companyId, PropsKeys.OPEN_SSO_LOGOUT_URL,
				_openSSOConfiguration.logoutURL());
		}

		return null;
	}

	@Override
	public String getSignInURL(long companyId, String defaultSigninURL) {
		if (!isOpenSSOEnabled(companyId)) {
			return null;
		}

		if (Validator.isNotNull(_openSSOConfiguration.loginURL())) {
			defaultSigninURL = _openSSOConfiguration.loginURL();
		}

		return PrefsPropsUtil.getString(
			companyId, PropsKeys.OPEN_SSO_LOGIN_URL, defaultSigninURL);
	}

	@Override
	public boolean isLoginRedirectRequired(long companyId) {
		return isOpenSSOEnabled(companyId);
	}

	@Override
	public boolean isRedirectRequired(long companyId) {
		return false;
	}

	@Override
	public boolean isSessionRedirectOnExpire(long companyId) {
		if (isOpenSSOEnabled(companyId)) {
			return _openSSOConfiguration.logoutOnSessionExpiration();
		}

		return false;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_openSSOConfiguration = ConfigurableUtil.createConfigurable(
			OpenSSOConfiguration.class, properties);
	}

	protected boolean isOpenSSOEnabled(long companyId) {
		if (PrefsPropsUtil.getBoolean(
				companyId, PropsKeys.OPEN_SSO_AUTH_ENABLED,
				_openSSOConfiguration.enabled())) {

			return true;
		}

		return false;
	}

	private volatile OpenSSOConfiguration _openSSOConfiguration;

}