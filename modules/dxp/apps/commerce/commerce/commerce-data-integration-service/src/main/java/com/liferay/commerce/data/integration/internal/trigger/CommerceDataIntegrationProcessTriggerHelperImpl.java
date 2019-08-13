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

package com.liferay.commerce.data.integration.internal.trigger;

import com.liferay.commerce.data.integration.constants.CommerceDataIntegrationConstants;
import com.liferay.commerce.data.integration.trigger.CommerceDataIntegrationProcessTriggerHelper;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	service = CommerceDataIntegrationProcessTriggerHelper.class
)
public class CommerceDataIntegrationProcessTriggerHelperImpl
	implements CommerceDataIntegrationProcessTriggerHelper {

	@Override
	public void addScheduledTask(
			long commerceDataIntegrationProcessId, String cronExpression,
			Date startDate, Date endDate)
		throws SchedulerException {

		deleteScheduledTask(commerceDataIntegrationProcessId);

		String groupName =
			CommerceDataIntegrationConstants.JOB_NAME_PREFIX +
				commerceDataIntegrationProcessId;

		Trigger trigger = _triggerFactory.createTrigger(
			String.valueOf(commerceDataIntegrationProcessId), groupName,
			startDate, endDate, cronExpression);

		Message message = new Message();

		JSONObject payLoad = JSONUtil.put(
			"commerceDataIntegrationProcessId",
			commerceDataIntegrationProcessId);

		message.setPayload(payLoad.toString());

		_schedulerEngineHelper.schedule(
			trigger, StorageType.PERSISTED,
			String.valueOf(commerceDataIntegrationProcessId),
			CommerceDataIntegrationConstants.EXECUTOR_DESTINATION_NAME, message, 1000);
	}

	@Override
	public void deleteScheduledTask(long commerceDataIntegrationProcessId)
		throws SchedulerException {

		String groupName =
			CommerceDataIntegrationConstants.JOB_NAME_PREFIX +
				commerceDataIntegrationProcessId;

		SchedulerResponse scheduledJob = getScheduledJob(
			commerceDataIntegrationProcessId);

		if (scheduledJob != null) {
			_schedulerEngineHelper.delete(
				String.valueOf(commerceDataIntegrationProcessId), groupName,
				StorageType.PERSISTED);
		}
	}

	@Override
	public Date getNextFireTime(long commerceDataIntegrationProcessId) {
		Date nextFireTime = null;

		String groupName =
			CommerceDataIntegrationConstants.JOB_NAME_PREFIX +
				commerceDataIntegrationProcessId;

		try {
			nextFireTime = _schedulerEngineHelper.getNextFireTime(
				String.valueOf(commerceDataIntegrationProcessId), groupName,
				StorageType.PERSISTED);
		}
		catch (SchedulerException se) {
			_log.error(se, se);
		}

		return nextFireTime;
	}

	@Override
	public Date getPreviousFireTime(long commerceDataIntegrationProcessId) {
		Date nextFireTime = null;

		String groupName =
			CommerceDataIntegrationConstants.JOB_NAME_PREFIX +
				commerceDataIntegrationProcessId;

		try {
			nextFireTime = _schedulerEngineHelper.getPreviousFireTime(
				String.valueOf(commerceDataIntegrationProcessId), groupName,
				StorageType.PERSISTED);
		}
		catch (SchedulerException se) {
			_log.error(se, se);
		}

		return nextFireTime;
	}

	@Override
	public SchedulerResponse getScheduledJob(
			long commerceDataIntegrationProcessId)
		throws SchedulerException {

		String groupName =
			CommerceDataIntegrationConstants.JOB_NAME_PREFIX +
				commerceDataIntegrationProcessId;

		return _schedulerEngineHelper.getScheduledJob(
			String.valueOf(commerceDataIntegrationProcessId), groupName,
			StorageType.PERSISTED);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceDataIntegrationProcessTriggerHelperImpl.class);

	@Reference
	private SchedulerEngine _schedulerEngine;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

}