/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import com.liferay.osb.asah.common.constants.ServiceConstants;

import io.lettuce.core.ReadFrom;

import java.net.MalformedURLException;
import java.net.URL;

import java.time.Duration;

import java.util.Arrays;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.hadoop.util.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Inácio Nery
 * @author Pedro Queiroz
 * @author Shinn Lok
 */
@Configuration
@EnableCaching
public class OSBAsahCachingConfigurerSupport extends CachingConfigurerSupport {

	@Bean
	@Override
	public CacheResolver cacheResolver() {
		if (_cacheEnabled) {
			return new OSBAsahCacheResolver(osbAsahCacheManager());
		}

		return new OSBAsahCacheResolver(new NoOpCacheManager());
	}

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();

			Class<?> clazz = OSBAsahCacheUtil.extractTargetClass(target);

			sb.append(clazz.getSimpleName());

			sb.append("#");
			sb.append(method.getName());
			sb.append("#");
			sb.append(Arrays.deepHashCode(method.getParameterTypes()));
			sb.append("#");
			sb.append(Arrays.deepHashCode(params));

			return sb.toString();
		};
	}

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		try {
			LettucePoolingClientConfigurationBuilder
				lettucePoolingClientConfigurationBuilder =
					LettucePoolingClientConfiguration.builder();

			lettucePoolingClientConfigurationBuilder.readFrom(
				ReadFrom.REPLICA_PREFERRED);

			lettucePoolingClientConfigurationBuilder.commandTimeout(
				Duration.ofSeconds(10));
			lettucePoolingClientConfigurationBuilder.shutdownTimeout(
				Duration.ofSeconds(10));

			GenericObjectPoolConfig genericObjectPoolConfig =
				new GenericObjectPoolConfig();

			genericObjectPoolConfig.setMaxTotal(16);
			genericObjectPoolConfig.setMaxIdle(16);

			lettucePoolingClientConfigurationBuilder.poolConfig(
				genericObjectPoolConfig);

			return _newLettuceConnectionFactory(
				lettucePoolingClientConfigurationBuilder.build());
		}
		catch (MalformedURLException malformedURLException) {
			throw new IllegalArgumentException(malformedURLException);
		}
	}

	@Bean
	public OSBAsahCacheManager osbAsahCacheManager() {
		return new OSBAsahCacheManager(redisTemplate());
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		return new RedisTemplate<Object, Object>() {
			{
				setConnectionFactory(lettuceConnectionFactory());
				setEnableTransactionSupport(false);
				setKeySerializer(StringRedisSerializer.UTF_8);
			}
		};
	}

	private LettuceConnectionFactory _newLettuceConnectionFactory(
			LettuceClientConfiguration lettuceClientConfiguration)
		throws MalformedURLException {

		String[] redisNodesUrls = StringUtils.split(
			ServiceConstants.URL_REDIS, ',');

		if (redisNodesUrls.length == 1) {
			URL url = new URL(redisNodesUrls[0].trim());

			return new LettuceConnectionFactory(
				new RedisStandaloneConfiguration(url.getHost(), url.getPort()),
				lettuceClientConfiguration);
		}

		URL redisMasterURL = new URL(redisNodesUrls[0]);

		RedisStaticMasterReplicaConfiguration
			redisStaticMasterReplicaConfiguration =
				new RedisStaticMasterReplicaConfiguration(
					redisMasterURL.getHost(), redisMasterURL.getPort());

		for (int i = 1; i < redisNodesUrls.length; i++) {
			URL url = new URL(redisNodesUrls[i].trim());

			redisStaticMasterReplicaConfiguration.addNode(
				url.getHost(), url.getPort());
		}

		return new LettuceConnectionFactory(
			redisStaticMasterReplicaConfiguration, lettuceClientConfiguration);
	}

	@Value("${osb.asah.cache.enabled:true}")
	private boolean _cacheEnabled;

}