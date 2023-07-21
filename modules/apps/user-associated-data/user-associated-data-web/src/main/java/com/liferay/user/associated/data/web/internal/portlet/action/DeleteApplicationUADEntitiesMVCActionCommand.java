/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.web.internal.util.UADApplicationSummaryHelper;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/delete_application_uad_entities"
	},
	service = MVCActionCommand.class
)
public class DeleteApplicationUADEntitiesMVCActionCommand
	extends BaseUADMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String applicationKey = ParamUtil.getString(
			actionRequest, "applicationKey");

		List<UADAnonymizer> uadAnonymizers =
			_uadApplicationSummaryHelper.getApplicationUADAnonymizers(
				applicationKey);

		for (UADAnonymizer uadAnonymizer : uadAnonymizers) {
			long selectedUserId = getSelectedUserId(actionRequest);

			uadAnonymizer.deleteAll(selectedUserId);
		}
	}

	@Reference
	private UADApplicationSummaryHelper _uadApplicationSummaryHelper;

}