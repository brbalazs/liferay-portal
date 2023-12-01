/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.servlet.filter;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author Ivica Cardic
 */
public class ForwardedHeaderFilter extends OncePerRequestFilter {

	public ForwardedHeaderFilter() {
		_forwardedHeaderNames.add("Forwarded");
		_forwardedHeaderNames.add("X-Forwarded-For");
		_forwardedHeaderNames.add("X-Forwarded-Host");
		_forwardedHeaderNames.add("X-Forwarded-Port");
		_forwardedHeaderNames.add("X-Forwarded-Prefix");
		_forwardedHeaderNames.add("X-Forwarded-Proto");
		_forwardedHeaderNames.add("X-Forwarded-Ssl");
		_forwardedHeaderNames.add("X-Liferay-Origin-Forwarded-Host");
		_forwardedHeaderNames.add("X-Liferay-Origin-Forwarded-Port");
		_forwardedHeaderNames.add("X-Liferay-Origin-Forwarded-Proto");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest wrappedHttpServletRequest =
			new ForwardedHeaderExtractingRequest(httpServletRequest);

		HttpServletResponse wrappedHttpServletResponse =
			new ForwardedHeaderExtractingResponse(
				wrappedHttpServletRequest, httpServletResponse);

		filterChain.doFilter(
			wrappedHttpServletRequest, wrappedHttpServletResponse);
	}

	@Override
	protected void doFilterNestedErrorDispatch(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
		for (String forwardedHeaderName : _forwardedHeaderNames) {
			if (httpServletRequest.getHeader(forwardedHeaderName) != null) {
				return false;
			}
		}

		return true;
	}

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected boolean shouldNotFilterErrorDispatch() {
		return false;
	}

	private static final Set<String> _forwardedHeaderNames =
		Collections.newSetFromMap(
			new LinkedCaseInsensitiveMap<>(10, Locale.ENGLISH));

	private static class ForwardedHeaderExtractingRequest
		extends ForwardedHeaderRemovingRequest {

		@Override
		public String getContextPath() {
			return _forwardedPrefixExtractor.getContextPath();
		}

		@Override
		public String getRemoteAddr() {
			if (_remoteAddress != null) {
				return _remoteAddress.getHostString();
			}

			return super.getRemoteAddr();
		}

		@Override
		public String getRemoteHost() {
			if (_remoteAddress != null) {
				return _remoteAddress.getHostString();
			}

			return super.getRemoteHost();
		}

		@Override
		public int getRemotePort() {
			if (_remoteAddress != null) {
				return _remoteAddress.getPort();
			}

			return super.getRemotePort();
		}

		@Override
		public String getRequestURI() {
			return _forwardedPrefixExtractor.getRequestUri();
		}

		@Override
		public StringBuffer getRequestURL() {
			return _forwardedPrefixExtractor.getRequestUrl();
		}

		@Override
		public String getScheme() {
			return _scheme;
		}

		@Override
		public String getServerName() {
			return _host;
		}

		@Override
		public int getServerPort() {
			return _port;
		}

		@Override
		public boolean isSecure() {
			return _secure;
		}

		private ForwardedHeaderExtractingRequest(
			HttpServletRequest httpServletRequest) {

			super(httpServletRequest);

			ServerHttpRequest servletServerHttpRequest =
				new ServletServerHttpRequest(httpServletRequest);

			UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(
				servletServerHttpRequest
			).build();

			int port = uriComponents.getPort();

			_scheme = uriComponents.getScheme();

			_secure = _scheme.equals("https") || _scheme.equals("wss");

			_host = uriComponents.getHost();

			_port = (port == -1) ? (_secure ? 443 : 80) : port;

			_remoteAddress = UriComponentsBuilder.parseForwardedFor(
				servletServerHttpRequest,
				servletServerHttpRequest.getRemoteAddress());

			String baseUrl =
				_scheme + "://" + _host + ((port == -1) ? "" : ":" + port);
			Supplier<HttpServletRequest> delegateRequest =
				() -> (HttpServletRequest)getRequest();

			_forwardedPrefixExtractor = new ForwardedPrefixExtractor(
				delegateRequest, baseUrl);
		}

		private final ForwardedPrefixExtractor _forwardedPrefixExtractor;
		private final String _host;
		private final int _port;
		private final InetSocketAddress _remoteAddress;
		private final String _scheme;
		private final boolean _secure;

	}

	private static class ForwardedHeaderExtractingResponse
		extends HttpServletResponseWrapper {

		@Override
		public void sendRedirect(String location) throws IOException {
			UriComponentsBuilder uriComponentsBuilder =
				UriComponentsBuilder.fromUriString(location);

			UriComponents uriComponents = uriComponentsBuilder.build();

			if (uriComponents.getScheme() != null) {
				super.sendRedirect(location);

				return;
			}

			if (location.startsWith("//")) {
				super.sendRedirect(
					uriComponentsBuilder.scheme(
						_httpServletRequest.getScheme()
					).toUriString());

				return;
			}

			String path = uriComponents.getPath();

			if ((path != null) && !path.startsWith(_FOLDER_SEPARATOR)) {
				path = StringUtils.applyRelativePath(
					_httpServletRequest.getRequestURI(), path);
			}

			uriComponentsBuilder = UriComponentsBuilder.fromHttpRequest(
				new ServletServerHttpRequest(_httpServletRequest));

			uriComponents = uriComponentsBuilder.replacePath(
				path
			).replaceQuery(
				uriComponents.getQuery()
			).fragment(
				uriComponents.getFragment()
			).build();

			UriComponents normalizedUriComponents = uriComponents.normalize();

			super.sendRedirect(normalizedUriComponents.toUriString());
		}

		private ForwardedHeaderExtractingResponse(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

			super(httpServletResponse);

			_httpServletRequest = httpServletRequest;
		}

		private static final String _FOLDER_SEPARATOR = "/";

		private final HttpServletRequest _httpServletRequest;

	}

