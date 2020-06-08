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

import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommercePunchoutConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.punchout.service.PunchoutReturnService;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true,
	property = {
		"commerce.checkout.step.name=" + PunchoutCommerceCheckoutStep.NAME,
		"commerce.checkout.step.order:Integer=" + (Integer.MAX_VALUE - 100)
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

		// TODO: check if enabled for channel -
		// TODO: it could've been turned off while punchout sessions still exist

		return _punchoutSession(httpServletRequest);
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

		HttpServletResponse httpServletResponse = themeDisplay.getResponse();

		String punchoutReturnURL = _getPunchoutReturnURL(httpServletRequest);

		CommerceOrder commerceOrder = _getCommerceOrder(actionRequest);

		String redirectURL = _punchoutReturnService.returnToPunchoutVendor(
			commerceOrder, punchoutReturnURL);

		if (!Validator.isBlank(redirectURL)) {
			_endPunchoutSession(httpServletRequest, httpServletResponse);

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

	private void _endPunchoutSession(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		CookieKeys.deleteCookies(
			httpServletRequest, httpServletResponse,
			CookieKeys.getDomain(httpServletRequest),
			CommercePunchoutConstants.PUNCHOUT_RETURN_URL_COOKIE_NAME);

		CookieKeys.deleteCookies(
			httpServletRequest, httpServletResponse,
			CookieKeys.getDomain(httpServletRequest),
			CommercePunchoutConstants.PUNCHOUT_COMMERCE_ORDER_UUID_COOKIE_NAME);
	}

	private CommerceOrder _getCommerceOrder(ActionRequest actionRequest) {
		return (CommerceOrder)actionRequest.getAttribute(
			CommerceCheckoutWebKeys.COMMERCE_ORDER);
	}

	private String _getPunchoutReturnURL(
		HttpServletRequest httpServletRequest) {

		return CookieKeys.getCookie(
			httpServletRequest,
			CommercePunchoutConstants.PUNCHOUT_RETURN_URL_COOKIE_NAME);
	}

	private boolean _punchoutSession(HttpServletRequest request) {
		String punchoutReturnURL = _getPunchoutReturnURL(request);

		return !Validator.isBlank(punchoutReturnURL);
	}

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private PunchoutReturnService _punchoutReturnService;

}