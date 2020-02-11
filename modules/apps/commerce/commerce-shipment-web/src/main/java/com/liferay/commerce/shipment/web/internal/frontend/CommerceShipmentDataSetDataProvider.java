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

import com.liferay.commerce.constants.CommerceShipmentConstants;
import com.liferay.commerce.constants.CommerceShipmentDataSetConstants;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.LabelField;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceRegion;
import com.liferay.commerce.model.CommerceShipment;
import com.liferay.commerce.model.Shipment;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.service.CommerceShipmentService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.text.DateFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceShipmentDataSetConstants.COMMERCE_DATA_SET_KEY_SHIPMENTS,
	service = CommerceDataSetDataProvider.class
)
public class CommerceShipmentDataSetDataProvider
	implements CommerceDataSetDataProvider<Shipment> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.fetchCommerceOrder(
			commerceOrderId);

		if (commerceOrder != null) {
			return _commerceShipmentService.getCommerceShipmentsCount(
				commerceOrder.getCompanyId(),
				commerceOrder.getShippingAddressId());
		}

		return _commerceShipmentService.getCommerceShipmentsCount(
			_portal.getCompanyId(httpServletRequest));
	}

	@Override
	public List<Shipment> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Shipment> shipments = new ArrayList<>();

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.fetchCommerceOrder(
			commerceOrderId);

		List<CommerceShipment> commerceShipments;

		if (commerceOrder != null) {
			commerceShipments = _commerceShipmentService.getCommerceShipments(
				commerceOrder.getCompanyId(),
				commerceOrder.getShippingAddressId(),
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);
		}
		else {
			commerceShipments = _commerceShipmentService.getCommerceShipments(
				_portal.getCompanyId(httpServletRequest),
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);
		}

		User user = _portal.getUser(httpServletRequest);

		Format dateTimeFormat = FastDateFormatFactoryUtil.getDateTime(
			DateFormat.SHORT, DateFormat.SHORT,
			_portal.getLocale(httpServletRequest), user.getTimeZone());

		for (CommerceShipment commerceShipment : commerceShipments) {
			shipments.add(
				new Shipment(
					commerceShipment.getCommerceShipmentId(),
					_getShipmentPanelURL(
						commerceShipment.getCommerceShipmentId(),
						httpServletRequest),
					commerceShipment.getCommerceAccountName(),
					_getDescriptiveAddress(commerceShipment),
					dateTimeFormat.format(commerceShipment.getCreateDate()),
					new LabelField(
						CommerceShipmentConstants.getShipmentLabelStyle(
							commerceShipment.getStatus()),
						LanguageUtil.get(
							httpServletRequest,
							CommerceShipmentConstants.getShipmentStatusLabel(
								commerceShipment.getStatus()))),
					commerceShipment.getTrackingNumber()));
		}

		return shipments;
	}

	private String _getDescriptiveAddress(CommerceShipment commerceShipment)
		throws PortalException {

		CommerceAddress commerceAddress =
			commerceShipment.fetchCommerceAddress();

		if (commerceAddress == null) {
			return StringPool.BLANK;
		}

		CommerceRegion commerceRegion = commerceAddress.getCommerceRegion();

		StringBundler sb = new StringBundler((commerceRegion == null) ? 5 : 7);

		sb.append(commerceAddress.getStreet1());
		sb.append(StringPool.SPACE);
		sb.append(commerceAddress.getCity());
		sb.append(StringPool.NEW_LINE);

		if (commerceRegion != null) {
			sb.append(commerceRegion.getCode());
			sb.append(StringPool.SPACE);
		}

		sb.append(commerceAddress.getZip());

		return sb.toString();
	}

	private String _getShipmentPanelURL(
			long commerceShipmentId, HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CommerceOrder.class.getName(),
			PortletProvider.Action.MANAGE);

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommerceOrderShipment");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(httpServletRequest));

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		portletURL.setParameter(
			"commerceOrderId", String.valueOf(commerceOrderId));

		portletURL.setParameter(
			"commerceShipmentId", String.valueOf(commerceShipmentId));

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException wse) {
			_log.error(wse, wse);
		}

		return portletURL.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceShipmentDataSetDataProvider.class);

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceShipmentService _commerceShipmentService;

	@Reference
	private Portal _portal;

}