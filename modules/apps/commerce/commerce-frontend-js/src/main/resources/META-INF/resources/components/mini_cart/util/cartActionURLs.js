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

const CHECKOUT_ENDPOINT = '/checkout',
	ORDER_DETAILS_ENDPOINT = '/pending-orders';

const DEFAULT_CHECKOUT_PORTLET_ID =
		'com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet',
	DEFAULT_ORDER_DETAILS_PORTLET_ID =
		'com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet';

function generateCheckoutURL(portletId = DEFAULT_CHECKOUT_PORTLET_ID) {
	const baseUrl = new URL(
		`${Liferay.ThemeDisplay.getCanonicalURL()}${CHECKOUT_ENDPOINT}`
	);

	baseUrl.searchParams.append('p_p_id', portletId);
	baseUrl.searchParams.append('p_p_lifecycle', '0');

	return baseUrl.toString();
}

function generateOrderDetailURL(
	orderUuid = null,
	portletId = DEFAULT_ORDER_DETAILS_PORTLET_ID
) {
	const baseURL = new URL(
		`${Liferay.ThemeDisplay.getCanonicalURL()}${ORDER_DETAILS_ENDPOINT}`
	);

	baseURL.searchParams.append('p_p_id', portletId);
	baseURL.searchParams.append('p_p_lifecycle', '0');
	baseURL.searchParams.append(
		`_${portletId}_mvcRenderCommandName`,
		'editCommerceOrder'
	);
	baseURL.searchParams.append(`_${portletId}_commerceOrderUuid`, orderUuid);

	return baseURL.toString();
}

export const generateActionURLs = ({
	checkoutPortletId,
	orderDetailsPortletId,
	orderUUID
}) => ({
	checkoutURL: generateCheckoutURL(checkoutPortletId),
	detailsURL: generateOrderDetailURL(orderUUID, orderDetailsPortletId)
});
