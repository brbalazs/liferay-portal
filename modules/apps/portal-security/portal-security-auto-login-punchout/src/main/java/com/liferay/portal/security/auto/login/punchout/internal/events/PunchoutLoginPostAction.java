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

package com.liferay.portal.security.auto.login.punchout.internal.events;

import com.liferay.commerce.constants.CommercePunchoutConstants;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.oauth2.provider.punchout.model.PunchoutAccessToken;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true, property = "key=login.events.post",
	service = LifecycleAction.class
)
public class PunchoutLoginPostAction extends Action {

	@Override
	public void run(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			Object punchoutAccessTokenObject = httpServletRequest.getAttribute(
				"punchoutAccessToken");

			Object punchoutUserIdObject = httpServletRequest.getAttribute(
				"punchoutUserId");

			if ((punchoutAccessTokenObject == null) ||
				(punchoutUserIdObject == null)) {

				return;
			}

			PunchoutAccessToken punchoutAccessToken =
				(PunchoutAccessToken)httpServletRequest.getAttribute(
					"punchoutAccessToken");

			long punchoutUserId = (long)httpServletRequest.getAttribute(
				"punchoutUserId");

			long companyId = _portal.getCompanyId(httpServletRequest);

			_startNewPunchoutSession(
				companyId, punchoutAccessToken.getGroupId(),
				punchoutAccessToken.getCommerceAccountId(),
				punchoutAccessToken.getCurrencyCode(), punchoutAccessToken,
				punchoutUserId, httpServletRequest, httpServletResponse);

			httpServletRequest.removeAttribute("punchoutAccessToken");

			httpServletRequest.removeAttribute("punchoutUserId");
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private void _addPunchoutCookie(
		String name, String value, HttpServletRequest request,
		HttpServletResponse response) {

		Cookie cookie = new Cookie(name, value);

		String domain = CookieKeys.getDomain(request);

		if (Validator.isNotNull(domain)) {
			cookie.setDomain(domain);
		}

		cookie.setMaxAge(CookieKeys.MAX_AGE);
		cookie.setPath(StringPool.SLASH);

		CookieKeys.addCookie(request, response, cookie);
	}

	private ThemeDisplay _getThemeDisplay() {
		return new ThemeDisplay() {
			{
				setSignedIn(true);
			}
		};
	}

	private void _startNewPunchoutSession(
			long companyId, long groupId, long commerceAccountId,
			String currencyCode, PunchoutAccessToken punchoutAccessToken,
			long punchoutUserId, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws PortalException {

		long commerceCurrencyId = 0;

		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				companyId, currencyCode);

		if (commerceCurrency != null) {
			commerceCurrencyId = commerceCurrency.getCommerceCurrencyId();
		}

		long commerceChannelGroupId =
			_commerceChannelLocalService.getCommerceChannelGroupIdBySiteGroupId(
				groupId);

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				punchoutUserId, commerceChannelGroupId, commerceAccountId,
				commerceCurrencyId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			companyId, commerceChannelGroupId, punchoutUserId,
			commerceOrder.getCommerceOrderId(), commerceAccountId);

		httpServletRequest.setAttribute(
			CommerceWebKeys.COMMERCE_CONTEXT, commerceContext);

		ThemeDisplay themeDisplay = _getThemeDisplay();

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		_commerceOrderHttpHelper.setCurrentCommerceOrder(
			httpServletRequest, commerceOrder);

		_addPunchoutCookie(
			CommercePunchoutConstants.PUNCHOUT_RETURN_URL_COOKIE_NAME,
			punchoutAccessToken.getPunchoutReturnURL(), httpServletRequest,
			httpServletResponse);

		_addPunchoutCookie(
			CommercePunchoutConstants.PUNCHOUT_COMMERCE_ORDER_UUID_COOKIE_NAME,
			commerceOrder.getUuid(), httpServletRequest, httpServletResponse);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutLoginPostAction.class);

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private Portal _portal;

}