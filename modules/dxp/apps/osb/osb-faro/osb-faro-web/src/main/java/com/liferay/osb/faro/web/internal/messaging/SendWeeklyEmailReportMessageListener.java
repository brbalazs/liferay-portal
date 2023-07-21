/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.messaging;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.Trigger;

import java.util.Date;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Rachael Koestartyo
 */
@Component(
	immediate = true, service = SendWeeklyEmailReportMessageListener.class
)
public class SendWeeklyEmailReportMessageListener
	extends BaseEmailReportMessageListener {

	@Activate
	protected void activate() {
		Class<?> clazz = getClass();

		Trigger trigger = triggerFactory.createTrigger(
			clazz.getName(), clazz.getName(), new Date(), null,
			"0 0 0 ? * MON");

		schedulerEngineHelper.register(
			this, new SchedulerEntryImpl(clazz.getName(), trigger),
			DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		schedulerEngineHelper.unregister(this);
	}

	@Override
	protected String getFrequency() {
		return "weekly";
	}

}