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

import com.liferay.commerce.constants.CommerceShipmentConstants;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceShipment;
import com.liferay.commerce.model.CommerceShipmentItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceShipmentItemService;
import com.liferay.commerce.service.CommerceShipmentService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.commerce.subscription.web.internal.model.Label;
import com.liferay.commerce.subscription.web.internal.model.Link;
import com.liferay.commerce.subscription.web.internal.model.Shipment;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
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
	property = "commerce.data.provider.key=" + CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_SHIPMENTS,
	service = CommerceDataSetDataProvider.class
)
public class CommerceSubscriptionShipmentsDataSetDataProvider
	implements CommerceDataSetDataProvider<Shipment> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commerceSubscriptionEntryId = ParamUtil.getLong(
			httpServletRequest, "commerceSubscriptionEntryId");

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntryId);

		return	_commerceShipmentItemService.getCommerceShipmentItemsCount(
			commerceSubscriptionEntry.getCommerceOrderItemId());
	}

	@Reference
	private CommerceShipmentItemService _commerceShipmentItemService;

	@Override
	public List<Shipment> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Shipment> shipments = new ArrayList<>();

		long commerceSubscriptionEntryId = ParamUtil.getLong(
			httpServletRequest, "commerceSubscriptionEntryId");

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntryId);

		List<CommerceShipmentItem> commerceShipmentItems =
			_commerceShipmentItemService.getCommerceShipmentItems(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		for (CommerceShipmentItem commerceShipmentItem :
				commerceShipmentItems) {

			CommerceShipment commerceShipment =
				_commerceShipmentService.getCommerceShipment(
					commerceShipmentItem.getCommerceShipmentId());

			CommerceOrderItem commerceOrderItem =
				_commerceOrderItemService.getCommerceOrderItem(
					commerceShipmentItem.getCommerceOrderItemId());

			String commerceShipmentIdString = String.valueOf(
				commerceShipment.getCommerceShipmentId());

			String commerceOrderIdString = String.valueOf(
				commerceOrderItem.getCommerceOrderId());

			CommerceAddress commerceAddress =
				_commerceAddressService.getCommerceAddress(
					commerceShipment.getCommerceAddressId());

			StringBundler addressStringBundler = new StringBundler();

			addressStringBundler.append(commerceAddress.getName());
			addressStringBundler.append(CharPool.SPACE);
			addressStringBundler.append(commerceAddress.getStreet1());

			Shipment shipment = new Shipment(
				commerceShipment.getCreateDate(),
				new Link(
					commerceShipmentIdString,
					_getEditShipmentURL(
						commerceShipment.getCommerceShipmentId(),
						httpServletRequest)),
				_getShipmentStatus(commerceShipment),
				new Link(
					commerceOrderIdString,
					_getEditCommerceOrderURL(
						commerceOrderItem.getCommerceOrderId(),
						httpServletRequest)),
				addressStringBundler.toString(),
				new Link(commerceShipment.getTrackingNumber(), ""));

			shipments.add(shipment);
		}

		return shipments;
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

	private String _getEditShipmentURL(long commerceShipmentId, HttpServletRequest httpServletRequest)
		throws PortalException {

		CPRequestHelper cpRequestHelper = new CPRequestHelper(httpServletRequest);

		ThemeDisplay themeDisplay = cpRequestHelper.getThemeDisplay();

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, themeDisplay.getScopeGroup(),
			CommerceShipment.class.getName(), PortletProvider.Action.MANAGE);

		portletURL.setParameter("mvcRenderCommandName", "viewCommerceShipmentDetail");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter("commerceShipmentId", String.valueOf(commerceShipmentId));

		return portletURL.toString();
	}

	private Label _getShipmentStatus(CommerceShipment commerceShipment) {
		if (Objects.equals(
				commerceShipment.getStatus(),
				CommerceShipmentConstants.SHIPMENT_STATUS_SHIPPED)) {

			return new Label(
				CommerceShipmentConstants.getShipmentStatusLabel(
					CommerceShipmentConstants.SHIPMENT_STATUS_SHIPPED),
				Label.SUCCESS);
		}
		else if (Objects.equals(
					commerceShipment.getStatus(),
					CommerceShipmentConstants.SHIPMENT_STATUS_DELIVERED)) {

			return new Label(
				CommerceShipmentConstants.getShipmentStatusLabel(
					CommerceShipmentConstants.SHIPMENT_STATUS_DELIVERED),
				Label.INFO);
		}
		else if (Objects.equals(
					commerceShipment.getStatus(),
					CommerceShipmentConstants.SHIPMENT_STATUS_PROCESSING)) {

			return new Label(
				CommerceShipmentConstants.getShipmentStatusLabel(
					CommerceShipmentConstants.SHIPMENT_STATUS_PROCESSING),
				Label.INFO);
		}
		else if (Objects.equals(
					commerceShipment.getStatus(),
					CommerceShipmentConstants.
						SHIPMENT_STATUS_READY_TO_BE_SHIPPED)) {

			return new Label(
				CommerceShipmentConstants.getShipmentStatusLabel(
					CommerceShipmentConstants.
						SHIPMENT_STATUS_READY_TO_BE_SHIPPED),
				Label.INFO);
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
				"", "commerce-shipment-order-by-col", orderByCol);
			preferences.setValue(
				"", "commerce-shipment-order-by-type", orderByType);
		}
		else {
			orderByCol = preferences.getValue(
				"", "commerce-shipment-order-by-col", "sku");
			orderByType = preferences.getValue(
				"", "commerce-shipment-order-by-type", "asc");
		}

		_setOrderByCol(orderByCol);
		_setOrderByType(orderByType);
	}

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceShipmentService _commerceShipmentService;

	@Reference
	private CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;

	private String _orderByCol;
	private String _orderByType;

	@Reference
	private Portal _portal;

}