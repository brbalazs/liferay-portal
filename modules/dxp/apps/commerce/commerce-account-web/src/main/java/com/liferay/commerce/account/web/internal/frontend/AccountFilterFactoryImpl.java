/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.account.web.internal.frontend;

import com.liferay.commerce.account.configuration.CommerceAccountGroupServiceConfiguration;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.FilterFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommerceAccountClayTable.NAME,
		"commerce.data.provider.key=" + CommerceAccountOrganizationClayTable.NAME,
		"commerce.data.provider.key=" + CommerceAccountUserClayTable.NAME
	},
	service = FilterFactory.class
)
public class AccountFilterFactoryImpl implements FilterFactory {

	@Override
	public Filter create(HttpServletRequest httpServletRequest) {
		AccountFilterImpl accountFilter = new AccountFilterImpl();

		long commerceAccountId = ParamUtil.getLong(
			httpServletRequest, "commerceAccountId");

		accountFilter.setAccountId(commerceAccountId);

		String keywords = ParamUtil.getString(httpServletRequest, "q");

		accountFilter.setKeywords(keywords);

		try {
			CommerceAccountGroupServiceConfiguration
				commerceAccountGroupServiceConfiguration =
					_configurationProvider.getGroupConfiguration(
						CommerceAccountGroupServiceConfiguration.class,
						_portal.getScopeGroupId(httpServletRequest));

			accountFilter.setCommerceSiteType(
				commerceAccountGroupServiceConfiguration.commerceSiteType());
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.error(pe, pe);
			}
		}

		return accountFilter;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AccountFilterFactoryImpl.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

}