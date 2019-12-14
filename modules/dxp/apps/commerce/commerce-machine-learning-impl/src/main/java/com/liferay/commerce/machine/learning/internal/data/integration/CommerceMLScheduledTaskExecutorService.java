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

package com.liferay.commerce.machine.learning.internal.data.integration;

import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcessLog;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLocalService;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLogLocalService;
import com.liferay.commerce.machine.learning.internal.configuration.CommerceMLConfiguration;
import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.net.URL;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	configurationPid = "com.liferay.commerce.machine.learning.internal.configuration.CommerceMLConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	service = CommerceMLScheduledTaskExecutorService.class
)
public class CommerceMLScheduledTaskExecutorService {

	public void executeScheduledTask(
			long userId, long commerceDataIntegrationProcessId,
			Map<String, String> scheduledTaskContext)
		throws PortalException {

		CommerceDataIntegrationProcess commerceDataIntegrationProcess =
			_commerceDataIntegrationProcessLocalService.
				getCommerceDataIntegrationProcess(
					commerceDataIntegrationProcessId);

		Date startDate = new Date();

		CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog =
			_commerceDataIntegrationProcessLogLocalService.
				addCommerceDataIntegrationProcessLog(
					userId,
					commerceDataIntegrationProcess.
						getCommerceDataIntegrationProcessId(),
					null, null, BackgroundTaskConstants.STATUS_IN_PROGRESS,
					startDate, null);

		try {
			executeProcess(
				commerceDataIntegrationProcess, scheduledTaskContext);

			commerceDataIntegrationProcessLog.setEndDate(new Date());

			commerceDataIntegrationProcessLog.setStatus(
				BackgroundTaskConstants.STATUS_SUCCESSFUL);

			_commerceDataIntegrationProcessLogLocalService.
				updateCommerceDataIntegrationProcessLog(
					commerceDataIntegrationProcessLog);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}

			_commerceDataIntegrationProcessLogLocalService.
				updateCommerceDataIntegrationProcessLog(
					commerceDataIntegrationProcessLog.
						getCommerceDataIntegrationProcessLogId(),
					e.getMessage(), null, BackgroundTaskConstants.STATUS_FAILED,
					new Date());
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_commerceMLConfiguration = ConfigurableUtil.createConfigurable(
			CommerceMLConfiguration.class, properties);
	}

	protected void executeProcess(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess,
			Map<String, String> additionalProcessContextProperties)
		throws Exception {

		UnicodeProperties contextProperties =
			commerceDataIntegrationProcess.getTypeSettingsProperties();

		contextProperties.putAll(additionalProcessContextProperties);

		JSONWebServiceClient jsonWebServiceClient = getJSONWebServiceClient(
			contextProperties);

		JSONObject contextPropertiesJSONObject =
			JSONFactoryUtil.createJSONObject();

		Set<Map.Entry<String, String>> contextPropertiesEntrySet =
			contextProperties.entrySet();

		Stream<Map.Entry<String, String>> contextPropertiesStream =
			contextPropertiesEntrySet.stream();

		contextPropertiesStream.forEach(
			s -> contextPropertiesJSONObject.put(s.getKey(), s.getValue()));

		jsonWebServiceClient.doPostAsJSON(
			_COMMERCE_ML_URL_PATH, contextPropertiesJSONObject.toString());
	}

	protected JSONWebServiceClient getJSONWebServiceClient(
			UnicodeProperties contextProperties)
		throws Exception {

		URL url = new URL(
			contextProperties.getProperty(
				_COMMERCE_ML_BASE_URL,
				_commerceMLConfiguration.commerceMLBaseURL()));

		Dictionary<String, String> properties = new Hashtable<>();

		properties.put("hostName", url.getHost());
		properties.put("hostPort", String.valueOf(url.getPort()));
		properties.put("protocol", url.getProtocol());

		ComponentInstance componentInstance =
			_jsonWebServiceClientComponentFactory.newInstance(properties);

		return (JSONWebServiceClient)componentInstance.getInstance();
	}

	private static final String _COMMERCE_ML_BASE_URL = "commerce.ml.base.url";

	private static final String _COMMERCE_ML_URL_PATH = "/ml/update-model";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceMLScheduledTaskExecutorService.class);

	@Reference
	private CommerceDataIntegrationProcessLocalService
		_commerceDataIntegrationProcessLocalService;

	@Reference
	private CommerceDataIntegrationProcessLogLocalService
		_commerceDataIntegrationProcessLogLocalService;

	private CommerceMLConfiguration _commerceMLConfiguration;

	@Reference(target = "(component.factory=JSONWebServiceClient)")
	private ComponentFactory _jsonWebServiceClientComponentFactory;

}