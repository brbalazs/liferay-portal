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
import com.liferay.commerce.pricing.constants.CommercePricingClassActionKeys;
import com.liferay.commerce.pricing.web.internal.frontend.constants.CommercePricingClassDataSetConstants;
import com.liferay.commerce.pricing.web.internal.model.PricingClassCPDefinitionRel;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS,
	service = ClayDataSetActionProvider.class
)
public class CommercePricingClassCPDefinitionRelDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayDataSetActions = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (PortalPermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				CommercePricingClassActionKeys.
					MANAGE_COMMERCE_PRICING_CLASSES)) {

			PricingClassCPDefinitionRel pricingClassCPDefinitionRel =
				(PricingClassCPDefinitionRel)model;

			PortletURL editURL = _getCPDefinitionEditURL(
				pricingClassCPDefinitionRel.getCPDefinitionId(),
				httpServletRequest);

			ClayDataSetAction editClayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK, editURL.toString(), StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, Constants.EDIT),
				StringPool.BLANK, false, false);

			clayDataSetActions.add(editClayDataSetAction);

			ClayDataSetAction deleteClayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK,
				_getPricingClassCPDefinitionRelDeleteURL(
					pricingClassCPDefinitionRel.
						getPricingClassCPDefinitionRelId()),
				StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, Constants.DELETE),
				StringPool.BLANK, false, false);

			deleteClayDataSetAction.setTarget("async");

			deleteClayDataSetAction.setMethod("delete");

			clayDataSetActions.add(deleteClayDataSetAction);
		}

		return clayDataSetActions;
	}

	private PortletURL _getCPDefinitionEditURL(
			long cpDefinitionId, HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CPDefinition.class.getName(),
			PortletProvider.Action.MANAGE);

		portletURL.setParameter(
			"mvcRenderCommandName", "editProductDefinition");
		portletURL.setParameter(
			"cpDefinitionId", String.valueOf(cpDefinitionId));
		portletURL.setParameter("screenNavigationCategoryKey", "details");

		return portletURL;
	}

	private String _getPricingClassCPDefinitionRelDeleteURL(
		long pricingClassCPDefinitionRelId) {

		return "/o/headless-commerce-admin-catalog/v1.0" +
			"/product-group-products/" + pricingClassCPDefinitionRelId;
	}

	@Reference
	private Portal _portal;

}