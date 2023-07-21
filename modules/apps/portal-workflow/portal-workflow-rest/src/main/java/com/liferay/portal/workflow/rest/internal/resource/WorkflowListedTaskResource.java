/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.rest.internal.resource;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskManager;
import com.liferay.portal.workflow.rest.internal.helper.WorkflowHelper;
import com.liferay.portal.workflow.rest.internal.model.WorkflowTaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adam Brandizzi
 */
@Component(immediate = true, service = WorkflowListedTaskResource.class)
@Path("/tasks")
public class WorkflowListedTaskResource {

	@GET
	@Produces("application/json")
	public List<WorkflowTaskModel> getUserWorkflowTaskHeaders(
			@Context Company company, @Context User user,
			@Context Locale locale)
		throws PortalException {

		List<WorkflowTaskModel> workflowTaskModels = new ArrayList<>();

		List<WorkflowTask> userWorkflowTasks =
			_workflowTaskManager.getWorkflowTasksByUser(
				user.getCompanyId(), user.getUserId(), null, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		populateWorkflowTaskModels(
			company, user, locale, userWorkflowTasks, workflowTaskModels);

		List<WorkflowTask> roleWorkflowTasks =
			_workflowTaskManager.getWorkflowTasksByUserRoles(
				user.getCompanyId(), user.getUserId(), null, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		populateWorkflowTaskModels(
			company, user, locale, roleWorkflowTasks, workflowTaskModels);

		return workflowTaskModels;
	}

	protected void populateWorkflowTaskModels(
			Company company, User user, Locale locale,
			List<WorkflowTask> userWorkflowTasks,
			List<WorkflowTaskModel> workflowTaskModels)
		throws PortalException {

		for (WorkflowTask workflowTask : userWorkflowTasks) {
			WorkflowTaskModel workflowListedTaskModel =
				_workflowHelper.getWorkflowTaskModel(
					company.getCompanyId(), user.getUserId(),
					workflowTask.getWorkflowTaskId(), locale);

			workflowTaskModels.add(workflowListedTaskModel);
		}
	}

	@Reference
	private WorkflowHelper _workflowHelper;

	@Reference
	private WorkflowTaskManager _workflowTaskManager;

}