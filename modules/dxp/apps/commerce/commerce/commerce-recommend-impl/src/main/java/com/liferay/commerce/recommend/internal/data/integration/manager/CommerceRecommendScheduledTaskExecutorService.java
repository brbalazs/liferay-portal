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
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalService;
import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	service = CommerceRecommendScheduledTaskExecutorService.class
)
public class CommerceRecommendScheduledTaskExecutorService {

	public void executeScheduledTask(
			long userId, long scheduledTaskId, String executionType,
			Map<String, String> scheduledTaskContext)
		throws PortalException {

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

			executeProcess(process, scheduledTaskContext);

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

	protected void executeProcess(
			Process process,
			Map<String, String> additionalProcessContextProperties)
		throws Exception {

		String contextPropertiesString = process.getContextProperties();

		UnicodeProperties contextProperties = new UnicodeProperties(true);

		contextProperties.fastLoad(contextPropertiesString);

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
			s -> {
				contextPropertiesJSONObject.put(s.getKey(), s.getValue());
			});

		jsonWebServiceClient.doPostAsJSON(
			_UPDATE_MODEL_URL, contextPropertiesJSONObject.toString());
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

	private static final String _UPDATE_MODEL_URL = "/recommend/update-model";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceRecommendScheduledTaskExecutorService.class);

	@Reference
	private HistoryLocalService _historyLocalService;

	@Reference(target = "(component.factory=JSONWebServiceClient)")
	private ComponentFactory _jsonWebServiceClientComponentFactory;

	@Reference
	private ProcessService _processService;

	@Reference
	private ScheduledTaskLocalService _scheduledTaskLocalService;

}