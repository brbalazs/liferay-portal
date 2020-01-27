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

package com.liferay.headless.commerce.delivery.cart.internal.graphql.query.v1_0;

import com.liferay.headless.commerce.delivery.cart.dto.v1_0.BillingAddress;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Note;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.PaymentMethod;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.ShippingMethod;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.BillingAddressResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartItemResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.NoteResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.PaymentMethodResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.ShippingAddressResource;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.ShippingMethodResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Map;
import java.util.function.BiFunction;

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
public class Query {

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

	public static void setPaymentMethodResourceComponentServiceObjects(
		ComponentServiceObjects<PaymentMethodResource>
			paymentMethodResourceComponentServiceObjects) {

		_paymentMethodResourceComponentServiceObjects =
			paymentMethodResourceComponentServiceObjects;
	}

	public static void setShippingAddressResourceComponentServiceObjects(
		ComponentServiceObjects<ShippingAddressResource>
			shippingAddressResourceComponentServiceObjects) {

		_shippingAddressResourceComponentServiceObjects =
			shippingAddressResourceComponentServiceObjects;
	}

	public static void setShippingMethodResourceComponentServiceObjects(
		ComponentServiceObjects<ShippingMethodResource>
			shippingMethodResourceComponentServiceObjects) {

		_shippingMethodResourceComponentServiceObjects =
			shippingMethodResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartBillingAddress(cartId: ___, channelId: ___){id, city, country, countryISOCode, description, latitude, longitude, name, phoneNumber, region, regionISOCode, street1, street2, street3, vatNumber, zip}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrive cart billing address.")
	public BillingAddress channelCartBillingAddress(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_billingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			billingAddressResource ->
				billingAddressResource.getChannelCartBillingAddress(
					channelId, cartId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCarts(channelId: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Retrieves carts for specific account in the given channl."
	)
	public CartPage channelCarts(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> new CartPage(
				cartResource.getChannelCartsPage(
					channelId, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCart(cartId: ___, channelId: ___){id, account, accountId, author, billingAddress, billingId, createDate, cartItems, couponCode, customFields, lastPriceUpdateDate, modifiedDate, notes, paymentMethod, paymentMethodLabel, paymentStatus, paymentStatusLabel, printedNote, purchaseOrderNumber, shippingAddress, shippingAddressId, shippingMethod, shippingOption, status, summary}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrive information of the given Cart.")
	public Cart channelCart(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartResource -> cartResource.getChannelCart(channelId, cartId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartItems(cartId: ___, channelId: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrive cart items of a Cart.")
	public CartItemPage channelCartItems(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> new CartItemPage(
				cartItemResource.getChannelCartItemsPage(
					channelId, cartId, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartItemCartItem(cartId: ___, cartItemId: ___, channelId: ___){name, id, customFields, price, options, productId, quantity, sku, skuId, subscription}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrive information of the given Cart.")
	public CartItem channelCartItemCartItem(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("cartItemId") Long cartItemId)
		throws Exception {

		return _applyComponentServiceObjects(
			_cartItemResourceComponentServiceObjects,
			this::_populateResourceContext,
			cartItemResource -> cartItemResource.getChannelCartItemCartItem(
				channelId, cartId, cartItemId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartNotes(cartId: ___, channelId: ___, page: ___, pageSize: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public NotePage channelCartNotes(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		return _applyComponentServiceObjects(
			_noteResourceComponentServiceObjects,
			this::_populateResourceContext,
			noteResource -> new NotePage(
				noteResource.getChannelCartNotesPage(
					channelId, cartId, Pagination.of(page, pageSize))));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartNote(cartId: ___, channelId: ___, noteId: ___){id, author, content, orderId, restricted}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Note channelCartNote(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId,
			@GraphQLName("noteId") Long noteId)
		throws Exception {

		return _applyComponentServiceObjects(
			_noteResourceComponentServiceObjects,
			this::_populateResourceContext,
			noteResource -> noteResource.getChannelCartNote(
				channelId, cartId, noteId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartPaymentMethods(cartId: ___, channelId: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Retrive payment methods available for the Cart."
	)
	public PaymentMethodPage channelCartPaymentMethods(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_paymentMethodResourceComponentServiceObjects,
			this::_populateResourceContext,
			paymentMethodResource -> new PaymentMethodPage(
				paymentMethodResource.getChannelCartPaymentMethodsPage(
					channelId, cartId)));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartShippingAddress(cartId: ___, channelId: ___){id, city, country, countryISOCode, description, latitude, longitude, name, phoneNumber, region, regionISOCode, street1, street2, street3, zip}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(description = "Retrive cart shipping address.")
	public ShippingAddress channelCartShippingAddress(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_shippingAddressResourceComponentServiceObjects,
			this::_populateResourceContext,
			shippingAddressResource ->
				shippingAddressResource.getChannelCartShippingAddress(
					channelId, cartId));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {channelCartShippingMethods(cartId: ___, channelId: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField(
		description = "Retrive payment methods available for the Cart."
	)
	public ShippingMethodPage channelCartShippingMethods(
			@GraphQLName("channelId") Long channelId,
			@GraphQLName("cartId") Long cartId)
		throws Exception {

		return _applyComponentServiceObjects(
			_shippingMethodResourceComponentServiceObjects,
			this::_populateResourceContext,
			shippingMethodResource -> new ShippingMethodPage(
				shippingMethodResource.getChannelCartShippingMethodsPage(
					channelId, cartId)));
	}

	@GraphQLName("BillingAddressPage")
	public class BillingAddressPage {

		public BillingAddressPage(Page billingAddressPage) {
			actions = billingAddressPage.getActions();
			items = billingAddressPage.getItems();
			lastPage = billingAddressPage.getLastPage();
			page = billingAddressPage.getPage();
			pageSize = billingAddressPage.getPageSize();
			totalCount = billingAddressPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<BillingAddress> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("CartPage")
	public class CartPage {

		public CartPage(Page cartPage) {
			actions = cartPage.getActions();
			items = cartPage.getItems();
			lastPage = cartPage.getLastPage();
			page = cartPage.getPage();
			pageSize = cartPage.getPageSize();
			totalCount = cartPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<Cart> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("CartItemPage")
	public class CartItemPage {

		public CartItemPage(Page cartItemPage) {
			actions = cartItemPage.getActions();
			items = cartItemPage.getItems();
			lastPage = cartItemPage.getLastPage();
			page = cartItemPage.getPage();
			pageSize = cartItemPage.getPageSize();
			totalCount = cartItemPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<CartItem> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("NotePage")
	public class NotePage {

		public NotePage(Page notePage) {
			actions = notePage.getActions();
			items = notePage.getItems();
			lastPage = notePage.getLastPage();
			page = notePage.getPage();
			pageSize = notePage.getPageSize();
			totalCount = notePage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<Note> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("PaymentMethodPage")
	public class PaymentMethodPage {

		public PaymentMethodPage(Page paymentMethodPage) {
			actions = paymentMethodPage.getActions();
			items = paymentMethodPage.getItems();
			lastPage = paymentMethodPage.getLastPage();
			page = paymentMethodPage.getPage();
			pageSize = paymentMethodPage.getPageSize();
			totalCount = paymentMethodPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<PaymentMethod> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("ShippingAddressPage")
	public class ShippingAddressPage {

		public ShippingAddressPage(Page shippingAddressPage) {
			actions = shippingAddressPage.getActions();
			items = shippingAddressPage.getItems();
			lastPage = shippingAddressPage.getLastPage();
			page = shippingAddressPage.getPage();
			pageSize = shippingAddressPage.getPageSize();
			totalCount = shippingAddressPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<ShippingAddress> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	@GraphQLName("ShippingMethodPage")
	public class ShippingMethodPage {

		public ShippingMethodPage(Page shippingMethodPage) {
			actions = shippingMethodPage.getActions();
			items = shippingMethodPage.getItems();
			lastPage = shippingMethodPage.getLastPage();
			page = shippingMethodPage.getPage();
			pageSize = shippingMethodPage.getPageSize();
			totalCount = shippingMethodPage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<ShippingMethod> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

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
			PaymentMethodResource paymentMethodResource)
		throws Exception {

		paymentMethodResource.setContextAcceptLanguage(_acceptLanguage);
		paymentMethodResource.setContextCompany(_company);
		paymentMethodResource.setContextHttpServletRequest(_httpServletRequest);
		paymentMethodResource.setContextHttpServletResponse(
			_httpServletResponse);
		paymentMethodResource.setContextUriInfo(_uriInfo);
		paymentMethodResource.setContextUser(_user);
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

	private void _populateResourceContext(
			ShippingMethodResource shippingMethodResource)
		throws Exception {

		shippingMethodResource.setContextAcceptLanguage(_acceptLanguage);
		shippingMethodResource.setContextCompany(_company);
		shippingMethodResource.setContextHttpServletRequest(
			_httpServletRequest);
		shippingMethodResource.setContextHttpServletResponse(
			_httpServletResponse);
		shippingMethodResource.setContextUriInfo(_uriInfo);
		shippingMethodResource.setContextUser(_user);
	}

	private static ComponentServiceObjects<BillingAddressResource>
		_billingAddressResourceComponentServiceObjects;
	private static ComponentServiceObjects<CartResource>
		_cartResourceComponentServiceObjects;
	private static ComponentServiceObjects<CartItemResource>
		_cartItemResourceComponentServiceObjects;
	private static ComponentServiceObjects<NoteResource>
		_noteResourceComponentServiceObjects;
	private static ComponentServiceObjects<PaymentMethodResource>
		_paymentMethodResourceComponentServiceObjects;
	private static ComponentServiceObjects<ShippingAddressResource>
		_shippingAddressResourceComponentServiceObjects;
	private static ComponentServiceObjects<ShippingMethodResource>
		_shippingMethodResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private com.liferay.portal.kernel.model.Company _company;
	private com.liferay.portal.kernel.model.User _user;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;

}