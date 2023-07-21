/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.web.internal.constants.UADWebKeys;
import com.liferay.user.associated.data.web.internal.registry.UADRegistry;
import com.liferay.user.associated.data.web.internal.util.SelectedUserHelper;
import com.liferay.user.associated.data.web.internal.util.UADApplicationSummaryHelper;

import java.util.Collection;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author William Newbury
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/view_uad_summary"
	},
	service = MVCRenderCommand.class
)
public class ViewUADSummaryMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			User selectedUser = _selectedUserHelper.getSelectedUser(
				renderRequest);

			renderRequest.setAttribute(
				UADWebKeys.VIEW_UAD_SUMMARY_STEP, _determineStep(selectedUser));
		}
		catch (Exception pe) {
			throw new PortletException(pe);
		}

		return "/view_uad_summary.jsp";
	}

	private int _determineStep(User selectedUser) throws Exception {
		if (selectedUser.isActive()) {
			return 1;
		}

		int selectedUserPageCount =
			selectedUser.getPrivateLayoutsPageCount() +
				selectedUser.getPublicLayoutsPageCount();

		if (selectedUserPageCount > 0) {
			return 2;
		}

		int reviewableUADEntitiesCount =
			_uadApplicationSummaryHelper.getReviewableUADEntitiesCount(
				_uadRegistry.getUADDisplayStream(), selectedUser.getUserId());

		if (reviewableUADEntitiesCount > 0) {
			return 3;
		}

		Collection<UADAnonymizer> uadAnonymizers =
			_uadRegistry.getUADAnonymizers();

		int selectedUserEntityCount = 0;

		for (UADAnonymizer uadAnonymizer : uadAnonymizers) {
			selectedUserEntityCount += uadAnonymizer.count(
				selectedUser.getUserId());
		}

		if (selectedUserEntityCount > 0) {
			return 4;
		}

		return 5;
	}

	@Reference
	private SelectedUserHelper _selectedUserHelper;

	@Reference
	private UADApplicationSummaryHelper _uadApplicationSummaryHelper;

	@Reference
	private UADRegistry _uadRegistry;

}