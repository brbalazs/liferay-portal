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

package com.liferay.headless.commerce.delivery.cart.internal.graphql.mutation.v1_0;

import com.liferay.headless.commerce.delivery.cart.dto.v1_0.BillingAddress;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItemPost;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartPost;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CouponCode;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Note;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.BillingAddressResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartItemResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.NoteResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.ShippingAddressResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setBillingAddressResourceComponentServiceObjects(
		ComponentServiceObjects<BillingAddressResource>
			billingAddressResourceComponentServiceObjects) {

		_billingAddressResourceComponentServiceObjects =
			billingAddressResourceComponentServiceObjects;
	}

	public static void setCartResourceComponentServiceObjects(
		ComponentServiceObjects<CartResource>
			cartResourceComponentServiceObjects) {

		_cartResourceComponentServiceObjects =
			cartResourceComponentServiceObjects;
	}

	public static void setCartItemResourceComponentServiceObjects(
		ComponentServiceObjects<CartItemResource>
			cartItemResourceComponentServiceObjects) {

		_cartItemResourceComponentServiceObjects =
			cartItemResourceComponentServiceObjects;
	}

	public static void setNoteResourceComponentServiceObjects(
		ComponentServiceObjects<NoteResource>
			noteResourceComponentServiceObjects) {

		_noteResourceComponentServiceObjects =
			noteResourceComponentServiceObjects;
	}

	public static void setShippingAddressResourceComponentServiceObjects(
		ComponentServiceObjects<ShippingAddressResource>
			shippingAddressResourceComponentServiceObjects) {

		_shippingAddressResourceComponentServiceObjects =
			shippingAddressResourceComponentServiceObjects;
	}

	@GraphQLField
	public Response patchChannelCartBillingAddress(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("billingAddress") BillingAddress billingAddress)
		throws Exception {

		return _applyComponentServiceObjects(
			_billingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			billingAddressResource ->
				billingAddressResource.patchChannelCartBillingAddress(
					channelId, cartId, billingAddress));
	}

	@GraphQLField
	public Cart createChannelCart(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartPost") CartPost cartPost)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.postChannelCart(channelId, cartPost));
	}

	@GraphQLField
	public Response deleteChannelCart(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.deleteChannelCart(channelId, cartId));
	}

	@GraphQLField
	public Cart patchChannelCart(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartPost") CartPost cartPost)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.patchChannelCart(
				channelId, cartId, cartPost));
	}

	@GraphQLField(
		description = "Add new Items to a Cart, return the whole Cart updated."
	)
	public Cart createChannelCartCouponCode(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("couponCode") CouponCode couponCode)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.postChannelCartCouponCode(
				channelId, cartId, couponCode));
	}

	@GraphQLField(
		description = "Add new Items to a Cart, return the whole Cart updated."
	)
	public CartItem createChannelCartItem(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemPost") CartItemPost cartItemPost)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.postChannelCartItem(
				channelId, cartId, cartItemPost));
	}

	@GraphQLField
	public Response deleteChannelCartItemCartItem(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.deleteChannelCartItemCartItem(
				channelId, cartId, cartItemId));
	}

	@GraphQLField(description = "Retrive information of the given Cart.")
	public CartItem patchChannelCartItemCartItem(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId,
			@GraphQLName("cartItem") CartItem cartItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.patchChannelCartItemCartItem(
				channelId, cartId, cartItemId, cartItem));
	}

	@GraphQLField(description = "update the given Cart.")
	public CartItem updateChannelCartItemCartItem(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId,
			@GraphQLName("cartItem") CartItem cartItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.putChannelCartItemCartItem(
				channelId, cartId, cartItemId, cartItem));
	}

	@GraphQLField
	public Note createChannelCartNote(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId, @GraphQLName("note") Note note)
		throws Exception {

		return _applyComponentServiceObjects(
			_noteResourceComponentServiceObjects,
			this::_populateResourceContext,
			noteResource -> noteResource.postChannelCartNote(
				channelId, cartId, note));
	}

	@GraphQLField
	public Response deleteChannelCartNote(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("noteId") Long noteId)
		throws Exception {

		return _applyComponentServiceObjects(
			_noteResourceComponentServiceObjects,
			this::_populateResourceContext,
			noteResource -> noteResource.deleteChannelCartNote(
				channelId, cartId, noteId));
	}

	@GraphQLField
	public Response patchChannelCartNote(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("noteId") Long noteId, @GraphQLName("note") Note note)
		throws Exception {

		return _applyComponentServiceObjects(
			_noteResourceComponentServiceObjects,
			this::_populateResourceContext,
			noteResource -> noteResource.patchChannelCartNote(
				channelId, cartId, noteId, note));
	}

	@GraphQLField
	public Response patchChannelCartShippingAddress(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("shippingAddress") ShippingAddress shippingAddress)
		throws Exception {

		return _applyComponentServiceObjects(
			_shippingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			shippingAddressResource ->
				shippingAddressResource.patchChannelCartShippingAddress(
					channelId, cartId, shippingAddress));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			BillingAddressResource billingAddressResource)
		throws Exception {

		billingAddressResource.setContextAcceptLanguage(_acceptLanguage);
		billingAddressResource.setContextCompany(_company);
		billingAddressResource.setContextHttpServletRequest(
			_httpServletRequest);
		billingAddressResource.setContextHttpServletResponse(
			_httpServletResponse);
		billingAddressResource.setContextUriInfo(_uriInfo);
		billingAddressResource.setContextUser(_user);
	}

	private void _populateResourceContext(CartResource cartResource)
		throws Exception {

		cartResource.setContextAcceptLanguage(_acceptLanguage);
		cartResource.setContextCompany(_company);
		cartResource.setContextHttpServletRequest(_httpServletRequest);
		cartResource.setContextHttpServletResponse(_httpServletResponse);
		cartResource.setContextUriInfo(_uriInfo);
		cartResource.setContextUser(_user);
	}

	private void _populateResourceContext(CartItemResource cartItemResource)
		throws Exception {

		cartItemResource.setContextAcceptLanguage(_acceptLanguage);
		cartItemResource.setContextCompany(_company);
		cartItemResource.setContextHttpServletRequest(_httpServletRequest);
		cartItemResource.setContextHttpServletResponse(_httpServletResponse);
		cartItemResource.setContextUriInfo(_uriInfo);
		cartItemResource.setContextUser(_user);
	}

	private void _populateResourceContext(NoteResource noteResource)
		throws Exception {

		noteResource.setContextAcceptLanguage(_acceptLanguage);
		noteResource.setContextCompany(_company);
		noteResource.setContextHttpServletRequest(_httpServletRequest);
		noteResource.setContextHttpServletResponse(_httpServletResponse);
		noteResource.setContextUriInfo(_uriInfo);
		noteResource.setContextUser(_user);
	}

	private void _populateResourceContext(
			ShippingAddressResource shippingAddressResource)
		throws Exception {

		shippingAddressResource.setContextAcceptLanguage(_acceptLanguage);
		shippingAddressResource.setContextCompany(_company);
		shippingAddressResource.setContextHttpServletRequest(
			_httpServletRequest);
		shippingAddressResource.setContextHttpServletResponse(
			_httpServletResponse);
		shippingAddressResource.setContextUriInfo(_uriInfo);
		shippingAddressResource.setContextUser(_user);
	}

	private static ComponentServiceObjects<BillingAddressResource>
		_billingAddressResourceComponentServiceObjects;
	private static ComponentServiceObjects<CartResource>
		_cartResourceComponentServiceObjects;
	private static ComponentServiceObjects<CartItemResource>
		_cartItemResourceComponentServiceObjects;
	private static ComponentServiceObjects<NoteResource>
		_noteResourceComponentServiceObjects;
	private static ComponentServiceObjects<ShippingAddressResource>
		_shippingAddressResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private com.liferay.portal.kernel.model.User _user;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;

}