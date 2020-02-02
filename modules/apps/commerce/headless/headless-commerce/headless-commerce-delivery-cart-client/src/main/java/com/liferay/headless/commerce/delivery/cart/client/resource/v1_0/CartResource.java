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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.CartSerDes;

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
public interface CartResource {

	public static Builder builder() {
		return new Builder();
	}

	public Page<Cart> getChannelCartsPage(Long channelId, Pagination pagination)
		throws Exception;

	public HttpInvoker.HttpResponse getChannelCartsPageHttpResponse(
			Long channelId, Pagination pagination)
		throws Exception;

	public Cart postChannelCart(
			Long channelId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartPost
				cartPost)
		throws Exception;

	public HttpInvoker.HttpResponse postChannelCartHttpResponse(
			Long channelId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartPost
				cartPost)
		throws Exception;

	public void deleteChannelCart(Long channelId, Long cartId) throws Exception;

	public HttpInvoker.HttpResponse deleteChannelCartHttpResponse(
			Long channelId, Long cartId)
		throws Exception;

	public Cart getChannelCart(Long channelId, Long cartId) throws Exception;

	public HttpInvoker.HttpResponse getChannelCartHttpResponse(
			Long channelId, Long cartId)
		throws Exception;

	public Cart patchChannelCart(
			Long channelId, Long cartId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartPost
				cartPost)
		throws Exception;

	public HttpInvoker.HttpResponse patchChannelCartHttpResponse(
			Long channelId, Long cartId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartPost
				cartPost)
		throws Exception;

	public Cart postChannelCartCouponCode(
			Long channelId, Long cartId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
				CouponCode couponCode)
		throws Exception;

	public HttpInvoker.HttpResponse postChannelCartCouponCodeHttpResponse(
			Long channelId, Long cartId,
			com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
				CouponCode couponCode)
		throws Exception;

	public static class Builder {

		public Builder authentication(String login, String password) {
			_login = login;
			_password = password;

			return this;
		}

		public CartResource build() {
			return new CartResourceImpl(this);
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

	public static class CartResourceImpl implements CartResource {

		public Page<Cart> getChannelCartsPage(
				Long channelId, Pagination pagination)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getChannelCartsPageHttpResponse(channelId, pagination);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			return Page.of(content, CartSerDes::toDTO);
		}

		public HttpInvoker.HttpResponse getChannelCartsPageHttpResponse(
				Long channelId, Pagination pagination)
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

			if (pagination != null) {
				httpInvoker.parameter(
					"page", String.valueOf(pagination.getPage()));
				httpInvoker.parameter(
					"pageSize", String.valueOf(pagination.getPageSize()));
			}

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts",
				channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Cart postChannelCart(
				Long channelId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CartPost cartPost)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse = postChannelCartHttpResponse(
				channelId, cartPost);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse postChannelCartHttpResponse(
				Long channelId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CartPost cartPost)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(cartPost.toString(), "application/json");

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

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts",
				channelId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void deleteChannelCart(Long channelId, Long cartId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				deleteChannelCartHttpResponse(channelId, cartId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());
		}

		public HttpInvoker.HttpResponse deleteChannelCartHttpResponse(
				Long channelId, Long cartId)
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

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.DELETE);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Cart getChannelCart(Long channelId, Long cartId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse = getChannelCartHttpResponse(
				channelId, cartId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse getChannelCartHttpResponse(
				Long channelId, Long cartId)
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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Cart patchChannelCart(
				Long channelId, Long cartId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CartPost cartPost)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				patchChannelCartHttpResponse(channelId, cartId, cartPost);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse patchChannelCartHttpResponse(
				Long channelId, Long cartId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CartPost cartPost)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(cartPost.toString(), "application/json");

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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Cart postChannelCartCouponCode(
				Long channelId, Long cartId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CouponCode couponCode)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postChannelCartCouponCodeHttpResponse(
					channelId, cartId, couponCode);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return CartSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse postChannelCartCouponCodeHttpResponse(
				Long channelId, Long cartId,
				com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.
					CouponCode couponCode)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(couponCode.toString(), "application/json");

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

			httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

			httpInvoker.path(
				_builder._scheme + "://" + _builder._host + ":" +
					_builder._port +
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/coupon-code",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		private CartResourceImpl(Builder builder) {
			_builder = builder;
		}

		private static final Logger _logger = Logger.getLogger(
			CartResource.class.getName());

		private Builder _builder;

	}

}