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

package com.liferay.commerce.shipment.web.internal.frontend;

import com.liferay.commerce.constants.CommerceActionKeys;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.constants.CommerceShipmentDataSetConstants;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetAction;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetActionProvider;
import com.liferay.commerce.model.ShipmentItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceShipmentDataSetConstants.COMMERCE_DATA_SET_KEY_SHIPMENT_ITEMS,
	service = ClayDataSetActionProvider.class
)
public class CommerceShipmentItemDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayDataSetActions = new ArrayList<>();

		ShipmentItem shipmentItem = (ShipmentItem)model;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (PortalPermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				CommerceActionKeys.MANAGE_COMMERCE_SHIPMENTS)) {

			PortletURL portletURL = _portal.getControlPanelPortletURL(
				httpServletRequest, CommercePortletKeys.COMMERCE_SHIPMENT,
				PortletRequest.ACTION_PHASE);

			portletURL.setParameter(
				ActionRequest.ACTION_NAME, "editCommerceShipmentItem");
			portletURL.setParameter(
				"redirect", _portal.getCurrentURL(httpServletRequest));
			portletURL.setParameter(
				"commerceShipmentItemId",
				String.valueOf(shipmentItem.getShipmentItemId()));

			ClayDataSetAction editClayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK, portletURL.toString(), StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, "edit"), StringPool.BLANK,
				false, false);

			editClayDataSetAction.setTarget("sidePanel");

			clayDataSetActions.add(editClayDataSetAction);

			portletURL.setParameter(Constants.CMD, Constants.DELETE);

			clayDataSetActions.add(
				new ClayDataSetAction(
					StringPool.BLANK, portletURL.toString(), StringPool.BLANK,
					LanguageUtil.get(httpServletRequest, "delete"),
					StringPool.BLANK, false, false));
		}

		return clayDataSetActions;
	}

	@Reference
	private Portal _portal;

}