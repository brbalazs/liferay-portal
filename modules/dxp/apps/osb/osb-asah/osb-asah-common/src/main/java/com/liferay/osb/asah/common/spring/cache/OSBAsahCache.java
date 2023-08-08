/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.util.Pool;

import com.liferay.osb.asah.common.lock.KeyReentrantLock;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Inácio Nery
 */
public class OSBAsahCache extends AbstractValueAdaptingCache {

	public OSBAsahCache(
		Cache caffeineCache, Pool<Kryo> kryoPool, String name, Cache redisCache,
		RedisTemplate<Object, Object> redisTemplate) {

		super(true);

		_caffeineCache = caffeineCache;
		_kryoPool = kryoPool;
		_name = name;
		_redisCache = redisCache;
		_redisTemplate = redisTemplate;
	}

	@Override
	public void clear() {
		_clearRedisCache(null);

		clearCaffeineCache(null);
	}

	@Override
	public void evict(Object key) {
		_clearRedisCache(key);

		clearCaffeineCache(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Callable<T> callable) {
		ReentrantLock reentrantLock = KeyReentrantLock.getReentrantLock(
			getClass(), ProjectIdThreadLocal.getProjectId(), key);

		try {
			reentrantLock.lock();

			Object value = lookup(key);

			if (value != null) {
				return (T)value;
			}

			try {
				value = callable.call();
			}
			catch (Throwable throwable) {
				throw new ValueRetrievalException(key, callable, throwable);
			}

			put(key, value);

			return (T)value;
		}
		finally {
			reentrantLock.unlock();
		}
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public void put(Object key, Object value) {
		_put(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		ReentrantLock reentrantLock = KeyReentrantLock.getReentrantLock(
			getClass(), ProjectIdThreadLocal.getProjectId(), key);

		try {
			reentrantLock.lock();

			ValueWrapper valueWrapper = toValueWrapper(lookup(key));

			if (valueWrapper == null) {
				_put(key, value);
			}

			return valueWrapper;
		}
		finally {
			reentrantLock.unlock();
		}
	}

	protected void clearCaffeineCache(Object key) {
		if (key == null) {
			_caffeineCache.clear();

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Caffeine cache with name %s cleared", _name));
			}
		}
		else {
			_caffeineCache.evict(key);

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Caffeine cache with name %s and key %s cleared", _name,
						key));
			}
		}
	}

	@Override
	protected Object lookup(Object key) {
		ValueWrapper valueWrapper = _caffeineCache.get(key);

		if (valueWrapper != null) {
			Object object = _deserialize(valueWrapper.get());

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Got value %s from Caffeine with name %s and key %s",
						object, _name, key));
			}

			return object;
		}

		valueWrapper = _redisCache.get(key);

		if (valueWrapper != null) {
			Object object = _deserialize(valueWrapper.get());

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Got value %s from Redis with name %s and key %s",
						object, _name, key));
			}

			_caffeineCache.put(key, valueWrapper.get());

			return object;
		}

		return null;
	}

	private void _clearRedisCache(Object key) {
		if (key == null) {
			_redisCache.clear();

			_sendRedisMessage(
				new OSBAsahCacheMessage(_getHostAddress(), null, _name));

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format("Redis cache with name %s cleared", _name));
			}
		}
		else {
			_redisCache.evict(key);

			_sendRedisMessage(
				new OSBAsahCacheMessage(_getHostAddress(), key, _name));

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Redis cache with name %s and key %s cleared", _name,
						key));
			}
		}
	}

	private Object _deserialize(Object object) {
		if (object == null) {
			return null;
		}

		byte[] bytes = (byte[])object;

		if (bytes.length == 0) {
			if (_log.isWarnEnabled()) {
				_log.warn("Attempt to deserialize a zero byte object");
			}

			return null;
		}

		Kryo kryo = _kryoPool.obtain();

		try (ByteBufferInput byteBufferInput = new ByteBufferInput(bytes)) {
			return kryo.readClassAndObject(byteBufferInput);
		}
		catch (Exception exception) {
			throw new KryoException("Cannot deserialize object", exception);
		}
		finally {
			_kryoPool.free(kryo);
		}
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

	private void _put(Object key, Object value) {
		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"Put cache with name %s, key %s and value %s", _name, key,
					value));
		}

		byte[] bytes = _serialize(value);

		_redisCache.put(key, bytes);

		_sendRedisMessage(
			new OSBAsahCacheMessage(_getHostAddress(), key, _name));

		_caffeineCache.put(key, bytes);
	}

	private void _sendRedisMessage(OSBAsahCacheMessage osbAsahCacheMessage) {
		_redisTemplate.convertAndSend(
			"cache:redis:caffeine:topic", osbAsahCacheMessage);
	}

	private byte[] _serialize(Object object) {
		if (object == null) {
			return null;
		}

		Kryo kryo = _kryoPool.obtain();

		try (ByteBufferOutput byteBufferOutput = new ByteBufferOutput(
				1024, -1)) {

			kryo.writeClassAndObject(byteBufferOutput, object);

			return byteBufferOutput.toBytes();
		}
		catch (Exception exception) {
			throw new KryoException("Cannot serialize object", exception);
		}
		finally {
			_kryoPool.free(kryo);
		}
	}

	private static final Log _log = LogFactory.getLog(OSBAsahCache.class);

	private final Cache _caffeineCache;
	private String _hostAddress;
	private final Pool<Kryo> _kryoPool;
	private final String _name;
	private final Cache _redisCache;
	private final RedisTemplate<Object, Object> _redisTemplate;

}