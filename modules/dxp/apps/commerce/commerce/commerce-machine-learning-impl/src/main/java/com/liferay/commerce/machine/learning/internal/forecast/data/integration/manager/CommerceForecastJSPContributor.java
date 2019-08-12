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

package com.liferay.commerce.machine.learning.internal.forecast.data.integration.manager;

import com.liferay.commerce.data.integration.constants.CommerceDataIntegrationWebKeys;
import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.process.type.ProcessTypeJSPContributor;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	property = "commerce.data.integration.process.type.key=" + CommerceForecastScheduledTaskExecutorService.NAME,
	service = ProcessTypeJSPContributor.class
)
public class CommerceForecastJSPContributor
	implements ProcessTypeJSPContributor {

	@Override
	public Map<String, String> processAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		return null;
	}

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

			String forecastServiceEndpoint = url.toString();

			String level = typeSettingsProperties.getProperty("level");

			String period = typeSettingsProperties.getProperty("period");

			String target = typeSettingsProperties.getProperty("target");

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
				"forecastServiceEndpoint", forecastServiceEndpoint);

			httpServletRequest.setAttribute("level", level);

			httpServletRequest.setAttribute("period", period);

			httpServletRequest.setAttribute("target", target);
		}

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/forecast/view.jsp");
	}

	private static final List<String> _mainProperties = Arrays.asList(
		"protocol", "host.name", "host.port", "level", "period", "target");

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.machine.learning.impl)"
	)
	private ServletContext _servletContext;

}