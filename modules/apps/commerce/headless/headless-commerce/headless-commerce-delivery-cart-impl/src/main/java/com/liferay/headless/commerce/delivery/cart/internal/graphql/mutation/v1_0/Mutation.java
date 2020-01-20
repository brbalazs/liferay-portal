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

import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Order;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartItemResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class Mutation {

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

	@GraphQLField(
		description = "Add new Items to a Cart, return the whole Cart updated."
	)
	public Cart createChannelCartCartItem(
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("orderItem") OrderItem orderItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.postChannelCartCartItem(
				cartId, channelId, orderItem));
	}

	@GraphQLField
	public Cart updateChannelCart(
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("order") Order order)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.putChannelCart(
				cartId, channelId, order));
	}

	@GraphQLField
	public Cart createChannelCart(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("order") Order order)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.postChannelCart(channelId, order));
	}

	@GraphQLField(description = "Retrive information of the given Cart.")
	public CartItem patchChannelCartCartItem(
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId,
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartItem") CartItem cartItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.patchChannelCartCartItem(
				cartId, cartItemId, channelId, cartItem));
	}

	@GraphQLField(description = "Retrive information of the given Cart.")
	public CartItem updateChannelCartCartItem(
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId,
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartItem") CartItem cartItem)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.putChannelCartCartItem(
				cartId, cartItemId, channelId, cartItem));
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

	private static ComponentServiceObjects<CartResource>
		_cartResourceComponentServiceObjects;
	private static ComponentServiceObjects<CartItemResource>
		_cartItemResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private com.liferay.portal.kernel.model.User _user;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;

}