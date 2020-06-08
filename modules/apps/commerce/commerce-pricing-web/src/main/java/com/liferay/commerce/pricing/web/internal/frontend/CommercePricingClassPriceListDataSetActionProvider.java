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

package com.liferay.commerce.pricing.web.internal.frontend;

import com.liferay.commerce.frontend.clay.data.set.ClayDataSetAction;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetActionProvider;
import com.liferay.commerce.price.list.constants.CommercePriceListPortletKeys;
import com.liferay.commerce.pricing.constants.CommercePricingClassActionKeys;
import com.liferay.commerce.pricing.web.internal.model.PricingClassPriceList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRICE_LISTS,
	service = ClayDataSetActionProvider.class
)
public class CommercePricingClassPriceListDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayDataSetActions = new ArrayList<>();

		PricingClassPriceList pricingClassPriceList =
			(PricingClassPriceList)model;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (PortalPermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				CommercePricingClassActionKeys.
					MANAGE_COMMERCE_PRICING_CLASSES)) {

			PortletURL editURL = _getPriceListEditURL(
				pricingClassPriceList.getCommercePriceListId(),
				httpServletRequest);

			ClayDataSetAction editClayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK, editURL.toString(), StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, Constants.EDIT),
				StringPool.BLANK, false, false);

			clayDataSetActions.add(editClayDataSetAction);
		}

		return clayDataSetActions;
	}

	private PortletURL _getPriceListEditURL(
		long commercePriceListId, HttpServletRequest httpServletRequest) {

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			httpServletRequest,
			CommercePriceListPortletKeys.COMMERCE_PRICE_LIST,
			PortletRequest.RENDER_PHASE);

		String redirect = ParamUtil.getString(
			httpServletRequest, "currentUrl",
			_portal.getCurrentURL(httpServletRequest));

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommercePriceList");
		portletURL.setParameter(
			"commercePriceListId", String.valueOf(commercePriceListId));
		portletURL.setParameter("redirect", redirect);

		return portletURL;
	}

	@Reference
	private Portal _portal;

}