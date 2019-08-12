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

import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLocalService;
import com.liferay.commerce.data.integration.service.ScheduledTaskExecutorService;
import com.liferay.commerce.machine.learning.internal.data.integration.manager.CommerceMachineLearningScheduledTaskExecutorService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = "data.integration.service.executor.key=" + CommerceForecastScheduledTaskExecutorService.NAME,
	service = ScheduledTaskExecutorService.class
)
public class CommerceForecastScheduledTaskExecutorService
	implements ScheduledTaskExecutorService {

	public static final String NAME = "forecast-service";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void runProcess(long commerceDataIntegrationProcessId)
		throws IOException, PortalException {

		CommerceDataIntegrationProcess commerceDataIntegrationProcess =
			_commerceDataIntegrationProcessLocalService.
				getCommerceDataIntegrationProcess(
					commerceDataIntegrationProcessId);

		Map<String, String> contextProperties = getContextProperties(
			commerceDataIntegrationProcess);

		_commerceMachineLearningScheduledTaskExecutorService.
			executeScheduledTask(
				commerceDataIntegrationProcess.getUserId(),
				commerceDataIntegrationProcess.
					getCommerceDataIntegrationProcessId(),
				contextProperties);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);
	}

	protected Map<String, String> getContextProperties(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess)
		throws PortalException {

		Map<String, String> contextProperties = new HashMap<>();

		// Forecast Application

		contextProperties.put("LIFERAY_RECOMMEND_APPLICATION", getName());

		// Source index name

		String sourceIndexName =
			_elasticsearchConfiguration.indexNamePrefix() +
				commerceDataIntegrationProcess.getCompanyId();

		contextProperties.put("LIFERAY_INDEX_NAME", sourceIndexName);

		UnicodeProperties typeSettingsProperties =
			commerceDataIntegrationProcess.getTypeSettingsProperties();

		StringBuilder sb = new StringBuilder(11);

		sb.append("--level");
		sb.append(StringPool.SPACE);
		sb.append(typeSettingsProperties.getProperty("level"));
		sb.append(StringPool.SPACE);
		sb.append("--period");
		sb.append(StringPool.SPACE);
		sb.append(typeSettingsProperties.getProperty("period"));
		sb.append(StringPool.SPACE);
		sb.append("--target");
		sb.append(StringPool.SPACE);
		sb.append(typeSettingsProperties.getProperty("target"));

		contextProperties.put("spark.launcher.args", sb.toString());

		return contextProperties;
	}

	@Reference
	private CommerceDataIntegrationProcessLocalService
		_commerceDataIntegrationProcessLocalService;

	@Reference
	private CommerceMachineLearningScheduledTaskExecutorService
		_commerceMachineLearningScheduledTaskExecutorService;

	private ElasticsearchConfiguration _elasticsearchConfiguration;

}