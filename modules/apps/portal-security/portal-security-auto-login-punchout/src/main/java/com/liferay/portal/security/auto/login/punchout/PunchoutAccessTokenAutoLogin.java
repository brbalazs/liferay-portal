/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.auto.login.punchout;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.security.auto.login.BaseAutoLogin;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.auto.login.punchout.internal.PunchoutAutoLoginConstants;
import com.liferay.portal.security.auto.login.punchout.module.configuration.PunchoutAccessTokenAutoLoginConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	configurationPid = "com.liferay.portal.security.auto.login.punchout.module.configuration.PunchoutAccessTokenAutoLoginConfiguration",
	immediate = true, service = AutoLogin.class
)
public class PunchoutAccessTokenAutoLogin extends BaseAutoLogin {

	@Override
	protected String[] doLogin(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		long companyId = _portal.getCompanyId(request);

		if (!isEnabled(companyId)) {
			return null;
		}

		return _autoLogin.login(request, response);
	}

	protected boolean isEnabled(long companyId) {
		PunchoutAccessTokenAutoLoginConfiguration
			requestParameterAutoLoginConfiguration =
				_getPunchoutAccessTokenAutoLoginConfiguration(companyId);

		if (requestParameterAutoLoginConfiguration == null) {
			return false;
		}

		return requestParameterAutoLoginConfiguration.enabled();
	}

	@Reference(unbind = "-")
	protected void setConfigurationProvider(
		ConfigurationProvider configurationProvider) {

		_configurationProvider = configurationProvider;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portal = portal;
	}

	private PunchoutAccessTokenAutoLoginConfiguration
		_getPunchoutAccessTokenAutoLoginConfiguration(long companyId) {

		try {
			return _configurationProvider.getConfiguration(
				PunchoutAccessTokenAutoLoginConfiguration.class,
				new CompanyServiceSettingsLocator(
					companyId, PunchoutAutoLoginConstants.SERVICE_NAME));
		}
		catch (ConfigurationException ce) {
			_log.error(
				"Unable to get punchout access token auto login configuration",
				ce);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutAccessTokenAutoLogin.class);

	@Reference(
		target = "(&(private.auto.login=true)(type=punchout.access.token))"
	)
	private AutoLogin _autoLogin;

	private ConfigurationProvider _configurationProvider;
	private Portal _portal;

}