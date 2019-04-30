/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.recommend.internal.data.integration.manager;

import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessActionHelper;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributor;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseCommerceRecommendJSPContributor
	implements ProcessTypeJSPContributor {

	@Override
	public Process processAction(ActionRequest actionRequest, Process process)
		throws Exception {

		String remoteServiceEndpoint = ParamUtil.getString(
			actionRequest, "recommendServiceEndpoint");

		URL url = new URL(remoteServiceEndpoint);

		String customOptions = ParamUtil.getString(
			actionRequest, "customOptions");

		UnicodeProperties contextProperties = new UnicodeProperties(true);

		contextProperties.put("host.name", url.getHost());
		contextProperties.put("host.port", String.valueOf(url.getPort()));
		contextProperties.put("protocol", url.getProtocol());

		for (String customOption : customOptions.split(StringPool.NEW_LINE)) {
			if (Validator.isNull(customOption)) {
				continue;
			}

			String[] tokens = customOption.split(StringPool.EQUAL, 2);

			if (tokens.length != 2) {
				if (_log.isWarnEnabled()) {
					_log.warn("Skipping malformed property: " + customOption);
				}

				continue;
			}

			if (!_mainProperties.contains(tokens[0])) {
				contextProperties.put(tokens[0], tokens[1]);
			}
		}

		process.setContextProperties(contextProperties.toString());

		return process;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		Process process = getDataIntegrationProcessActionHelper().getProcess(
			httpServletRequest);

		if (process != null) {
			String contextProperties = process.getContextProperties();

			UnicodeProperties contextPropertiesUnicode = new UnicodeProperties(
				true);

			contextPropertiesUnicode.fastLoad(contextProperties);

			String protocol = contextPropertiesUnicode.get("protocol");

			String host = contextPropertiesUnicode.get("host.name");

			int port = GetterUtil.getInteger(
				contextPropertiesUnicode.getProperty("host.port", "80"));

			URL url = new URL(protocol, host, port, "");

			String recommendServiceEndpoint = url.toString();

			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, String> contextPropertyEntry :
					contextPropertiesUnicode.entrySet()) {

				if (!_mainProperties.contains(contextPropertyEntry.getKey())) {
					sb.append(contextPropertyEntry.getKey());
					sb.append(StringPool.EQUAL);
					sb.append(contextPropertyEntry.getValue());
					sb.append(StringPool.NEW_LINE);
				}
			}

			String customOptions = sb.toString();

			httpServletRequest.setAttribute("customOptions", customOptions);

			httpServletRequest.setAttribute(
				"recommendServiceEndpoint", recommendServiceEndpoint);
		}

		doRenderJSP(httpServletRequest, httpServletResponse);
	}

	protected abstract void doRenderJSP(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException;

	protected DataIntegrationProcessActionHelper
		getDataIntegrationProcessActionHelper() {

		return _serviceTracker.getService();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseCommerceRecommendJSPContributor.class);

	private static final List<String> _mainProperties = Arrays.asList(
		"protocol", "host.name", "host.port");
	private static final ServiceTracker
		<DataIntegrationProcessActionHelper, DataIntegrationProcessActionHelper>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			BaseCommerceRecommendJSPContributor.class);

		ServiceTracker
			<DataIntegrationProcessActionHelper,
			 DataIntegrationProcessActionHelper> serviceTracker =
				new ServiceTracker<>(
					bundle.getBundleContext(),
					DataIntegrationProcessActionHelper.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}