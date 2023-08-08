/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Inácio Nery
 */
public class OSBAsahCacheMessageListener implements MessageListener {

	public OSBAsahCacheMessageListener(
		OSBAsahCacheManager osbAsahCacheManager,
		RedisTemplate<Object, Object> redisTemplate) {

		_osbAsahCacheManager = osbAsahCacheManager;
		_redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		RedisSerializer<?> redisSerializer =
			_redisTemplate.getValueSerializer();

		OSBAsahCacheMessage osbAsahCacheMessage =
			(OSBAsahCacheMessage)redisSerializer.deserialize(message.getBody());

		if (osbAsahCacheMessage == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("OSB Asah cache message is null");
			}

			return;
		}

		if (Objects.equals(
				osbAsahCacheMessage.getHostAddress(), _getHostAddress())) {

			if (_log.isDebugEnabled()) {
				_log.debug("Ignoring OSB Asah cache message from same host");
			}

			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Received OSB Asah cache message");
		}

		_osbAsahCacheManager.clearCaffeineCache(
			osbAsahCacheMessage.getName(), osbAsahCacheMessage.getKey());
	}

	private String _getHostAddress() {
		if (_hostAddress != null) {
			return _hostAddress;
		}

		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			_hostAddress = inetAddress.getHostAddress();
		}
		catch (UnknownHostException unknownHostException) {
			_log.error(unknownHostException, unknownHostException);
		}

		return null;
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahCacheMessageListener.class);

	private String _hostAddress;
	private final OSBAsahCacheManager _osbAsahCacheManager;
	private final RedisTemplate<Object, Object> _redisTemplate;

}