/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception.handler;

import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.annotation.SuppressErrorLogging;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahError;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.HandlerMethod;

/**
 * @author Matthew Kong
 * @author Rachael Koestartyo
 */
@ControllerAdvice
public class ResponseEntityExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<OSBAsahError> handleIllegalArgumentException(
		HandlerMethod handlerMethod, HttpServletRequest httpServletRequest,
		IllegalArgumentException illegalArgumentException) {

		return _getResponseEntity(
			illegalArgumentException, handlerMethod, httpServletRequest,
			HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<OSBAsahError> handleMethodArgumentNotValidException(
			HandlerMethod handlerMethod, HttpServletRequest httpServletRequest,
			MethodArgumentNotValidException methodArgumentNotValidException)
		throws Exception {

		BindingResult bindingResult =
			methodArgumentNotValidException.getBindingResult();

		return _getResponseEntity(
			JSONUtil.put(
				"errorCount", bindingResult.getErrorCount()
			).put(
				"fieldErrors",
				JSONUtil.toJSONArray(
					bindingResult.getFieldErrors(),
					fieldError -> JSONUtil.put(
						fieldError.getField(), fieldError.getDefaultMessage()))
			),
			methodArgumentNotValidException, handlerMethod, httpServletRequest,
			HttpStatus.BAD_REQUEST, null);
	}

	@ExceptionHandler(OSBAsahException.class)
	public ResponseEntity<OSBAsahError> handleOSBAsahException(
		HandlerMethod handlerMethod, HttpServletRequest httpServletRequest,
		OSBAsahException osbAsahException) {

		return _getResponseEntity(
			null, osbAsahException, handlerMethod, httpServletRequest,
			osbAsahException.getHttpStatus(), osbAsahException.getMessageKey());
	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<OSBAsahError> handleRestClientException(
		HandlerMethod handlerMethod, HttpServletRequest httpServletRequest,
		RestClientException restClientException) {

		if (restClientException instanceof HttpClientErrorException) {
			HttpClientErrorException httpClientErrorException =
				(HttpClientErrorException)restClientException;

			return _getResponseEntity(
				restClientException, handlerMethod, httpServletRequest,
				httpClientErrorException.getStatusCode());
		}

		return _getResponseEntity(
			restClientException, handlerMethod, httpServletRequest,
			HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<OSBAsahError> _getResponseEntity(
		Exception exception, HandlerMethod handlerMethod,
		HttpServletRequest httpServletRequest, HttpStatus httpStatus) {

		return _getResponseEntity(
			null, exception, handlerMethod, httpServletRequest, httpStatus,
			null);
	}

	private ResponseEntity<OSBAsahError> _getResponseEntity(
		JSONObject debugInfoJSONObject, Exception exception,
		HandlerMethod handlerMethod, HttpServletRequest httpServletRequest,
		HttpStatus httpStatus, String messageKey) {

		if (_shouldLogError(exception, handlerMethod)) {
			_log.error("Unable to process request", exception);
		}
		else if (_log.isDebugEnabled()) {
			_log.debug("Unable to process request", exception);
		}

		OSBAsahError osbAsahError = new OSBAsahError(
			_environment.getActiveProfiles());

		if (debugInfoJSONObject != null) {
			osbAsahError.setErrorAttribute("debugInfo", debugInfoJSONObject);
		}

		osbAsahError.setErrorAttribute("error", httpStatus.getReasonPhrase());

		Class<?> clazz = exception.getClass();

		osbAsahError.setErrorAttribute("exception", clazz.getName());

		osbAsahError.setErrorAttribute("message", exception.getMessage());

		if (messageKey != null) {
			osbAsahError.setErrorAttribute("messageKey", messageKey);
		}

		osbAsahError.setErrorAttribute(
			"path", httpServletRequest.getRequestURI());
		osbAsahError.setErrorAttribute("status", httpStatus.value());
		osbAsahError.setErrorAttribute("timestamp", System.currentTimeMillis());

		return new ResponseEntity<>(osbAsahError, httpStatus);
	}

	private boolean _shouldLogError(
		Exception exception, HandlerMethod handlerMethod) {

		if (handlerMethod == null) {
			return true;
		}

		SuppressErrorLogging suppressErrorLogging =
			handlerMethod.getMethodAnnotation(SuppressErrorLogging.class);

		if ((suppressErrorLogging != null) &&
			(suppressErrorLogging.value() == exception.getClass())) {

			return false;
		}

		return true;
	}

	private static final Log _log = LogFactory.getLog(
		ResponseEntityExceptionHandler.class);

	@Autowired
	private Environment _environment;

}