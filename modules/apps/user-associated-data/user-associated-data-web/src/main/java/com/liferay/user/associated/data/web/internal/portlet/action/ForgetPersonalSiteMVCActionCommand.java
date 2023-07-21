/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.RequiredUserException;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseTransactionalMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.web.internal.util.SelectedUserHelper;

import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author William Newbury
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/forget_personal_site"
	},
	service = MVCActionCommand.class
)
public class ForgetPersonalSiteMVCActionCommand
	extends BaseTransactionalMVCActionCommand {

	@Override
	protected void doTransactionalCommand(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		User selectedUser = _selectedUserHelper.getSelectedUser(actionRequest);

		if (selectedUser.isDefaultUser()) {
			throw new RequiredUserException();
		}

		_groupLocalService.deleteGroup(selectedUser.getGroup());

		_groupLocalService.addGroup(
			selectedUser.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
			User.class.getName(), selectedUser.getUserId(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID, (Map<Locale, String>)null,
			null, 0, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION,
			StringPool.SLASH + selectedUser.getScreenName(), false, true, null);
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private SelectedUserHelper _selectedUserHelper;

}