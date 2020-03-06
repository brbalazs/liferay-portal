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

package com.liferay.commerce.shipping.engine.fixed.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetAction;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetActionProvider;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableSchema;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaField;
import com.liferay.commerce.model.CommerceShippingMethod;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.shipping.engine.fixed.model.CommerceShippingFixedOption;
import com.liferay.commerce.shipping.engine.fixed.service.CommerceShippingFixedOptionService;
import com.liferay.commerce.shipping.engine.fixed.web.internal.model.ShippingFixedOption;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommerceShippingFixedOptionClayTable.NAME,
		"commerce.data.set.display.name=" + CommerceShippingFixedOptionClayTable.NAME
	},
	service = {
		ClayDataSetActionProvider.class, ClayDataSetDisplayView.class,
		CommerceDataSetDataProvider.class
	}
)
public class CommerceShippingFixedOptionClayTable
	extends ClayTableDataSetDisplayView
	implements ClayDataSetActionProvider,
			   CommerceDataSetDataProvider<ShippingFixedOption> {

	public static final String NAME = "shipping-fixed-options";

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayDataSetAction> clayTableActions = new ArrayList<>();

		try {
			ShippingFixedOption shippingFixedOption =
				(ShippingFixedOption)model;

			PortletURL portletURL = PortletProviderUtil.getPortletURL(
				httpServletRequest, CommerceShippingMethod.class.getName(),
				PortletProvider.Action.EDIT);

			portletURL.setParameter(
				"mvcRenderCommandName", "editCommerceShippingFixedOption");
			portletURL.setParameter(
				"commerceShippingFixedOptionId",
				String.valueOf(shippingFixedOption.getShippingFixedOptionId()));

			portletURL.setWindowState(LiferayWindowState.POP_UP);

			ClayDataSetAction clayDataSetAction = new ClayDataSetAction(
				StringPool.BLANK, portletURL.toString(), StringPool.BLANK,
				LanguageUtil.get(httpServletRequest, "edit"), null, false,
				false);

			clayDataSetAction.setTarget("sidePanel");

			clayTableActions.add(clayDataSetAction);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return clayTableActions;
	}

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commerceShippingMethodId = ParamUtil.getLong(
			httpServletRequest, "commerceShippingMethodId");

		return _commerceShippingFixedOptionService.
			getCommerceShippingFixedOptionsCount(commerceShippingMethodId);
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		ClayTableSchemaField clayTableSchemaField =
			clayTableSchemaBuilder.addField("name", "name");

		clayTableSchemaField.setContentRenderer("actionLink");

		clayTableSchemaBuilder.addField("description", "description");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public List<ShippingFixedOption> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long commerceShippingMethodId = ParamUtil.getLong(
			httpServletRequest, "commerceShippingMethodId");

		List<CommerceShippingFixedOption> commerceShippingFixedOptions =
			_commerceShippingFixedOptionService.getCommerceShippingFixedOptions(
				commerceShippingMethodId, pagination.getStartPosition(),
				pagination.getEndPosition(), null);

		List<ShippingFixedOption> shippingFixedOptions = new ArrayList<>();

		for (CommerceShippingFixedOption commerceShippingFixedOption :
				commerceShippingFixedOptions) {

			shippingFixedOptions.add(
				new ShippingFixedOption(
					HtmlUtil.escape(
						commerceShippingFixedOption.getDescription(
							themeDisplay.getLocale())),
					HtmlUtil.escape(
						commerceShippingFixedOption.getName(
							themeDisplay.getLocale())),
					commerceShippingFixedOption.
						getCommerceShippingFixedOptionId()));
		}

		return shippingFixedOptions;
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CommerceShippingFixedOptionService
		_commerceShippingFixedOptionService;

}