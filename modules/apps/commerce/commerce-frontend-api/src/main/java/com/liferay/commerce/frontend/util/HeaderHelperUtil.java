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

package com.liferay.commerce.frontend.util;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.exception.PortalException;

import com.liferay.portal.kernel.workflow.WorkflowTask;

import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Alec Sloan
 */
public class HeaderHelperUtil {

	public static WorkflowTask getReviewWorkflowTask(
			long companyId, long userId, long beanId, String className)
		throws PortalException {

		HeaderHelper headerHelper = _serviceTracker.getService();

		return headerHelper.getReviewWorkflowTask(
			companyId, userId, beanId, className);
	}

	private static final ServiceTracker<?, HeaderHelper>
		_serviceTracker = ServiceTrackerFactory.open(
			FrameworkUtil.getBundle(HeaderHelperUtil.class),
			HeaderHelper.class);

}