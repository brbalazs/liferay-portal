/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule.callback;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.SearchEngineHelperUtil;
import com.liferay.portal.kernel.servlet.ServletContextClassLoaderPool;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.test.rule.ArquillianUtil;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.PortalLifecycle;
import com.liferay.portal.kernel.util.PortalLifecycleUtil;
import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.servlet.MainServlet;
import com.liferay.portal.test.mock.AutoDeployMockServletContext;

import javax.servlet.ServletException;

import org.junit.runner.Description;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Shuyang Zhou
 */
public class MainServletTestCallback extends BaseTestCallback<Void, Void> {

	public static final MainServletTestCallback INSTANCE =
		new MainServletTestCallback();

	public static MainServlet getMainServlet() {
		return _mainServlet;
	}

	@Override
	public void afterClass(Description description, Void c)
		throws PortalException {

		if (ArquillianUtil.isArquillianTest(description)) {
			return;
		}

		SearchEngineHelperUtil.removeCompany(TestPropsValues.getCompanyId());
	}

	@Override
	public Void beforeClass(Description description) {
		if (ArquillianUtil.isArquillianTest(description)) {
			return null;
		}

		if (_mainServlet == null) {
			final MockServletContext mockServletContext =
				new AutoDeployMockServletContext(
					new FileSystemResourceLoader());

			mockServletContext.setServletContextName(StringPool.BLANK);

			Thread currentThread = Thread.currentThread();

			ServletContextClassLoaderPool.register(
				StringPool.BLANK, currentThread.getContextClassLoader());

			PortalLifecycleUtil.register(
				new PortalLifecycle() {

					@Override
					public void portalDestroy() {
					}

					@Override
					public void portalInit() {
						ModuleFrameworkUtilAdapter.registerContext(
							mockServletContext);
					}

				});

			ServletContextPool.put(StringPool.BLANK, mockServletContext);

			MockServletConfig mockServletConfig = new MockServletConfig(
				mockServletContext);

			_mainServlet = new MainServlet();

			try {
				_mainServlet.init(mockServletConfig);
			}
			catch (ServletException se) {
				throw new RuntimeException(
					"The main servlet could not be initialized");
			}

			ServiceTestUtil.initStaticServices();
		}

		ServiceTestUtil.initServices();

		ServiceTestUtil.initPermissions();

		return null;
	}

	protected MainServletTestCallback() {
	}

	private static MainServlet _mainServlet;

}