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

package com.liferay.commerce.shipping.engine.fixed.web.internal.display.context;

import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuActionItem;
import com.liferay.commerce.model.CommerceShippingMethod;
import com.liferay.commerce.service.CommerceShippingMethodService;
import com.liferay.commerce.shipping.engine.fixed.constants.CommerceShippingEngineFixedWebKeys;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOption;
import com.liferay.commerce.shipping.engine.fixed.service.CommerceShippingFixedOptionService;
import com.liferay.commerce.shipping.engine.fixed.web.internal.FixedCommerceShippingEngine;
import com.liferay.commerce.shipping.engine.fixed.web.internal.servlet.taglib.ui.CommerceShippingMethodFixedOptionsScreenNavigationEntry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;

import java.math.BigDecimal;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceShippingFixedOptionsDisplayContext
	extends BaseCommerceShippingFixedOptionDisplayContext
		<CommerceShippingFixedOption> {

	public CommerceShippingFixedOptionsDisplayContext(
		CommerceCurrencyLocalService commerceCurrencyLocalService,
		CommercePriceFormatter commercePriceFormatter,
		CommerceShippingMethodService commerceShippingMethodService,
		CommerceShippingFixedOptionService commerceShippingFixedOptionService,
		Portal portal, PortletResourcePermission portletResourcePermission,
		RenderRequest renderRequest, RenderResponse renderResponse) {

		super(
			commerceCurrencyLocalService, commerceShippingMethodService,
			portletResourcePermission, renderRequest, renderResponse);

		_commercePriceFormatter = commercePriceFormatter;
		_commerceShippingFixedOptionService =
			commerceShippingFixedOptionService;
		_portal = portal;
	}

	public String getAddShippingFixedOptionURL() throws Exception {
		PortletURL editCommerceChannelPortletURL =
			_portal.getControlPanelPortletURL(
				renderRequest, CommercePortletKeys.COMMERCE_SHIPPING_METHODS,
				PortletRequest.RENDER_PHASE);

		editCommerceChannelPortletURL.setParameter(
			"mvcRenderCommandName", "editCommerceShippingFixedOption");

		editCommerceChannelPortletURL.setParameter(
			"commerceShippingMethodId",
			String.valueOf(getCommerceShippingMethodId()));

		editCommerceChannelPortletURL.setWindowState(LiferayWindowState.POP_UP);

		return editCommerceChannelPortletURL.toString();
	}

	public ClayCreationMenu getClayCreationMenu() throws Exception {
		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		clayCreationMenu.addClayCreationMenuActionItem(
			getAddShippingFixedOptionURL(),
			LanguageUtil.get(themeDisplay.getLocale(), "add-shipping-option"),
			ClayCreationMenuActionItem.CLAY_MENU_ACTION_ITEM_TARGET_SIDE_PANEL);

		return clayCreationMenu;
	}

	public CommerceShippingFixedOption getCommerceShippingFixedOption()
		throws PortalException {

		CommerceShippingFixedOption commerceShippingFixedOption =
			(CommerceShippingFixedOption)renderRequest.getAttribute(
				CommerceShippingEngineFixedWebKeys.
					COMMERCE_SHIPPING_FIXED_OPTION);

		if (commerceShippingFixedOption != null) {
			return commerceShippingFixedOption;
		}

		long commerceShippingFixedOptionId = ParamUtil.getLong(
			renderRequest, "commerceShippingFixedOptionId");

		commerceShippingFixedOption =
			_commerceShippingFixedOptionService.
				fetchCommerceShippingFixedOption(commerceShippingFixedOptionId);

		renderRequest.setAttribute(
			CommerceShippingEngineFixedWebKeys.COMMERCE_SHIPPING_FIXED_OPTION,
			commerceShippingFixedOption);

		return commerceShippingFixedOption;
	}

	public String getCommerceShippingFixedOptionAmount(BigDecimal amount)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return _commercePriceFormatter.format(
			themeDisplay.getCompanyId(), amount, themeDisplay.getLocale());
	}

	public String getEditURL(String functionName, boolean isNew, String url) {
		StringBundler sb = new StringBundler(11);

		sb.append("javascript:");
		sb.append(renderResponse.getNamespace());
		sb.append(functionName);
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(isNew);
		sb.append(StringPool.COMMA_AND_SPACE);
		sb.append(StringPool.APOSTROPHE);
		sb.append(HtmlUtil.escapeJS(url));
		sb.append(StringPool.APOSTROPHE);
		sb.append(StringPool.CLOSE_PARENTHESIS);
		sb.append(StringPool.SEMICOLON);

		return sb.toString();
	}

	@Override
	public String getScreenNavigationCategoryKey() {
		return CommerceShippingMethodFixedOptionsScreenNavigationEntry.
			CATEGORY_KEY;
	}

	public boolean isFixed() throws PortalException {
		CommerceShippingMethod commerceShippingMethod =
			getCommerceShippingMethod();

		if (commerceShippingMethod == null) {
			CommerceShippingFixedOption commerceShippingFixedOption =
				getCommerceShippingFixedOption();

			commerceShippingMethod =
				commerceShippingMethodService.getCommerceShippingMethod(
					commerceShippingFixedOption.getCommerceShippingMethodId());
		}

		String engineKey = commerceShippingMethod.getEngineKey();

		if (engineKey.equals(FixedCommerceShippingEngine.KEY)) {
			return true;
		}

		return false;
	}

	private final CommercePriceFormatter _commercePriceFormatter;
	private final CommerceShippingFixedOptionService
		_commerceShippingFixedOptionService;
	private final Portal _portal;

}