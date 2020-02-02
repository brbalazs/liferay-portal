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

package com.liferay.headless.commerce.delivery.cart.internal.resource.v1_0;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderNote;
import com.liferay.commerce.service.CommerceOrderNoteService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DefaultDTOConverterContext;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartNote;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.NoteDTOConverter;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartNoteResource;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldId;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Andrea Sbarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/cart-note.properties",
	scope = ServiceScope.PROTOTYPE, service = CartNoteResource.class
)
public class CartNoteResourceImpl extends BaseCartNoteResourceImpl {

	@Override
	public Response deleteCartNote(@NotNull Long noteId) throws Exception {
		_commerceOrderNoteService.deleteCommerceOrderNote(noteId);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public CartNote getCartNote(@NotNull Long noteId) throws Exception {
		return _noteDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				GetterUtil.getLong(noteId)));
	}

	@NestedField(parentClass = Cart.class, value = "notes")
	@Override
	public Page<CartNote> getCartNotesPage(
			@NestedFieldId("id") @NotNull Long cartId, Pagination pagination)
		throws Exception {

		List<CommerceOrderNote> commerceOrderNotes =
			_commerceOrderNoteService.getCommerceOrderNotes(
				cartId, pagination.getStartPosition(),
				pagination.getEndPosition());

		int totalItems = _commerceOrderNoteService.getCommerceOrderNotesCount(
			cartId);

		return Page.of(
			_toOrderNotes(commerceOrderNotes), pagination, totalItems);
	}

	@Override
	public CartNote patchCartNote(@NotNull Long noteId, CartNote cartNote)
		throws Exception {

		return _updateOrderNote(
			_commerceOrderNoteService.getCommerceOrderNote(noteId), cartNote);
	}

	@Override
	public CartNote postCartNote(@NotNull Long cartId, CartNote cartNote)
		throws Exception {

		return _upsertOrderNote(
			_commerceOrderService.getCommerceOrder(cartId), cartNote);
	}

	private List<CartNote> _toOrderNotes(
			List<CommerceOrderNote> commerceOrderNotes)
		throws Exception {

		List<CartNote> orders = new ArrayList<>();

		for (CommerceOrderNote commerceOrderNote : commerceOrderNotes) {
			orders.add(
				_noteDTOConverter.toDTO(
					new DefaultDTOConverterContext(
						contextAcceptLanguage.getPreferredLocale(),
						commerceOrderNote.getCommerceOrderNoteId())));
		}

		return orders;
	}

	private CartNote _updateOrderNote(
			CommerceOrderNote commerceOrderNote, CartNote note)
		throws Exception {

		commerceOrderNote = _commerceOrderNoteService.updateCommerceOrderNote(
			commerceOrderNote.getCommerceOrderNoteId(),
			GetterUtil.get(note.getContent(), commerceOrderNote.getContent()),
			GetterUtil.get(
				note.getRestricted(), commerceOrderNote.isRestricted()));

		return _noteDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				commerceOrderNote.getCommerceOrderNoteId()));
	}

	private CartNote _upsertOrderNote(
			CommerceOrder commerceOrder, CartNote note)
		throws Exception {

		CommerceOrderNote commerceOrderNote =
			_commerceOrderNoteService.upsertCommerceOrderNote(
				GetterUtil.get(note.getId(), 0L),
				commerceOrder.getCommerceOrderId(), note.getContent(),
				GetterUtil.get(note.getRestricted(), false), null,
				_serviceContextHelper.getServiceContext(
					commerceOrder.getGroupId()));

		return _noteDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				commerceOrderNote.getCommerceOrderNoteId()));
	}

	@Reference
	private CommerceOrderNoteService _commerceOrderNoteService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private NoteDTOConverter _noteDTOConverter;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}