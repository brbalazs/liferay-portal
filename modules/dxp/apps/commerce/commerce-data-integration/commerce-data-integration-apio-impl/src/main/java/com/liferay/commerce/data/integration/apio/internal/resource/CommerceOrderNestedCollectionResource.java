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

package com.liferay.commerce.data.integration.apio.internal.resource;

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.NestedCollectionResource;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceAccountIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceAddressIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommercePaymentMethodIdentifier;
import com.liferay.commerce.data.integration.apio.internal.form.CommerceOrderUpdaterForm;
import com.liferay.commerce.data.integration.apio.internal.util.CommerceOrderHelper;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.organization.service.CommerceOrganizationService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class CommerceOrderNestedCollectionResource
	implements
		NestedCollectionResource<CommerceOrder, Long,
			CommerceOrderIdentifier, Long, CommerceAccountIdentifier> {

	@Override
	public NestedCollectionRoutes<CommerceOrder, Long, Long> collectionRoutes(
		NestedCollectionRoutes.Builder<CommerceOrder, Long, Long> builder) {

		return builder.addGetter(
			this::_getPageItems
		).build();
	}

	@Override
	public String getName() {
		return "commerce-order";
	}

	@Override
	public ItemRoutes<CommerceOrder, Long> itemRoutes(
		ItemRoutes.Builder<CommerceOrder, Long> builder) {

		return builder.addGetter(
			this::_getCommerceOrder
		).addUpdater(
			this::_updateCommerceOrder, _hasPermission::forUpdating,
			CommerceOrderUpdaterForm::buildForm
		).build();
	}

	@Override
	public Representor<CommerceOrder> representor(
		Representor.Builder<CommerceOrder, Long> builder) {

		return builder.types(
			"CommerceOrder"
		).identifier(
			CommerceOrder::getCommerceOrderId
		).addBidirectionalModel(
			"commerceAccount", "commerceOrders",
			CommerceAccountIdentifier.class,
			CommerceOrder::getOrderOrganizationId
		).addLinkedModel(
			"commerceAccount", CommerceAccountIdentifier.class,
			CommerceOrder::getOrderOrganizationId
		).addLinkedModel(
			"commercePaymentMethod", CommercePaymentMethodIdentifier.class,
			CommerceOrder::getCommercePaymentMethodId
		).addString(
			"purchaseOrderNumber", CommerceOrder::getPurchaseOrderNumber
		).addNumber(
			"shippingPrice", CommerceOrder::getShippingAmount
		).addNumber(
			"total", CommerceOrder::getTotal
		).addString(
			"orderStatus",
			order -> CommerceOrderConstants.getOrderStatusLabel(
				order.getOrderStatus())
		).addString(
			"paymentStatus",
			order -> CommerceOrderConstants.getPaymentStatusLabel(
				order.getPaymentStatus())
		).addDate(
			"dateCreated", CommerceOrder::getCreateDate
		).addDate(
			"dateModified", CommerceOrder::getModifiedDate
		).addLinkedModel(
			"author", PersonIdentifier.class, CommerceOrder::getUserId
		).addLinkedModel(
			"shippingAddress", CommerceAddressIdentifier.class,
			CommerceOrder::getShippingAddressId
		).addLinkedModel(
			"billingAddress", CommerceAddressIdentifier.class,
			CommerceOrder::getBillingAddressId
		).build();
	}

	private CommerceOrder _getCommerceOrder(Long commerceOrderId)
		throws PortalException {

		return _commerceOrderService.getCommerceOrder(commerceOrderId);
	}

	private PageItems<CommerceOrder> _getPageItems(
			Pagination pagination, Long organizationId)
		throws PortalException {

		Organization organization =
			_commerceOrganizationService.getOrganization(organizationId);

		Group group = organization.getGroup();

		List<CommerceOrder> commerceOrders =
			_commerceOrderService.getCommerceOrders(
				group.getGroupId(), pagination.getStartPosition(),
				pagination.getEndPosition(), null);

		int total = _commerceOrderService.getCommerceOrdersCount(
			group.getGroupId());

		return new PageItems<>(commerceOrders, total);
	}

	private CommerceOrder _updateCommerceOrder(
			Long commerceOrderId,
			CommerceOrderUpdaterForm commerceOrderUpdaterForm)
		throws PortalException {

		return _commerceOrderHelper.updateCommerceOrder(
			commerceOrderId, commerceOrderUpdaterForm.getOrderStatus(),
			commerceOrderUpdaterForm.getPaymentStatus());
	}

	@Reference
	private CommerceOrderHelper _commerceOrderHelper;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceOrganizationService _commerceOrganizationService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.model.CommerceOrder)"
	)
	private HasPermission<Long> _hasPermission;

}