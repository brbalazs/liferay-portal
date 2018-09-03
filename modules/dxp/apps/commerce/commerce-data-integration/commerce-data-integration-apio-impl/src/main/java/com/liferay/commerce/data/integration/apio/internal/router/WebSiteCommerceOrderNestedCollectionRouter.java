/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.apio.internal.router;

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.router.NestedCollectionRouter;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.data.integration.apio.identifiers.ClassPKExternalReferenceCode;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderIdentifier;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.site.apio.architect.identifier.WebSiteIdentifier;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class WebSiteCommerceOrderNestedCollectionRouter
	implements NestedCollectionRouter
		<CommerceOrder, ClassPKExternalReferenceCode,
			CommerceOrderIdentifier, Long, WebSiteIdentifier> {

	@Override
	public NestedCollectionRoutes
		<CommerceOrder, ClassPKExternalReferenceCode, Long>
			collectionRoutes(
				NestedCollectionRoutes.Builder
					<CommerceOrder, ClassPKExternalReferenceCode, Long>
						builder) {

		return builder.addGetter(
			this::_getPageItems
		).build();
	}

	private PageItems<CommerceOrder> _getPageItems(
			Pagination pagination, long groupId)
		throws PortalException {

		List<CommerceOrder> orders = _commerceOrderService.getCommerceOrders(
			groupId,
			new int[] {CommerceOrderConstants.ORDER_STATUS_TO_TRANSMIT});

		return new PageItems<>(orders, orders.size());
	}

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.model.CommerceOrder)"
	)
	private HasPermission<Long> _hasPermission;

}