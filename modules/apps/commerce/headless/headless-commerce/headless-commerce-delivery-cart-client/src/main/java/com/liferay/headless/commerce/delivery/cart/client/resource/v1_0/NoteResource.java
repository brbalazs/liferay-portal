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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Note;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.NoteSerDes;

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
public interface NoteResource {

	public static Builder builder() {
		return new Builder();
	}

	public Page<Note> getChannelCartNotesPage(
			Long channelId, Long cartId, Pagination pagination)
		throws Exception;

	public HttpInvoker.HttpResponse getChannelCartNotesPageHttpResponse(
			Long channelId, Long cartId, Pagination pagination)
		throws Exception;

	public Note postChannelCartNote(Long channelId, Long cartId, Note note)
		throws Exception;

	public HttpInvoker.HttpResponse postChannelCartNoteHttpResponse(
			Long channelId, Long cartId, Note note)
		throws Exception;

	public void deleteChannelCartNote(Long channelId, Long cartId, Long noteId)
		throws Exception;

	public HttpInvoker.HttpResponse deleteChannelCartNoteHttpResponse(
			Long channelId, Long cartId, Long noteId)
		throws Exception;

	public Note getChannelCartNote(Long channelId, Long cartId, Long noteId)
		throws Exception;

	public HttpInvoker.HttpResponse getChannelCartNoteHttpResponse(
			Long channelId, Long cartId, Long noteId)
		throws Exception;

	public void patchChannelCartNote(
			Long channelId, Long cartId, Long noteId, Note note)
		throws Exception;

	public HttpInvoker.HttpResponse patchChannelCartNoteHttpResponse(
			Long channelId, Long cartId, Long noteId, Note note)
		throws Exception;

	public static class Builder {

		public Builder authentication(String login, String password) {
			_login = login;
			_password = password;

			return this;
		}

		public NoteResource build() {
			return new NoteResourceImpl(this);
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

	public static class NoteResourceImpl implements NoteResource {

		public Page<Note> getChannelCartNotesPage(
				Long channelId, Long cartId, Pagination pagination)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getChannelCartNotesPageHttpResponse(
					channelId, cartId, pagination);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			return Page.of(content, NoteSerDes::toDTO);
		}

		public HttpInvoker.HttpResponse getChannelCartNotesPageHttpResponse(
				Long channelId, Long cartId, Pagination pagination)
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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/notes",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Note postChannelCartNote(Long channelId, Long cartId, Note note)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				postChannelCartNoteHttpResponse(channelId, cartId, note);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return NoteSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse postChannelCartNoteHttpResponse(
				Long channelId, Long cartId, Note note)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(note.toString(), "application/json");

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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/notes",
				channelId, cartId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void deleteChannelCartNote(
				Long channelId, Long cartId, Long noteId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				deleteChannelCartNoteHttpResponse(channelId, cartId, noteId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());
		}

		public HttpInvoker.HttpResponse deleteChannelCartNoteHttpResponse(
				Long channelId, Long cartId, Long noteId)
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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/notes/{noteId}",
				channelId, cartId, noteId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public Note getChannelCartNote(Long channelId, Long cartId, Long noteId)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				getChannelCartNoteHttpResponse(channelId, cartId, noteId);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			try {
				return NoteSerDes.toDTO(content);
			}
			catch (Exception e) {
				_logger.log(
					Level.WARNING,
					"Unable to process HTTP response: " + content, e);

				throw e;
			}
		}

		public HttpInvoker.HttpResponse getChannelCartNoteHttpResponse(
				Long channelId, Long cartId, Long noteId)
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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/notes/{noteId}",
				channelId, cartId, noteId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		public void patchChannelCartNote(
				Long channelId, Long cartId, Long noteId, Note note)
			throws Exception {

			HttpInvoker.HttpResponse httpResponse =
				patchChannelCartNoteHttpResponse(
					channelId, cartId, noteId, note);

			String content = httpResponse.getContent();

			_logger.fine("HTTP response content: " + content);

			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());
		}

		public HttpInvoker.HttpResponse patchChannelCartNoteHttpResponse(
				Long channelId, Long cartId, Long noteId, Note note)
			throws Exception {

			HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

			httpInvoker.body(note.toString(), "application/json");

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
						"/o/headless-commerce-delivery-cart/v1.0/channels/{channelId}/carts/{cartId}/notes/{noteId}",
				channelId, cartId, noteId);

			httpInvoker.userNameAndPassword(
				_builder._login + ":" + _builder._password);

			return httpInvoker.invoke();
		}

		private NoteResourceImpl(Builder builder) {
			_builder = builder;
		}

		private static final Logger _logger = Logger.getLogger(
			NoteResource.class.getName());

		private Builder _builder;

	}

}