/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.annotation.MessageBusChannel;
import com.liferay.osb.asah.test.util.messaging.MessageBusTestHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContext;

/**
 * @author André Miranda
 * @author Marcellus Tavares
 */
@TestComponent
public class OSBAsahMessageBusTestExecutionListener
	extends BaseOSBAsahTestExecutionListener {

	@Override
	public void afterTestMethod(TestContext testContext) {
		ProjectIdThreadLocal.setProjectId("test");

		MessageBusChannel messageBusChannel =
			AnnotatedElementUtils.findMergedAnnotation(
				testContext.getTestMethod(), MessageBusChannel.class);

		if (messageBusChannel != null) {
			MessageBusTestHelper messageBusTestHelper =
				new MessageBusTestHelper(_messageBus);

			messageBusTestHelper.clearMessageBusChannel(messageBusChannel);
		}

		for (String cacheName : _cacheManager.getCacheNames()) {
			Cache cache = _cacheManager.getCache(cacheName);

			if (cache != null) {
				cache.invalidate();
			}
		}

		ProjectIdThreadLocal.remove();
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		super.beforeTestClass(testContext);

		ProjectIdThreadLocal.setProjectId("test");
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		ProjectIdThreadLocal.setProjectId("test");

		MessageBusChannel messageBusChannel =
			AnnotatedElementUtils.findMergedAnnotation(
				testContext.getTestMethod(), MessageBusChannel.class);

		if (messageBusChannel != null) {
			MessageBusTestHelper messageBusTestHelper =
				new MessageBusTestHelper(_messageBus);

			messageBusTestHelper.prepareMessageBusChannel(
				testContext.getTestClass(), messageBusChannel);
		}
	}

	@Autowired
	private CacheManager _cacheManager;

	@Autowired
	private MessageBus _messageBus;

}