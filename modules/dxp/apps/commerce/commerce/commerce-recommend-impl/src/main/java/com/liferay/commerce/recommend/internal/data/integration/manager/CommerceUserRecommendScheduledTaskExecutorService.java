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

import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskExectutorService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalService;
import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
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
	property = "data.integration.service.executor.key=" + CommerceUserRecommendScheduledTaskExecutorService.NAME,
	service = ScheduledTaskExectutorService.class
)
public class CommerceUserRecommendScheduledTaskExecutorService
	implements ScheduledTaskExectutorService {

	public static final String NAME = "user-recommend-service";

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

		_commerceRecommendScheduledTaskExecutorService.executeScheduledTask(
			userId, scheduledTaskId, executionType,
			getContextProperties(scheduledTask.getCompanyId()));
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);
	}

	protected Map<String, String> getContextProperties(long companyId) {
		Map<String, String> contextProperties = new HashMap<>();

		// Company ID

		contextProperties.put("LIFERAY_COMPANY_ID", String.valueOf(companyId));

		// Recommend Application

		contextProperties.put("LIFERAY_RECOMMEND_APPLICATION", getName());

		// Source index name

		String sourceIndexName =
			_elasticsearchConfiguration.indexNamePrefix() + companyId;

		contextProperties.put("LIFERAY_INDEX_NAME", sourceIndexName);

		// Destination index name

		String destinationIndexName = _commerceRecommendIndexer.getIndexName(
			companyId);

		contextProperties.put(
			"LIFERAY_RECOMMEND_INDEX_NAME", destinationIndexName);

		return contextProperties;
	}

	@Reference(
		target = "(component.name=com.liferay.commerce.recommend.internal.search.index.UserCommerceRecommendIndexer)"
	)
	private CommerceRecommendIndexer _commerceRecommendIndexer;

	@Reference
	private CommerceRecommendScheduledTaskExecutorService
		_commerceRecommendScheduledTaskExecutorService;

	private ElasticsearchConfiguration _elasticsearchConfiguration;

	@Reference
	private ScheduledTaskLocalService _scheduledTaskLocalService;

}