/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auto.login.request.parameter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.security.auto.login.BaseAutoLogin;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.auto.login.internal.request.parameter.constants.RequestParameterAutoLoginConstants;
import com.liferay.portal.security.auto.login.request.parameter.module.configuration.RequestParameterAutoLoginConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Minhchau Dang
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.portal.security.auto.login.request.parameter.module.configuration.RequestParameterAutoLoginConfiguration",
	immediate = true, property = "type=request.parameter",
	service = AutoLogin.class
)
public class RequestParameterAutoLogin extends BaseAutoLogin {

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
		RequestParameterAutoLoginConfiguration
			requestParameterAutoLoginConfiguration =
				_getRequestParameterAutoLoginConfiguration(companyId);

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

	private RequestParameterAutoLoginConfiguration
		_getRequestParameterAutoLoginConfiguration(long companyId) {

		try {
			return _configurationProvider.getConfiguration(
				RequestParameterAutoLoginConfiguration.class,
				new CompanyServiceSettingsLocator(
					companyId,
					RequestParameterAutoLoginConstants.SERVICE_NAME));
		}
		catch (ConfigurationException ce) {
			_log.error(
				"Unable to get request parameter auto login configuration", ce);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RequestParameterAutoLogin.class);

	@Reference(target = "(&(private.auto.login=true)(type=request.parameter))")
	private AutoLogin _autoLogin;

	private ConfigurationProvider _configurationProvider;
	private Portal _portal;

}