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

import com.liferay.commerce.data.integration.manager.model.History;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.HistoryLocalService;
import com.liferay.commerce.data.integration.manager.service.ProcessService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskExectutorService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalService;
import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;
import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration;

import java.io.IOException;

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
	configurationPid = "com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = "data.integration.service.executor.key=" + CommerceContentRecommendScheduledTaskExecutorService.NAME,
	service = ScheduledTaskExectutorService.class
)
public class CommerceContentRecommendScheduledTaskExecutorService
	implements ScheduledTaskExectutorService {

	public static final String NAME = "content-recommend-service";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void runProcess(
			long userId, long scheduledTaskId, String executionType)
		throws IOException, PortalException {

		ScheduledTask scheduledTask =
			_scheduledTaskLocalService.startScheduledTask(
				userId, scheduledTaskId);

		History history = _historyLocalService.addHistory(
			userId, scheduledTaskId, executionType,
			scheduledTask.getStartDate(), null,
			BackgroundTaskConstants.STATUS_IN_PROGRESS, 0L, 0L);

		try {
			Process process = _processService.getProcess(
				userId, scheduledTask.getProcessId());

			triggerJob(process);

			history.setEndDate(new Date());

			history.setStatus(BackgroundTaskConstants.STATUS_SUCCESSFUL);

			_historyLocalService.updateHistory(history);
		}
		catch (Exception e) {
			_log.error(e, e);

			history.setEndDate(new Date());

			history.setStatus(BackgroundTaskConstants.STATUS_FAILED);

			_historyLocalService.updateHistory(history);
		}

		_scheduledTaskLocalService.stopScheduledTask(userId, scheduledTaskId);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);
	}

	protected JSONObject getContextPropertiesJSONObject(
		long companyId, Map<String, String> contextProperties) {

		Set<Map.Entry<String, String>> contextPropertiesEntrySet =
			contextProperties.entrySet();

		Stream<Map.Entry<String, String>> contextPropertiesStream =
			contextPropertiesEntrySet.stream();

		JSONObject contextPropertiesJSONObject =
			JSONFactoryUtil.createJSONObject();

		contextPropertiesStream.forEach(
			s -> {
				contextPropertiesJSONObject.put(s.getKey(), s.getValue());
			});

		// Source index name

		String indexName =
			_elasticsearchConfiguration.indexNamePrefix() + companyId;

		contextPropertiesJSONObject.put("LIFERAY_INDEX_NAME", indexName);

		// Destination index name

		String targetIndexName = _contentCommerceRecommendIndexer.getIndexName(
			companyId);

		contextPropertiesJSONObject.put(
			"LIFERAY_RECOMMEND_INDEX_NAME", targetIndexName);

		return contextPropertiesJSONObject;
	}

	protected JSONWebServiceClient getJSONWebServiceClient(
		Map<String, String> contextProperties) {

		Dictionary<String, String> properties = new Hashtable<>();

		properties.put("hostName", contextProperties.get("host.name"));
		properties.put("hostPort", contextProperties.get("host.port"));
		properties.put("protocol", contextProperties.get("protocol"));

		ComponentInstance componentInstance =
			_jsonWebServiceClientComponentFactory.newInstance(properties);

		return (JSONWebServiceClient)componentInstance.getInstance();
	}

	protected void triggerJob(Process process) throws Exception {
		String contextPropertiesString = process.getContextProperties();

		UnicodeProperties contextProperties = new UnicodeProperties(true);

		contextProperties.fastLoad(contextPropertiesString);

		JSONObject contextPropertiesJSONObject = getContextPropertiesJSONObject(
			process.getCompanyId(), contextProperties);

		JSONWebServiceClient jsonWebServiceClient = getJSONWebServiceClient(
			contextProperties);

		jsonWebServiceClient.doPostAsJSON(
			_UPDATE_MODEL_URL, contextPropertiesJSONObject.toString());
	}

	private static final String _UPDATE_MODEL_URL = "/recommend/update-model";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceContentRecommendScheduledTaskExecutorService.class);

	@Reference(
		target = "(component.name=com.liferay.commerce.recommend.internal.search.index.ContentCommerceRecommendIndexer)"
	)
	private CommerceRecommendIndexer _contentCommerceRecommendIndexer;

	private ElasticsearchConfiguration _elasticsearchConfiguration;

	@Reference
	private HistoryLocalService _historyLocalService;

	@Reference(target = "(component.factory=JSONWebServiceClient)")
	private ComponentFactory _jsonWebServiceClientComponentFactory;

	@Reference
	private ProcessService _processService;

	@Reference
	private ScheduledTaskLocalService _scheduledTaskLocalService;

}