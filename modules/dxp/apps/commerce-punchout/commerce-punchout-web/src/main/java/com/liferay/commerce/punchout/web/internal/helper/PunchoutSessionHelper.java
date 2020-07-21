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

package com.liferay.commerce.punchout.web.internal.helper;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.punchout.configuration.PunchoutConfiguration;
import com.liferay.commerce.punchout.constants.PunchoutConstants;
import com.liferay.commerce.punchout.service.PunchoutAccountRoleHelper;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(immediate = true, service = PunchoutSessionHelper.class)
public class PunchoutSessionHelper {

	public HttpSession getHttpSession(HttpServletRequest httpServletRequest) {
		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		return originalHttpServletRequest.getSession();
	}

	public String getPunchoutReturnURL(HttpServletRequest httpServletRequest) {
		HttpSession httpSession = getHttpSession(httpServletRequest);

		Object punchoutReturnUrlObject = httpSession.getAttribute(
			PunchoutConstants.PUNCHOUT_RETURN_URL_ATTRIBUTE_NAME);

		if (punchoutReturnUrlObject == null) {
			return null;
		}

		return (String)punchoutReturnUrlObject;
	}

	public boolean punchoutAllowed(HttpServletRequest httpServletRequest) {
		try {
			CommerceOrder commerceOrder = _getCommerceOrder(httpServletRequest);

			if (commerceOrder == null) {
				return false;
			}

			CommerceContext commerceContext =
				(CommerceContext)httpServletRequest.getAttribute(
					CommerceWebKeys.COMMERCE_CONTEXT);

			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			return _punchoutAccountRoleHelper.hasPunchoutRole(
				commerceOrder.getUserId(),
				commerceAccount.getCommerceAccountId());
		}
		catch (Exception e) {
			_log.error(
				"Failed to determine whether user has Punchout role under " +
					"commerce account");

			return false;
		}
	}

	public boolean punchoutEnabled(HttpServletRequest httpServletRequest) {
		try {
			CommerceContext commerceContext =
				(CommerceContext)httpServletRequest.getAttribute(
					CommerceWebKeys.COMMERCE_CONTEXT);

			long commerceChannelGroupId =
				commerceContext.getCommerceChannelGroupId();

			if (commerceChannelGroupId == 0L) {
				return false;
			}

			PunchoutConfiguration punchoutConfiguration =
				_getPunchoutConfiguration(commerceChannelGroupId);

			if (punchoutConfiguration != null) {
				return punchoutConfiguration.enabled();
			}
		}
		catch (Exception e) {
			_log.error("Failed to load punchout configuration", e);
		}

		return false;
	}

	public boolean punchoutSession(HttpServletRequest request) {
		String punchoutReturnURL = getPunchoutReturnURL(request);

		return !Validator.isBlank(punchoutReturnURL);
	}

	private CommerceOrder _getCommerceOrder(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		String commerceOrderUuid = ParamUtil.getString(
			httpServletRequest, "commerceOrderUuid");

		if (Validator.isNotNull(commerceOrderUuid)) {
			long groupId =
				_commerceChannelLocalService.
					getCommerceChannelGroupIdBySiteGroupId(
						_portal.getScopeGroupId(httpServletRequest));

			return _commerceOrderService.getCommerceOrderByUuidAndGroupId(
				commerceOrderUuid, groupId);
		}

		return _commerceOrderHttpHelper.getCurrentCommerceOrder(
			httpServletRequest);
	}

	private PunchoutConfiguration _getPunchoutConfiguration(
		long channelGroupId) {

		try {
			return _configurationProvider.getConfiguration(
				PunchoutConfiguration.class,
				new GroupServiceSettingsLocator(
					channelGroupId, PunchoutConstants.SERVICE_NAME));
		}
		catch (ConfigurationException ce) {
			_log.error("Unable to get punchout configuration", ce);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutSessionHelper.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

	@Reference
	private PunchoutAccountRoleHelper _punchoutAccountRoleHelper;

}