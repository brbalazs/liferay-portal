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
import com.liferay.commerce.data.integration.apio.identifiers.CPInstanceIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderItemIdentifier;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class CommerceOrderItemNestedCollectionResource
	implements
		NestedCollectionResource<CommerceOrderItem, Long,
			CommerceOrderItemIdentifier, Long, CommerceOrderIdentifier> {

	@Override
	public NestedCollectionRoutes<CommerceOrderItem, Long, Long>
		collectionRoutes(
			NestedCollectionRoutes.Builder<CommerceOrderItem, Long, Long>
				builder) {

		return builder.addGetter(
			this::_getPageItems
		).build();
	}

	@Override
	public String getName() {
		return "commerce-order-item";
	}

	@Override
	public ItemRoutes<CommerceOrderItem, Long> itemRoutes(
		ItemRoutes.Builder<CommerceOrderItem, Long> builder) {

		return builder.addGetter(
			this::_getCommerceOrderItem
		).build();
	}

	@Override
	public Representor<CommerceOrderItem> representor(
		Representor.Builder<CommerceOrderItem, Long> builder) {

		return builder.types(
			"CommerceOrderItem"
		).identifier(
			CommerceOrderItem::getCommerceOrderItemId
		).addBidirectionalModel(
			"commerceOrder", "commerceOrderItems",
			CommerceOrderIdentifier.class, CommerceOrderItem::getCommerceOrderId
		).addLinkedModel(
			"cpInstance", CPInstanceIdentifier.class,
			CommerceOrderItem::getCPInstanceId
		).addString(
			"sku", CommerceOrderItem::getSku
		).addLocalizedStringByLocale(
			"name", CommerceOrderItem::getName
		).addNumber(
			"quantity", CommerceOrderItem::getQuantity
		).addNumber(
			"shippedQuantity", CommerceOrderItem::getShippedQuantity
		).addNumber(
			"unitPrice", CommerceOrderItem::getUnitPrice
		).addNumber(
			"finalPrice", CommerceOrderItem::getFinalPrice
		).addNumber(
			"discountAmount", CommerceOrderItem::getDiscountAmount
		).addNumber(
			"discountPercentageLevel1",
			CommerceOrderItem::getDiscountPercentageLevel1
		).addNumber(
			"discountPercentageLevel2",
			CommerceOrderItem::getDiscountPercentageLevel2
		).addNumber(
			"discountPercentageLevel3",
			CommerceOrderItem::getDiscountPercentageLevel3
		).addNumber(
			"discountPercentageLevel4",
			CommerceOrderItem::getDiscountPercentageLevel4
		).addDate(
			"dateCreated", CommerceOrderItem::getCreateDate
		).addDate(
			"dateModified", CommerceOrderItem::getModifiedDate
		).addLinkedModel(
			"author", PersonIdentifier.class, CommerceOrderItem::getUserId
		).build();
	}

	private CommerceOrderItem _getCommerceOrderItem(Long commerceOrderItemId)
		throws PortalException {

		return _commerceOrderItemService.getCommerceOrderItem(
			commerceOrderItemId);
	}

	private PageItems<CommerceOrderItem> _getPageItems(
			Pagination pagination, Long commerceOrderId)
		throws PortalException {

		List<CommerceOrderItem> commerceOrderItems =
			_commerceOrderItemService.getCommerceOrderItems(
				commerceOrderId, pagination.getStartPosition(),
				pagination.getEndPosition());

		int total = _commerceOrderItemService.getCommerceOrderItemsCount(
			commerceOrderId);

		return new PageItems<>(commerceOrderItems, total);
	}

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

}