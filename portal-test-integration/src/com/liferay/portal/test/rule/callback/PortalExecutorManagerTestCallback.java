/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.runner.Description;

/**
 * @author     Michael C. Han
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PortalExecutorManagerTestCallback
	extends BaseTestCallback<Object, Object> {

	public static final PortalExecutorManagerTestCallback INSTANCE =
		new PortalExecutorManagerTestCallback();

	@Override
	public void afterClass(Description description, Object o) {
		_portalExecutorManager.shutdown(true);
	}

	protected class MockPortalExecutorManager implements PortalExecutorManager {

		@Override
		public ThreadPoolExecutor getPortalExecutor(String name) {
			return _threadPoolExecutor;
		}

		@Override
		public ThreadPoolExecutor getPortalExecutor(
			String name, boolean createIfAbsent) {

			return _threadPoolExecutor;
		}

		@Override
		public ThreadPoolExecutor registerPortalExecutor(
			String name, ThreadPoolExecutor threadPoolExecutor) {

			return _threadPoolExecutor;
		}

		@Override
		public void shutdown() {
			shutdown(false);
		}

		@Override
		public void shutdown(boolean interrupt) {
			if (interrupt) {
				_threadPoolExecutor.shutdownNow();
			}
			else {
				_threadPoolExecutor.shutdown();
			}
		}

		private final ThreadPoolExecutor _threadPoolExecutor =
			new ThreadPoolExecutor(0, 1);

	}

	private PortalExecutorManagerTestCallback() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_portalExecutorManager = new MockPortalExecutorManager();

		registry.registerService(
			PortalExecutorManager.class, _portalExecutorManager);
	}

	private final PortalExecutorManager _portalExecutorManager;

}