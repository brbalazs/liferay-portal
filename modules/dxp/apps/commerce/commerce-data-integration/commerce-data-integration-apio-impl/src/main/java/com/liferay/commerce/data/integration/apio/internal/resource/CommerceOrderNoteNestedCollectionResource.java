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
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderNoteIdentifier;
import com.liferay.commerce.model.CommerceOrderNote;
import com.liferay.commerce.service.CommerceOrderNoteService;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class CommerceOrderNoteNestedCollectionResource
	implements
		NestedCollectionResource<CommerceOrderNote, Long,
			CommerceOrderNoteIdentifier, Long, CommerceOrderIdentifier> {

	@Override
	public NestedCollectionRoutes<CommerceOrderNote, Long, Long>
		collectionRoutes(
			NestedCollectionRoutes.Builder<CommerceOrderNote, Long, Long>
				builder) {

		return builder.addGetter(
			this::_getPageItems
		).build();
	}

	@Override
	public String getName() {
		return "commerce-order-note";
	}

	@Override
	public ItemRoutes<CommerceOrderNote, Long> itemRoutes(
		ItemRoutes.Builder<CommerceOrderNote, Long> builder) {

		return builder.addGetter(
			this::_getCommerceOrderNote
		).build();
	}

	@Override
	public Representor<CommerceOrderNote> representor(
		Representor.Builder<CommerceOrderNote, Long> builder) {

		return builder.types(
			"CommerceOrderNote"
		).identifier(
			CommerceOrderNote::getCommerceOrderNoteId
		).addBidirectionalModel(
			"commerceOrder", "commerceOrderNotes",
			CommerceOrderIdentifier.class, CommerceOrderNote::getCommerceOrderId
		).addString(
			"content", CommerceOrderNote::getContent
		).addBoolean(
			"restricted", CommerceOrderNote::getRestricted
		).addDate(
			"dateCreated", CommerceOrderNote::getCreateDate
		).addDate(
			"dateModified", CommerceOrderNote::getModifiedDate
		).addLinkedModel(
			"author", PersonIdentifier.class, CommerceOrderNote::getUserId
		).build();
	}

	private CommerceOrderNote _getCommerceOrderNote(Long commerceOrderNoteId)
		throws PortalException {

		return _commerceOrderNoteService.getCommerceOrderNote(
			commerceOrderNoteId);
	}

	private PageItems<CommerceOrderNote> _getPageItems(
			Pagination pagination, Long commerceOrderId)
		throws PortalException {

		List<CommerceOrderNote> commerceOrderNotes =
			_commerceOrderNoteService.getCommerceOrderNotes(
				commerceOrderId, pagination.getStartPosition(),
				pagination.getEndPosition());

		int total = _commerceOrderNoteService.getCommerceOrderNotesCount(
			commerceOrderId);

		return new PageItems<>(commerceOrderNotes, total);
	}

	@Reference
	private CommerceOrderNoteService _commerceOrderNoteService;

}