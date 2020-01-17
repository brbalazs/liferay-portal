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

package com.liferay.commerce.frontend.internal.util;

import com.liferay.commerce.frontend.util.HeaderHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

/**
 * @author Alec Sloan
 */
@Component(service = HeaderHelper.class)
public class HeaderHelperImpl implements HeaderHelper {

	public WorkflowTask getReviewWorkflowTask(
			long companyId, long userId, long beanId, String className)
		throws PortalException {

		List<WorkflowTask> workflowTasks = _workflowTaskManager.search(
			companyId, userId, "review", className, new Long[] {beanId}, null,
			null, false, null, false, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);

		if (workflowTasks.size() == 1) {
			return workflowTasks.get(0);
		}

		return null;
	}

	@Reference
	private WorkflowTaskManager _workflowTaskManager;

}