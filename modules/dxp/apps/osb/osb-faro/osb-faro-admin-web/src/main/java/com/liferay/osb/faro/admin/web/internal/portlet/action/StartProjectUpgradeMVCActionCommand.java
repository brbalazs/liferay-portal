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
import com.liferay.osb.faro.admin.web.internal.portlet.UpgradeExecutor;
import com.liferay.osb.faro.constants.UpgradeConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PrefsPropsUtil;

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
		"mvc.command.name=/faro_admin/start_project_upgrade"
	},
	service = MVCActionCommand.class
)
public class StartProjectUpgradeMVCActionCommand extends BaseMVCActionCommand {

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
		String version = ParamUtil.getString(
			actionRequest, "version",
			PrefsPropsUtil.getString(
				_portal.getCompanyId(actionRequest),
				UpgradeConstants.REPOSITORY_SHA));
		boolean refreshLiferay = ParamUtil.getBoolean(
			actionRequest, "refreshLiferay");
		boolean waitForHealthy = ParamUtil.getBoolean(
			actionRequest, "waitForHealthy", true);

		if (faroProjectId > 0) {
			_upgradeExecutor.upgrade(
				faroProjectId, version, refreshLiferay, waitForHealthy);
		}
		else {
			int threadCount = ParamUtil.getInteger(
				actionRequest, "threadCount",
				PrefsPropsUtil.getInteger(
					_portal.getCompanyId(actionRequest),
					UpgradeConstants.UPGRADE_THREAD_COUNT));

			_upgradeExecutor.upgrade(
				version, refreshLiferay, threadCount, waitForHealthy);
		}
	}

	@Reference
	private Portal _portal;

	@Reference
	private UpgradeExecutor _upgradeExecutor;

}