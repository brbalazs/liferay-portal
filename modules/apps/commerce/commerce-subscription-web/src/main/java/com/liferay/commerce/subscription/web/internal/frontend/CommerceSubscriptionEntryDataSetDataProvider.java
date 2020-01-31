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

package com.liferay.commerce.subscription.web.internal.frontend;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceSubscriptionEntryConstants;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.commerce.subscription.web.internal.model.Label;
import com.liferay.commerce.subscription.web.internal.model.Link;
import com.liferay.commerce.subscription.web.internal.model.SubscriptionEntry;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_ENTRIES,
	service = CommerceDataSetDataProvider.class
)
public class CommerceSubscriptionEntryDataSetDataProvider
	implements CommerceDataSetDataProvider<SubscriptionEntry> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		BaseModelSearchResult<CommerceSubscriptionEntry> baseModelSearchResult =
			_getBaseModelSearchResult(httpServletRequest, filter, null);

		return baseModelSearchResult.getLength();
	}

	@Override
	public List<SubscriptionEntry> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<SubscriptionEntry> subscriptionEntries = new ArrayList<>();

		BaseModelSearchResult<CommerceSubscriptionEntry> baseModelSearchResult =
			_getBaseModelSearchResult(httpServletRequest, filter, pagination);

		for (CommerceSubscriptionEntry commerceSubscriptionEntry :
				baseModelSearchResult.getBaseModels()) {

			CommerceOrderItem commerceOrderItem =
				_commerceOrderItemLocalService.getCommerceOrderItem(
					commerceSubscriptionEntry.getCommerceOrderItemId());

			String commerceOrderIdString = String.valueOf(
				commerceOrderItem.getCommerceOrderId());

			CommerceOrder commerceOrder =
				_commerceOrderLocalService.getCommerceOrder(
					commerceOrderItem.getCommerceOrderId());

			CommerceAccount commerceAccount =
				commerceOrder.getCommerceAccount();

			String commerceAccountIdString = String.valueOf(
				commerceAccount.getCommerceAccountId());

			SubscriptionEntry subscriptionEntry = new SubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId(),
				new Link(
					commerceOrderIdString,
					_getEditCommerceOrderURL(
						commerceOrder.getCommerceOrderId(), httpServletRequest)),
				new Link(commerceAccountIdString,
					_getEditAccountURL(
						commerceAccount.getCommerceAccountId(),
						httpServletRequest)),
				_getSubscriptionStatus(commerceSubscriptionEntry),
				commerceAccount.getName());

			subscriptionEntries.add(subscriptionEntry);
		}

		return subscriptionEntries;
	}

	private String _getEditCommerceOrderURL(long commerceOrderId, HttpServletRequest httpServletRequest)
		throws PortalException {

		CPRequestHelper cpRequestHelper = new CPRequestHelper(httpServletRequest);

		ThemeDisplay themeDisplay = cpRequestHelper.getThemeDisplay();

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, themeDisplay.getScopeGroup(),
			CommerceOrder.class.getName(), PortletProvider.Action.MANAGE);

		portletURL.setParameter("mvcRenderCommandName", "editCommerceOrder");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter("commerceOrderId", String.valueOf(commerceOrderId));

		return portletURL.toString();
	}

	private String _getEditAccountURL(long commerceAccountId, HttpServletRequest httpServletRequest)
		throws PortalException {

		CPRequestHelper cpRequestHelper = new CPRequestHelper(httpServletRequest);

		ThemeDisplay themeDisplay = cpRequestHelper.getThemeDisplay();

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, themeDisplay.getScopeGroup(),
			CommerceAccount.class.getName(), PortletProvider.Action.MANAGE);

		portletURL.setParameter("mvcRenderCommandName", "editCommerceAccount");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter("commerceAccountId", String.valueOf(commerceAccountId));

		return portletURL.toString();
	}

	private BaseModelSearchResult<CommerceSubscriptionEntry>
			_getBaseModelSearchResult(
				HttpServletRequest httpServletRequest, Filter filter,
				Pagination pagination)
		throws PortalException {

		BaseModelSearchResult<CommerceSubscriptionEntry> baseModelSearchResult =
			null;

		int start = QueryUtil.ALL_POS;
		int end = QueryUtil.ALL_POS;

		if (pagination != null) {
			start = pagination.getStartPosition();
			end = pagination.getEndPosition();
		}

		_setSortPreferences(httpServletRequest);

		Sort sort = SortFactoryUtil.getSort(
			CommerceSubscriptionEntry.class, _orderByCol, _orderByType);

		SubscriptionEntryFilterImpl subscriptionEntryFilterImpl =
			(SubscriptionEntryFilterImpl)filter;

		long companyId = ParamUtil.getLong(httpServletRequest, "companyId");

		if (subscriptionEntryFilterImpl.isAdvancedSearch()) {
			baseModelSearchResult =
				_commerceSubscriptionEntryLocalService.
					searchCommerceSubscriptionEntries(
						companyId,
						subscriptionEntryFilterImpl.
							getPaymentSubscriptionRemainingCycles(),
						subscriptionEntryFilterImpl.
							getPaymentSubscriptionStatus(),
						subscriptionEntryFilterImpl.getKeywords(), start, end,
						sort);
		}
		else {
			baseModelSearchResult =
				_commerceSubscriptionEntryLocalService.
					searchCommerceSubscriptionEntries(
						companyId, null, null,
						subscriptionEntryFilterImpl.getKeywords(), start, end,
						sort);
		}

		return baseModelSearchResult;
	}

	private Label _getSubscriptionStatus(
		CommerceSubscriptionEntry commerceSubscriptionEntry) {

		if (Objects.equals(
				commerceSubscriptionEntry.getSubscriptionStatus(),
				CommerceSubscriptionEntryConstants.
					SUBSCRIPTION_STATUS_ACTIVE)) {

			return new Label("Active", Label.SUCCESS);
		}
		else if (Objects.equals(
					commerceSubscriptionEntry.getSubscriptionStatus(),
					CommerceSubscriptionEntryConstants.
						SUBSCRIPTION_STATUS_SUSPENDED)) {

			return new Label("Suspended", Label.WARNING);
		}
		else if (Objects.equals(
					commerceSubscriptionEntry.getSubscriptionStatus(),
					CommerceSubscriptionEntryConstants.
						SUBSCRIPTION_STATUS_CANCELLED)) {

			return new Label("Cancelled", Label.DANGER);
		}
		else if (Objects.equals(
					commerceSubscriptionEntry.getSubscriptionStatus(),
					CommerceSubscriptionEntryConstants.
						SUBSCRIPTION_STATUS_COMPLETED)) {

			return new Label("Active", Label.INFO);
		}

		return null;
	}

	private void _setOrderByCol(String orderByCol) {
		_orderByCol = orderByCol;
	}

	private void _setOrderByType(String orderByType) {
		_orderByType = orderByType;
	}

	private void _setSortPreferences(HttpServletRequest httpServletRequest) {
		PortalPreferences preferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(
				httpServletRequest);

		String orderByCol = ParamUtil.getString(
			httpServletRequest, "orderByCol");
		String orderByType = ParamUtil.getString(
			httpServletRequest, "orderByType");

		if (Validator.isNotNull(orderByCol) &&
			Validator.isNotNull(orderByType)) {

			preferences.setValue(
				"", "commerce-subscription-order-by-col", orderByCol);
			preferences.setValue(
				"", "commerce-subscription-order-by-type", orderByType);
		}
		else {
			orderByCol = preferences.getValue(
				"", "commerce-subscription-order-by-col", "sku");
			orderByType = preferences.getValue(
				"", "commerce-subscription-order-by-type", "asc");
		}

		_setOrderByCol(orderByCol);
		_setOrderByType(orderByType);
	}

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;

	private String _orderByCol;
	private String _orderByType;

}