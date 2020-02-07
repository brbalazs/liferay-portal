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

package com.liferay.commerce.inventory.web.internal.frontend;

import static com.liferay.portal.kernel.security.permission.PermissionThreadLocal.getPermissionChecker;

import com.liferay.commerce.frontend.clay.data.set.ClayDataSetAction;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetActionProvider;
import com.liferay.commerce.inventory.constants.CommerceInventoryActionKeys;
import com.liferay.commerce.inventory.web.internal.model.InventoryItem;
import com.liferay.commerce.inventory.web.internal.model.Sku;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
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
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_ITEMS,
	service = ClayDataSetActionProvider.class
)
public class CommerceInventoryItemClayDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayDataSetActions = new ArrayList<>();

		InventoryItem inventoryItem = (InventoryItem)model;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (PortalPermissionUtil.contains(
				getPermissionChecker(),
				CommerceInventoryActionKeys.ADD_WAREHOUSE)) {

			Sku sku = inventoryItem.getSku();

			ClayDataSetAction clayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK,
				_getEditCommerceInventoryItemURL(sku.getLabel(), themeDisplay),
				StringPool.BLANK, LanguageUtil.get(httpServletRequest, "edit"),
				null, false, false);

			clayDataSetActions.add(clayDataSetAction);
		}

		return clayDataSetActions;
	}

	private String _getEditCommerceInventoryItemURL(
		String sku, ThemeDisplay themeDisplay) {

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		PortletURL portletURL = PortletURLFactoryUtil.create(
			themeDisplay.getRequest(), portletDisplay.getId(),
			themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommerceInventoryItem");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter("sku", String.valueOf(sku));

		return portletURL.toString();
	}

	@Reference
	private Portal _portal;

}