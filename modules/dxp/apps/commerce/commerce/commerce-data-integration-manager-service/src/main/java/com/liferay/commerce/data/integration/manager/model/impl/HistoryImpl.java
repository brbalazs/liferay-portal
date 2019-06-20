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

package com.liferay.commerce.data.integration.manager.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class HistoryImpl extends HistoryBaseImpl {

	public HistoryImpl() {
	}

	public ScheduledTask getScheduledTask() {
		if (_scheduledTask == null) {
			try {
				_scheduledTask = ScheduledTaskLocalServiceUtil.getScheduledTask(
					getScheduledTaskId());
			}
			catch (PortalException pe) {
				_log.error(pe, pe);
			}
		}

		return _scheduledTask;
	}

	public String getScheduledTaskName() {
		ScheduledTask scheduledTask = getScheduledTask();

		if (scheduledTask != null) {
			return scheduledTask.getName();
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(HistoryImpl.class);

	private ScheduledTask _scheduledTask;

}