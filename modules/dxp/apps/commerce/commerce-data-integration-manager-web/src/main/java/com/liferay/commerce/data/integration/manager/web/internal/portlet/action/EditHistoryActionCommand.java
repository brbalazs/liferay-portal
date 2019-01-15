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

package com.liferay.commerce.data.integration.manager.web.internal.portlet.action;

import com.liferay.commerce.data.integration.manager.service.HistoryLocalService;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DataIntegrationWebPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	property = {
		"javax.portlet.name=" + DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB,
		"mvc.command.name=editHistory"
	},
	service = MVCActionCommand.class
)
public class EditHistoryActionCommand extends BaseMVCActionCommand {

	protected void deleteHistory(ActionRequest actionRequest) {
		long[] deleteHistoryIds = ParamUtil.getLongValues(
			actionRequest, "deleteHistoryIds");

		try {
			if ((deleteHistoryIds != null) && (deleteHistoryIds.length > 0)) {
				for (long historyId : deleteHistoryIds) {
					_historyLocalService.deleteHistory(historyId);
				}
			}
			else {
				long historyId = ParamUtil.getLong(
					actionRequest, "historyId", 0L);

				_historyLocalService.deleteHistory(historyId);
			}
		}
		catch (PortalException pe) {
			pe.printStackTrace();
			SessionErrors.add(actionRequest, "errorDeletingHistory");
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (Constants.DELETE.equals(cmd)) {
			deleteHistory(actionRequest);
		}
	}

	@Reference
	private HistoryLocalService _historyLocalService;

}