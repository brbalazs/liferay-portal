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

package com.liferay.commerce.pricing.web.internal.display.context;

import com.liferay.commerce.frontend.clay.data.set.ClayHeadlessDataSetActionTemplate;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public abstract class BaseCommercePriceListDisplayContext
	extends BasePricingDisplayContext {

	public BaseCommercePriceListDisplayContext(
		ModelResourcePermission<CommercePriceList>
			commercePriceListModelResourcePermission,
		CommercePriceListService commercePriceListService,
		HttpServletRequest httpServletRequest) {

		super(httpServletRequest);

		this.commercePriceListModelResourcePermission =
			commercePriceListModelResourcePermission;
		this.commercePriceListService = commercePriceListService;
	}

	public CommercePriceList getCommercePriceList() throws PortalException {
		if (commercePriceList != null) {
			return commercePriceList;
		}

		long commercePriceListId = ParamUtil.getLong(
			commercePricingRequestHelper.getRequest(), "commercePriceListId");

		if (commercePriceListId > 0) {
			commercePriceList = commercePriceListService.getCommercePriceList(
				commercePriceListId);
		}

		return commercePriceList;
	}

	public long getCommercePriceListId() throws PortalException {
		CommercePriceList commercePriceList = getCommercePriceList();

		if (commercePriceList == null) {
			return 0;
		}

		return commercePriceList.getCommercePriceListId();
	}

	public boolean hasPermission(long commercePriceListId, String actionId)
		throws PortalException {

		return commercePriceListModelResourcePermission.contains(
			commercePricingRequestHelper.getPermissionChecker(),
			commercePriceListId, actionId);
	}

	public boolean hasPermission(String actionId) {
		return PortalPermissionUtil.contains(
			commercePricingRequestHelper.getPermissionChecker(), actionId);
	}

	protected List<ClayHeadlessDataSetActionTemplate>
		getClayHeadlessDataSetActionTemplates(
			String portletURL, boolean isSidePanel) {

		List<ClayHeadlessDataSetActionTemplate>
			clayHeadlessDataSetActionTemplates = new ArrayList<>();

		ClayHeadlessDataSetActionTemplate clayHeadlessDataSetActionTemplate =
			new ClayHeadlessDataSetActionTemplate(
				portletURL, "view", "view",
				LanguageUtil.get(httpServletRequest, "view"), "get", null,
				null);

		if (isSidePanel) {
			clayHeadlessDataSetActionTemplate.setTarget("sidePanel");
		}

		clayHeadlessDataSetActionTemplates.add(
			clayHeadlessDataSetActionTemplate);

		clayHeadlessDataSetActionTemplates.add(
			new ClayHeadlessDataSetActionTemplate(
				null, "trash", "delete",
				LanguageUtil.get(httpServletRequest, "delete"), "delete",
				"delete", "headless"));

		return clayHeadlessDataSetActionTemplates;
	}

	protected CommercePriceList commercePriceList;
	protected final ModelResourcePermission<CommercePriceList>
		commercePriceListModelResourcePermission;
	protected CommercePriceListService commercePriceListService;

}