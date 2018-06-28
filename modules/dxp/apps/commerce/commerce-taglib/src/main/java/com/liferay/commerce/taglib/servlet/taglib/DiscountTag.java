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

package com.liferay.commerce.taglib.servlet.taglib;

import com.liferay.commerce.configuration.CommercePriceConfiguration;
import com.liferay.commerce.constants.CPDefinitionInventoryConstants;
import com.liferay.commerce.constants.CommerceConstants;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CPDefinitionInventory;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalServiceUtil;
import com.liferay.commerce.service.CPDefinitionInventoryServiceUtil;
import com.liferay.commerce.taglib.servlet.taglib.internal.servlet.ServletContextUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.settings.SystemSettingsLocator;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Alessio Antonio Rendina
 */
public class DiscountTag extends IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletResourcePermission cpPortletResourcePermission =
			ServletContextUtil.getCPPortletResourcePermission();

		if (!cpPortletResourcePermission.contains(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(), CPActionKeys.VIEW_PRICE)) {

			return super.doStartTag();
		}

		try {
			CommerceContext commerceContext =
				(CommerceContext)request.getAttribute(
					CommerceWebKeys.COMMERCE_CONTEXT);

			CPInstance cpInstance = CPInstanceLocalServiceUtil.getCPInstance(
				_cpInstanceId);

			if (_quantity <= 0) {
				CPDefinitionInventory cpDefinitionInventory =
					CPDefinitionInventoryServiceUtil.
						fetchCPDefinitionInventoryByCPDefinitionId(
							cpInstance.getCPDefinitionId());

				if (cpDefinitionInventory != null) {
					_quantity = cpDefinitionInventory.getMinOrderQuantity();
				}
				else {
					_quantity =
						CPDefinitionInventoryConstants.
							DEFAULT_MULTIPLE_ORDER_QUANTITY;
				}
			}

			CommerceProductPrice commerceProductPrice =
				commerceProductPriceCalculation.getCommerceProductPrice(
					_cpInstanceId, _quantity, commerceContext);

			_commerceDiscountValue = commerceProductPrice.getDiscountValue();

			CommercePriceConfiguration commercePriceConfiguration =
				configurationProvider.getConfiguration(
					CommercePriceConfiguration.class,
					new SystemSettingsLocator(
						CommerceConstants.PRICE_SERVICE_NAME));

			_displayDiscountLevels =
				commercePriceConfiguration.displayDiscountLevels();
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			return SKIP_BODY;
		}

		return super.doStartTag();
	}

	public void setCPInstanceId(long cpInstanceId) {
		_cpInstanceId = cpInstanceId;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		commerceProductPriceCalculation =
			ServletContextUtil.getCommercePriceCalculation();
		configurationProvider = ServletContextUtil.getConfigurationProvider();
		servletContext = ServletContextUtil.getServletContext();
	}

	public void setQuantity(int quantity) {
		_quantity = quantity;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_cpInstanceId = 0;
		_quantity = 0;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		request.setAttribute(
			"liferay-commerce:discount:commerceDiscountValue",
			_commerceDiscountValue);
		request.setAttribute(
			"liferay-commerce:discount:displayDiscountLevels",
			_displayDiscountLevels);
	}

	protected CommerceProductPriceCalculation commerceProductPriceCalculation;
	protected ConfigurationProvider configurationProvider;

	private static final String _PAGE = "/discount/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(DiscountTag.class);

	private CommerceDiscountValue _commerceDiscountValue;
	private long _cpInstanceId;
	private boolean _displayDiscountLevels;
	private int _quantity;

}