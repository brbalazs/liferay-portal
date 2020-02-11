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
import com.liferay.commerce.model.Shipment;
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
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceShipmentDataSetConstants.COMMERCE_DATA_SET_KEY_SHIPMENTS,
	service = ClayDataSetActionProvider.class
)
public class CommerceShipmentDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayDataSetActions = new ArrayList<>();

		Shipment shipment = (Shipment)model;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletURL editPortletURL = _portal.getControlPanelPortletURL(
			httpServletRequest, CommercePortletKeys.COMMERCE_SHIPMENT,
			PortletRequest.RENDER_PHASE);

		editPortletURL.setParameter("backURL", editPortletURL.toString());
		editPortletURL.setParameter(
			"redirect", _portal.getCurrentURL(httpServletRequest));
		editPortletURL.setParameter(
			"commerceShipmentId", String.valueOf(shipment.getShipmentId()));
		editPortletURL.setParameter(
			"mvcRenderCommandName", "editCommerceShipment");

		ClayDataSetAction viewClayDataSetAction = new ClayDataSetAction(
			StringPool.BLANK, editPortletURL.toString(), StringPool.BLANK,
			LanguageUtil.get(httpServletRequest, "edit"), StringPool.BLANK,
			false, false);

		clayDataSetActions.add(viewClayDataSetAction);

		if (PortalPermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				CommerceActionKeys.MANAGE_COMMERCE_SHIPMENTS)) {

			PortletURL deletePortletURL = _portal.getControlPanelPortletURL(
				httpServletRequest, CommercePortletKeys.COMMERCE_SHIPMENT,
				ActionRequest.ACTION_PHASE);

			deletePortletURL.setParameter(
				ActionRequest.ACTION_NAME, "editCommerceShipment");
			deletePortletURL.setParameter(Constants.CMD, Constants.DELETE);
			deletePortletURL.setParameter(
				"redirect", _portal.getCurrentURL(httpServletRequest));
			deletePortletURL.setParameter(
				"commerceShipmentId", String.valueOf(shipment.getShipmentId()));

			ClayDataSetAction deleteClayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK, deletePortletURL.toString(), StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, "delete"),
				StringPool.BLANK, false, false);

			clayDataSetActions.add(deleteClayDataSetAction);
		}

		return clayDataSetActions;
	}

	@Reference
	private Portal _portal;

}
