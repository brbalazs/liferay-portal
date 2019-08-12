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

package com.liferay.commerce.machine.learning.internal.recommend.data.integration.manager;

import com.liferay.commerce.data.integration.constants.CommerceDataIntegrationWebKeys;
import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.process.type.ProcessTypeJSPContributor;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.IOException;

import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseCommerceRecommendJSPContributor
	implements ProcessTypeJSPContributor {

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		CommerceDataIntegrationProcess commerceDataIntegrationProcess =
			(CommerceDataIntegrationProcess)httpServletRequest.getAttribute(
				CommerceDataIntegrationWebKeys.
					COMMERCE_DATA_INTEGRATION_PROCESS);

		if (commerceDataIntegrationProcess != null) {
			UnicodeProperties typeSettingsProperties =
				commerceDataIntegrationProcess.getTypeSettingsProperties();

			String protocol = typeSettingsProperties.get("protocol");

			String host = typeSettingsProperties.get("host.name");

			int port = GetterUtil.getInteger(
				typeSettingsProperties.getProperty("host.port", "80"));

			URL url = new URL(protocol, host, port, "");

			String recommendServiceEndpoint = url.toString();

			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, String> contextPropertyEntry :
					typeSettingsProperties.entrySet()) {

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

	private static final List<String> _mainProperties = Arrays.asList(
		"protocol", "host.name", "host.port");

}