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

package com.liferay.headless.commerce.delivery.cart.client.resource.v1_0;

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartItem;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.CartItemSerDes;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public interface CartItemResource {

	public static Builder builder() {
		return new Builder();
	}

	public CartItem getChannelCartCartItem(
			Long cartId, Long cartItemId, Long channelId)
		throws Exception;

	public HttpInvoker.HttpResponse getChannelCartCartItemHttpResponse(
			Long cartId, Long cartItemId, Long channelId)
		throws Exception;

	public CartItem patchChannelCartCartItem(
			Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
		throws Exception;

	public HttpInvoker.HttpResponse patchChannelCartCartItemHttpResponse(
			Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
		throws Exception;

	public CartItem putChannelCartCartItem(
			Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
		throws Exception;

	public HttpInvoker.HttpResponse putChannelCartCartItemHttpResponse(
			Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
		throws Exception;

	public Page<CartItem> getChannelCartCartItemsPage(
			Long cartId, Long channelId)
		throws Exception;

	public HttpInvoker.HttpResponse getChannelCartCartItemsPageHttpResponse(
			Long cartId, Long channelId)
		throws Exception;

	public static class Builder {

		public Builder authentication(String login, String password) {
			_login = login;
			_password = password;

			return this;
		}

		public CartItemResource build() {
			return new CartItemResourceImpl(this);
		}

		public Builder endpoint(String host, int port, String scheme) {
			_host = host;
			_port = port;
			_scheme = scheme;

			return this;
		}

		public Builder header(String key, String value) {
			_headers.put(key, value);

			return this;
		}

		public Builder locale(Locale locale) {
			_locale = locale;

			return this;
		}

		public Builder parameter(String key, String value) {
			_parameters.put(key, value);

			return this;
		}

		private Builder() {
		}

		private Map<String, String> _headers = new LinkedHashMap<>();
		private String _host = "localhost";
		private Locale _locale;
		private String _login = "test@liferay.com";
		private String _password = "test";
		private Map<String, String> _parameters = new LinkedHashMap<>();
		private int _port = 8080;
		private String _scheme = "http";

	}

	public static class CartItemResourceImpl implements CartItemResource {

		public CartItem getChannelCartCartItem(
				Long cartId, Long cartItemId, Long channelId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getChannelCartCartItemHttpResponse(
					cartId, cartItemId, channelId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartItemSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse getChannelCartCartItemHttpResponse(
				Long cartId, Long cartItemId, Long channelId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/cartItems/{cartItemId}",
				cartId, cartItemId, channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public CartItem patchChannelCartCartItem(
				Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				patchChannelCartCartItemHttpResponse(
					cartId, cartItemId, channelId, cartItem);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartItemSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse patchChannelCartCartItemHttpResponse(
				Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(cartItem.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.PATCH);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/cartItems/{cartItemId}",
				cartId, cartItemId, channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public CartItem putChannelCartCartItem(
				Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				putChannelCartCartItemHttpResponse(
					cartId, cartItemId, channelId, cartItem);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartItemSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse putChannelCartCartItemHttpResponse(
				Long cartId, Long cartItemId, Long channelId, CartItem cartItem)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(cartItem.toString(), "application/json");

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.PUT);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/cartItems/{cartItemId}",
				cartId, cartItemId, channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Page<CartItem> getChannelCartCartItemsPage(
				Long cartId, Long channelId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getChannelCartCartItemsPageHttpResponse(cartId, channelId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			return Page.of(content, CartItemSerDes::toDTO);
		}

		public HttpInvoker.HttpResponse getChannelCartCartItemsPageHttpResponse(
				Long cartId, Long channelId)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			if (_builder._locale != null) {
				httpInvoker.header(
					"Accept-Language", _builder._locale.toLanguageTag());
			}

			for (Map.Entry<String, String> entry :
					_builder._headers.entrySet()) {

				httpInvoker.header(entry.getKey(), entry.getValue());
			}

			for (Map.Entry<String, String> entry :
					_builder._parameters.entrySet()) {

				httpInvoker.parameter(entry.getKey(), entry.getValue());
			}

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/cartItems",
				cartId, channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		private CartItemResourceImpl(Builder builder) {
			_builder = builder;
		}

		private static final Logger _logger = Logger.getLogger(
			CartItemResource.class.getName());

		private Builder _builder;

	}

}