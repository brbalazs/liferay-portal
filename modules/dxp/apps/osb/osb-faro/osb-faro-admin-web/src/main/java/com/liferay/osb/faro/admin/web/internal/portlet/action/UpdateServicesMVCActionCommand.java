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

package com.liferay.osb.faro.admin.web.internal.portlet.action;

import com.liferay.osb.faro.admin.web.internal.constants.FaroAdminPortletKeys;
import com.liferay.osb.faro.engine.client.WorkspaceEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + FaroAdminPortletKeys.FARO_ADMIN,
		"mvc.command.name=/faro_admin/update_services"
	},
	service = MVCActionCommand.class
)
public class UpdateServicesMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (!permissionChecker.isOmniadmin()) {
			return;
		}

		long faroProjectId = ParamUtil.getLong(actionRequest, "faroProjectId");
		String operation = ParamUtil.getString(actionRequest, "operation");

		if (faroProjectId > 0) {
			FaroProject faroProject = _faroProjectLocalService.getFaroProject(
				faroProjectId);

			if (!faroProject.isSharedCluster()) {
				_workspaceEngineClient.updateServices(
					faroProject.getWeDeployKey(), operation);
			}
		}
		else {
			for (FaroProject faroProject :
					_faroProjectLocalService.getFaroProjects(
						QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

				if (!faroProject.isSharedCluster()) {
					_workspaceEngineClient.updateServices(
						faroProject.getWeDeployKey(), operation);
				}
			}
		}
	}

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference
	private WorkspaceEngineClient _workspaceEngineClient;

}