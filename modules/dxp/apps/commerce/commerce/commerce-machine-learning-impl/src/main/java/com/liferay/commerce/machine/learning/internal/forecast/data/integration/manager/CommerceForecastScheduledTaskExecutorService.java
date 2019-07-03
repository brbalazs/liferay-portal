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

import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.ProcessLocalService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskExectutorService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalService;
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
	service = ScheduledTaskExectutorService.class
)
public class CommerceForecastScheduledTaskExecutorService
	implements ScheduledTaskExectutorService {

	public static final String NAME = "forecast-service";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void runProcess(
			long userId, long scheduledTaskId, String executionType)
		throws IOException, PortalException {

		ScheduledTask scheduledTask =
			_scheduledTaskLocalService.getScheduledTask(scheduledTaskId);

		Map<String, String> contextProperties = getContextProperties(
			scheduledTask.getCompanyId(), scheduledTask.getProcessId());

		_commerceMachineLearningScheduledTaskExecutorService.
			executeScheduledTask(
				userId, scheduledTaskId, executionType, contextProperties);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);
	}

	protected Map<String, String> getContextProperties(
			long companyId, long processId)
		throws PortalException {

		Map<String, String> contextProperties = new HashMap<>();

		// Forecast Application

		contextProperties.put("LIFERAY_RECOMMEND_APPLICATION", getName());

		// Source index name

		String sourceIndexName =
			_elasticsearchConfiguration.indexNamePrefix() + companyId;

		contextProperties.put("LIFERAY_INDEX_NAME", sourceIndexName);

		Process process = _processLocalService.getProcess(processId);

		String processContextProperties = process.getContextProperties();

		UnicodeProperties contextPropertiesUnicode = new UnicodeProperties(
			true);

		contextPropertiesUnicode.fastLoad(processContextProperties);

		StringBuilder sb = new StringBuilder(11);

		sb.append("--level");
		sb.append(StringPool.SPACE);
		sb.append(contextPropertiesUnicode.getProperty("level"));
		sb.append(StringPool.SPACE);
		sb.append("--period");
		sb.append(StringPool.SPACE);
		sb.append(contextPropertiesUnicode.getProperty("period"));
		sb.append(StringPool.SPACE);
		sb.append("--target");
		sb.append(StringPool.SPACE);
		sb.append(contextPropertiesUnicode.getProperty("target"));

		contextProperties.put("spark.launcher.args", sb.toString());

		return contextProperties;
	}

	@Reference
	private CommerceMachineLearningScheduledTaskExecutorService
		_commerceMachineLearningScheduledTaskExecutorService;

	private ElasticsearchConfiguration _elasticsearchConfiguration;

	@Reference
	private ProcessLocalService _processLocalService;

	@Reference
	private ScheduledTaskLocalService _scheduledTaskLocalService;

}