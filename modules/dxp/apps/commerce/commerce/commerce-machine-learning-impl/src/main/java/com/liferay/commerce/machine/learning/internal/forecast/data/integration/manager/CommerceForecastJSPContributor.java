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

import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessActionHelper;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributor;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;

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
	public Process processAction(ActionRequest actionRequest, Process process)
		throws Exception {

		String remoteServiceEndpoint = ParamUtil.getString(
			actionRequest, "forecastServiceEndpoint");

		URL url = new URL(remoteServiceEndpoint);

		String customOptions = ParamUtil.getString(
			actionRequest, "customOptions");

		String level = ParamUtil.getString(actionRequest, "level");

		String period = ParamUtil.getString(actionRequest, "period");

		String target = ParamUtil.getString(actionRequest, "target");

		UnicodeProperties contextProperties = new UnicodeProperties(true);

		contextProperties.put("level", level);
		contextProperties.put("period", period);
		contextProperties.put("target", target);
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

		Process process = _dataIntegrationProcessActionHelper.getProcess(
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

			String forecastServiceEndpoint = url.toString();

			String level = contextPropertiesUnicode.getProperty("level");

			String period = contextPropertiesUnicode.getProperty("period");

			String target = contextPropertiesUnicode.getProperty("target");

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
				"forecastServiceEndpoint", forecastServiceEndpoint);

			httpServletRequest.setAttribute("level", level);

			httpServletRequest.setAttribute("period", period);

			httpServletRequest.setAttribute("target", target);
		}

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/forecast/view.jsp");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceForecastJSPContributor.class);

	private static final List<String> _mainProperties = Arrays.asList(
		"protocol", "host.name", "host.port", "level", "period", "target");

	@Reference
	private DataIntegrationProcessActionHelper
		_dataIntegrationProcessActionHelper;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.machine.learning.impl)"
	)
	private ServletContext _servletContext;

}