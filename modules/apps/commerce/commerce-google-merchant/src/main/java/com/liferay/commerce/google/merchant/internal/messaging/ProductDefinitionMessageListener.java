/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.google.merchant.internal.messaging;

import com.liferay.commerce.google.merchant.internal.configuration.ProductDefinitionConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.quartz.CronExpression;

import java.util.Map;

/**
 * @author Eric Chin
 */
@Component(
	configurationPid = "com.liferay.commerce.google.merchant.internal.configuration.ProductDefinitionConfiguration",
	immediate = true, service = ProductDefinitionMessageListener.class
)
public class ProductDefinitionMessageListener extends BaseMessageListener {

	@Activate
	protected void activate(Map<String, Object> properties) {
		Class<?> clazz = getClass();

		String className = clazz.getName();

		ProductDefinitionConfiguration productDefinitionConfiguration =
			ConfigurableUtil.createConfigurable(
				ProductDefinitionConfiguration.class, properties);

		TimeUnit timeUnit = TimeUnit.MINUTE;

		try {
			timeUnit = Enum.valueOf(
				TimeUnit.class,
				productDefinitionConfiguration.checkIntervalUnit());
		}
		catch (IllegalArgumentException iae) {
			_log.error(iae, iae);
		}

		String cronExpression = productDefinitionConfiguration.cronExpression();
		Trigger trigger;

		if (Validator.isNotNull(cronExpression) &&
				CronExpression.isValidExpression(cronExpression)) {

			trigger = _triggerFactory.createTrigger(
				className, className, null, null, cronExpression);
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug("Invalid cron expression");
			}

			trigger = _triggerFactory.createTrigger(
				className, className, null, null,
				productDefinitionConfiguration.checkInterval(), timeUnit);
		}

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(
			className, trigger);

		_schedulerEngineHelper.register(
			this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug(
				"Running " + ProductDefinitionMessageListener.class.getName());
		}

		// TODO: start generating XML definition or fire off background task
		// TODO: to handle the XML generation and storage
		// TODO: dependent on COMMERCE-2683
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

	private static final Log _log = LogFactoryUtil.getLog(
		ProductDefinitionMessageListener.class);

}
