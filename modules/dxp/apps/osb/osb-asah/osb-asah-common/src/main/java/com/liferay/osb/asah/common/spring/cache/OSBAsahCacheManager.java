/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.util.Pool;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Inácio Nery
 */
public class OSBAsahCacheManager implements CacheManager {

	public OSBAsahCacheManager(RedisTemplate<Object, Object> redisTemplate) {
		_redisTemplate = redisTemplate;

		_caffeineCacheManager = _createCaffeineCacheManager();
		_redisCacheManager = _createRedisCacheManager(
			redisTemplate.getRequiredConnectionFactory());
	}

	@Override
	public Cache getCache(String name) {
		return _osbAsahCaches.computeIfAbsent(name, this::_createOSBAsahCache);
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(_osbAsahCaches.keySet());
	}

	protected void clearCaffeineCache(String name, Object key) {
		OSBAsahCache osbAsahCache = _osbAsahCaches.get(name);

		if (osbAsahCache == null) {
			return;
		}

		osbAsahCache.clearCaffeineCache(key);
	}

	private CaffeineCacheManager _createCaffeineCacheManager() {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

		Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();

		caffeineBuilder.expireAfterAccess(Duration.ofSeconds(5));
		caffeineBuilder.maximumSize(10000);

		caffeineCacheManager.setCaffeine(caffeineBuilder);

		return caffeineCacheManager;
	}

	private OSBAsahCache _createOSBAsahCache(String name) {
		if (_log.isDebugEnabled()) {
			_log.debug("Created OSBAsahCache instance " + name);
		}

		return new OSBAsahCache(
			_caffeineCacheManager.getCache(name), _kryoPool, name,
			_redisCacheManager.getCache(name), _redisTemplate);
	}

	private RedisCacheManager _createRedisCacheManager(
		RedisConnectionFactory redisConnectionFactory) {

		RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
			RedisCacheManager.builder();

		RedisCacheConfiguration redisCacheConfiguration =
			RedisCacheConfiguration.defaultCacheConfig();

		redisCacheManagerBuilder.cacheDefaults(
			redisCacheConfiguration.entryTtl(Duration.ofDays(2)));

		redisCacheManagerBuilder.cacheWriter(
			RedisCacheWriter.nonLockingRedisCacheWriter(
				redisConnectionFactory));

		return redisCacheManagerBuilder.build();
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahCacheManager.class);

	private final CaffeineCacheManager _caffeineCacheManager;

	private final Pool<Kryo> _kryoPool = new Pool<Kryo>(true, false, 1000) {

		@Override
		protected Kryo create() {
			Kryo kryo = new Kryo();

			kryo.register(
				PageImpl.class,
				new FieldSerializer<PageImpl<?>>(kryo, PageImpl.class) {

					@Override
					protected PageImpl<?> create(
						Kryo kryo, Input input,
						Class<? extends PageImpl<?>> clazz) {

						return new PageImpl<>(Collections.emptyList());
					}

				});
			kryo.register(
				PageRequest.class,
				new FieldSerializer<PageRequest>(kryo, PageRequest.class) {

					@Override
					protected PageRequest create(
						Kryo kryo, Input input,
						Class<? extends PageRequest> clazz) {

						return PageRequest.of(0, 1);
					}

				});
			kryo.register(
				Sort.class,
				new FieldSerializer<Sort>(kryo, Sort.class) {

					@Override
					protected Sort create(
						Kryo kryo, Input input, Class<? extends Sort> clazz) {

						return Sort.unsorted();
					}

				});
			kryo.register(
				Sort.Order.class,
				new FieldSerializer<Sort.Order>(kryo, Sort.Order.class) {

					@Override
					protected Sort.Order create(
						Kryo kryo, Input input,
						Class<? extends Sort.Order> clazz) {

						return Sort.Order.by("id");
					}

				});
			kryo.setRegistrationRequired(false);

			return kryo;
		}

	};

	private final ConcurrentMap<String, OSBAsahCache> _osbAsahCaches =
		new ConcurrentHashMap<>();
	private final RedisCacheManager _redisCacheManager;
	private final RedisTemplate<Object, Object> _redisTemplate;

}