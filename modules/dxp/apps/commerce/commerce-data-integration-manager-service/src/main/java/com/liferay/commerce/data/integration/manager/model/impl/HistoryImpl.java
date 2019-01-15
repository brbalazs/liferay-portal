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
import com.liferay.portal.kernel.exception.PortalException;

/**
 * The extended model implementation for the History service. Represents a row in the &quot;History&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link History} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class HistoryImpl extends HistoryBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a history model instance should use the {@link History} interface instead.
	 */
	public HistoryImpl() {
	}

	public ScheduledTask getScheduledTask() {
		if (_scheduledTask == null) {
			try {
				_scheduledTask = ScheduledTaskLocalServiceUtil.getScheduledTask(
					getScheduledTaskId());
			}
			catch (PortalException pe) {
				pe.printStackTrace();
			}
		}

		return _scheduledTask;
	}

	public String getScheduledTaskName() {
		ScheduledTask scheduledTask = getScheduledTask();

		if (scheduledTask != null) {
			return scheduledTask.getName();
		}

		return "";
	}

	private ScheduledTask _scheduledTask;

}