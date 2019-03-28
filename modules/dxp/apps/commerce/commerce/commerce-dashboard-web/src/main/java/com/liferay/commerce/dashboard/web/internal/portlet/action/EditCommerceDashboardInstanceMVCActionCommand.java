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

package com.liferay.commerce.dashboard.web.internal.portlet.action;

import com.liferay.commerce.dashboard.web.internal.constants.CommerceDashboardPortletKeys;
import com.liferay.commerce.dashboard.web.internal.util.CommerceDashboardUtil;
import com.liferay.commerce.product.exception.NoSuchCPInstanceException;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CommerceDashboardPortletKeys.COMMERCE_DASHBOARD_INSTANCE_SELECTOR,
		"mvc.command.name=editCommerceDashboardInstance"
	},
	service = MVCActionCommand.class
)
public class EditCommerceDashboardInstanceMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		long cpInstanceId = ParamUtil.getLong(actionRequest, "cpInstanceId");

		Map<Long, Boolean> cpInstanceIds = CommerceDashboardUtil.getSessionMap(
			actionRequest, "cpInstanceIds");

		if (cpInstanceIds.isEmpty()) {
			cpInstanceIds = new LinkedHashMap<>();
		}

		try {
			_cpInstanceService.getCPInstance(cpInstanceId);
		}
		catch (PortalException pe) {
			if (pe instanceof NoSuchCPInstanceException ||
				pe instanceof PrincipalException) {

				cmd = Constants.REMOVE;
			}
			else {
				throw pe;
			}
		}

		if (cmd.equals(Constants.ADD) || cmd.equals(Constants.VIEW)) {
			cpInstanceIds.put(cpInstanceId, true);
		}
		else if (cmd.equals(Constants.DEACTIVATE)) {
			cpInstanceIds.put(cpInstanceId, false);
		}
		else if (cmd.equals(Constants.REMOVE)) {
			cpInstanceIds.remove(cpInstanceId);
		}

		CommerceDashboardUtil.setSessionValue(
			actionRequest, "cpInstanceIds", cpInstanceIds);

		hideDefaultSuccessMessage(actionRequest);
	}

	@Reference
	private CPInstanceService _cpInstanceService;

}