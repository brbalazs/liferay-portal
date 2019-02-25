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

package com.liferay.commerce.data.integration.manager.web.internal.portlet.action;

import com.liferay.commerce.data.integration.manager.model.History;
import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.HistoryLocalService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalService;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DIWebKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(immediate = true, service = ActionHelper.class)
public class ActionHelper {

	public History getHistory(PortletRequest portletRequest)
		throws PortalException {

		History history = (History)portletRequest.getAttribute(
			DIWebKeys.DI_HISTORY);

		if (history != null) {
			return history;
		}

		long historyId = ParamUtil.getLong(portletRequest, "historyId");

		if (historyId > 0) {
			history = _historyLocalService.fetchHistory(historyId);
		}

		if (history != null) {
			portletRequest.setAttribute(DIWebKeys.DI_HISTORY, history);
		}

		return history;
	}

	public ScheduledTask getScheduledTask(PortletRequest portletRequest)
		throws PortalException {

		ScheduledTask scheduledTask =
			(ScheduledTask)portletRequest.getAttribute(
				DIWebKeys.DI_SCHEDULED_TASK);

		if (scheduledTask != null) {
			return scheduledTask;
		}

		long scheduledTaskId = ParamUtil.getLong(
			portletRequest, "scheduledTaskId");

		if (scheduledTaskId > 0) {
			scheduledTask = _scheduledTaskLocalService.fetchScheduledTask(
				scheduledTaskId);
		}

		if (scheduledTask != null) {
			portletRequest.setAttribute(
				DIWebKeys.DI_SCHEDULED_TASK, scheduledTask);
		}

		return scheduledTask;
	}

	@Reference
	private HistoryLocalService _historyLocalService;

	@Reference
	private ScheduledTaskLocalService _scheduledTaskLocalService;

}