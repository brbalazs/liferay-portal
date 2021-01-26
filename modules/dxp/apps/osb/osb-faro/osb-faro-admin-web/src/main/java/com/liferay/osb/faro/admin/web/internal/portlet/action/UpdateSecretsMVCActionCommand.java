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
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + FaroAdminPortletKeys.FARO_ADMIN,
		"mvc.command.name=/faro_admin/update_secrets"
	},
	service = MVCActionCommand.class
)
public class UpdateSecretsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (!permissionChecker.isOmniadmin() ||
			Validator.isNull(_ELASTICSEARCH_PASSWORD) ||
			Validator.isNull(_ELASTICSEARCH_USER)) {

			return;
		}

		long faroProjectId = ParamUtil.getLong(actionRequest, "faroProjectId");

		if (faroProjectId > 0) {
			_updateSecrets(
				_faroProjectLocalService.getFaroProject(faroProjectId));
		}
		else {
			for (FaroProject faroProject :
					_faroProjectLocalService.getFaroProjects(
						QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

				_updateSecrets(faroProject);
			}
		}
	}

	private void _updateSecrets(FaroProject faroProject) throws Exception {
		if (_workspaceEngineClient.hasSecret(
				faroProject.getWeDeployKey(), "elasticsearchpassword")) {

			_workspaceEngineClient.updateSecret(
				faroProject.getWeDeployKey(), "elasticsearchpassword",
				_ELASTICSEARCH_PASSWORD);
			_workspaceEngineClient.updateSecret(
				faroProject.getWeDeployKey(), "elasticsearchuser",
				_ELASTICSEARCH_USER);
		}
		else {
			_workspaceEngineClient.createElasticsearchSecrets(
				faroProject.getWeDeployKey());

			_workspaceEngineClient.attachElasticsearchSecrets(
				faroProject.getWeDeployKey());
		}
	}

	private static final String _ELASTICSEARCH_PASSWORD = System.getenv(
		"ELASTICSEARCH_PASSWORD");

	private static final String _ELASTICSEARCH_USER = System.getenv(
		"ELASTICSEARCH_USER");

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile WorkspaceEngineClient _workspaceEngineClient;

}