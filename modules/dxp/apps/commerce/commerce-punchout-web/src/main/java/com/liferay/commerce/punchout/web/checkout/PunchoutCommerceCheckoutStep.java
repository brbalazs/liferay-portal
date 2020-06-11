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

package com.liferay.commerce.checkout.web.internal.util;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.punchout.configuration.PunchoutConfiguration;
import com.liferay.commerce.punchout.constants.PunchoutConstants;
import com.liferay.commerce.punchout.service.PunchoutAccountRoleHelper;
import com.liferay.commerce.punchout.service.PunchoutReturnService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true,
	property = {
		"commerce.checkout.step.name=" + PunchoutCommerceCheckoutStep.NAME,
		"commerce.checkout.step.order:Integer=" + Integer.MIN_VALUE
	},
	service = CommerceCheckoutStep.class
)
public class PunchoutCommerceCheckoutStep extends BaseCommerceCheckoutStep {

	public static final String NAME = "punchout";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isActive(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (_punchoutEnabled(httpServletRequest) &&
			_punchoutAllowed(httpServletRequest) &&
			_punchoutSession(httpServletRequest)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isOrder() {
		return true;
	}

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		HttpServletRequest httpServletRequest = themeDisplay.getRequest();

		String punchoutReturnURL = _getPunchoutReturnURL(httpServletRequest);

		CommerceOrder commerceOrder = _getCommerceOrder(actionRequest);

		String redirectURL = _punchoutReturnService.returnToPunchoutVendor(
			commerceOrder, punchoutReturnURL);

		if (!Validator.isBlank(redirectURL)) {
			_endPunchoutSession(httpServletRequest);

			actionResponse.setProperty("redirectURL", redirectURL);
		}
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		_jspRenderer.renderJSP(
			httpServletRequest, httpServletResponse,
			"/checkout_step/punchout.jsp");
	}

	private void _endPunchoutSession(HttpServletRequest httpServletRequest) {
		HttpSession httpSession = _getHttpSession(httpServletRequest);

		httpSession.removeAttribute(
			PunchoutConstants.PUNCHOUT_RETURN_URL_SESSION_ATTRIBUTE_NAME);
	}

	private CommerceOrder _getCommerceOrder(ActionRequest actionRequest) {
		return (CommerceOrder)actionRequest.getAttribute(
			CommerceCheckoutWebKeys.COMMERCE_ORDER);
	}

	private CommerceOrder _getCommerceOrder(HttpServletRequest httpServletRequest)
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

	private HttpSession _getHttpSession(HttpServletRequest httpServletRequest) {
		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		return originalHttpServletRequest.getSession();
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

	private String _getPunchoutReturnURL(
		HttpServletRequest httpServletRequest) {

		HttpSession httpSession = _getHttpSession(httpServletRequest);

		Object punchoutReturnUrlObject = httpSession.getAttribute(
			PunchoutConstants.PUNCHOUT_RETURN_URL_SESSION_ATTRIBUTE_NAME);

		if (punchoutReturnUrlObject == null) {
			return null;
		}

		return (String)punchoutReturnUrlObject;
	}

	private boolean _punchoutAllowed(HttpServletRequest httpServletRequest) {
		try {
			CommerceOrder commerceOrder = _getCommerceOrder(httpServletRequest);

			CommerceContext commerceContext =
				(CommerceContext)httpServletRequest.getAttribute(
					CommerceWebKeys.COMMERCE_CONTEXT);

			CommerceAccount commerceAccount =
				commerceContext.getCommerceAccount();

			return _punchoutAccountRoleHelper.hasPunchoutRole(
				commerceOrder.getCompanyId(), commerceOrder.getUserId(),
				commerceAccount.getCommerceAccountId());
		}
		catch (Exception e) {
			_log.error(
				"Failed to determine whether user has Punchout role under " +
					"commerce account");

			return false;
		}
	}

	private boolean _punchoutEnabled(HttpServletRequest httpServletRequest) {
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

	private boolean _punchoutSession(HttpServletRequest request) {
		String punchoutReturnURL = _getPunchoutReturnURL(request);

		return !Validator.isBlank(punchoutReturnURL);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutCommerceCheckoutStep.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Portal _portal;

	@Reference
	private PunchoutAccountRoleHelper _punchoutAccountRoleHelper;

	@Reference
	private PunchoutReturnService _punchoutReturnService;

}