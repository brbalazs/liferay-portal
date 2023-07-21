/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.context;

import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Miguel Pastor
 */
public class ServiceBeanPublisher
	implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof ContextClosedEvent) {
			ContextClosedEvent contextClosedEvent =
				(ContextClosedEvent)applicationEvent;

			ModuleFrameworkUtilAdapter.unregisterContext(
				contextClosedEvent.getApplicationContext());
		}
		else if (applicationEvent instanceof ContextRefreshedEvent) {
			ContextRefreshedEvent contextRefreshedEvent =
				(ContextRefreshedEvent)applicationEvent;

			ModuleFrameworkUtilAdapter.registerContext(
				contextRefreshedEvent.getApplicationContext());
		}
	}

}