	private static class ForwardedHeaderRemovingRequest
		extends HttpServletRequestWrapper {

		public ForwardedHeaderRemovingRequest(
			HttpServletRequest httpServletRequest) {

			super(httpServletRequest);

			_headers = _initHeaders(httpServletRequest);
		}

		@Override
		public String getHeader(String name) {
			List<String> value = _headers.get(name);

			if (CollectionUtils.isEmpty(value)) {
				return null;
			}

			return value.get(0);
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			return Collections.enumeration(_headers.keySet());
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> value = _headers.get(name);

			if (value == null) {
				return Collections.enumeration(Collections.emptySet());
			}

			return Collections.enumeration(value);
		}

		private Map<String, List<String>> _initHeaders(
			HttpServletRequest httpServletRequest) {

			Map<String, List<String>> headers = new LinkedCaseInsensitiveMap<>(
				Locale.ENGLISH);
			Enumeration<String> names = httpServletRequest.getHeaderNames();

			while (names.hasMoreElements()) {
				String name = names.nextElement();

				if (!_forwardedHeaderNames.contains(name)) {
					headers.put(
						name,
						Collections.list(httpServletRequest.getHeaders(name)));
				}
			}

			return headers;
		}

		private final Map<String, List<String>> _headers;

	}

	private static class ForwardedPrefixExtractor {

		public ForwardedPrefixExtractor(
			Supplier<HttpServletRequest> delegateRequest, String baseUrl) {

			HttpServletRequest httpServletRequest = delegateRequest.get();

			_delegateRequest = delegateRequest;
			_baseUrl = baseUrl;

			_actualRequestUri = httpServletRequest.getRequestURI();

			_forwardedPrefix = _initForwardedPrefix(delegateRequest.get());
			_requestUri = _initRequestUri();
			_requestUrl = _initRequestUrl();
		}

		public String getContextPath() {
			if (_forwardedPrefix != null) {
				return _forwardedPrefix;
			}

			HttpServletRequest httpServletRequest = _delegateRequest.get();

			return httpServletRequest.getContextPath();
		}

		public String getRequestUri() {
			if (_requestUri == null) {
				HttpServletRequest httpServletRequest = _delegateRequest.get();

				return httpServletRequest.getRequestURI();
			}

			_recalculatePathsIfNecessary();

			return _requestUri;
		}

		public StringBuffer getRequestUrl() {
			_recalculatePathsIfNecessary();

			return new StringBuffer(_requestUrl);
		}

		private String _initForwardedPrefix(HttpServletRequest request) {
			String result = null;
			Enumeration<String> names = request.getHeaderNames();

			while (names.hasMoreElements()) {
				String name = names.nextElement();

				if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
					result = request.getHeader(name);
				}
			}

			if (result != null) {
				StringBuilder prefix = new StringBuilder(result.length());
				String[] rawPrefixes = StringUtils.tokenizeToStringArray(
					result, ",");

				for (String rawPrefix : rawPrefixes) {
					int endIndex = rawPrefix.length();

					while ((endIndex > 0) &&
						   (rawPrefix.charAt(endIndex - 1) == '/')) {

						endIndex--;
					}

					prefix.append(
						(endIndex != rawPrefix.length()) ?
							rawPrefix.substring(0, endIndex) : rawPrefix);
				}

				return prefix.toString();
			}

			return null;
		}

		private String _initRequestUri() {
			if (_forwardedPrefix != null) {
				String path =
					UrlPathHelper.rawPathInstance.getPathWithinApplication(
						_delegateRequest.get());

				return _forwardedPrefix + path;
			}

			return null;
		}

		private String _initRequestUrl() {
			if (_requestUri != null) {
				return _baseUrl + _requestUri;
			}

			HttpServletRequest httpServletRequest = _delegateRequest.get();

			return _baseUrl + httpServletRequest.getRequestURI();
		}

		private void _recalculatePathsIfNecessary() {
			HttpServletRequest httpServletRequest = _delegateRequest.get();

			if (!_actualRequestUri.equals(httpServletRequest.getRequestURI())) {
				_actualRequestUri = httpServletRequest.getRequestURI();
				_requestUri = _initRequestUri();
				_requestUrl = _initRequestUrl();
			}
		}

		private String _actualRequestUri;
		private final String _baseUrl;
		private final Supplier<HttpServletRequest> _delegateRequest;
		private final String _forwardedPrefix;
		private String _requestUri;
		private String _requestUrl;

	}

}