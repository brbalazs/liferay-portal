/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.type.controller.content.internal.portlet.action;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.type.controller.content.internal.constants.ContentLayoutPortletKeys;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentLayoutPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/content_layout/update_fragment_entry_links"
	},
	service = MVCActionCommand.class
)
public class UpdateFragmentEntryLinksMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long fragmentEntryLinkId1 = ParamUtil.getLong(
			actionRequest, "fragmentEntryLinkId1");

		FragmentEntryLink fragmentEntryLink1 =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentEntryLinkId1);

		long fragmentEntryLinkId2 = ParamUtil.getLong(
			actionRequest, "fragmentEntryLinkId2");

		FragmentEntryLink fragmentEntryLink2 =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentEntryLinkId2);

		if ((fragmentEntryLink1 != null) && (fragmentEntryLink2 != null)) {
			_fragmentEntryLinkLocalService.updateFragmentEntryLink(
				fragmentEntryLinkId1, fragmentEntryLink2.getPosition());

			_fragmentEntryLinkLocalService.updateFragmentEntryLink(
				fragmentEntryLinkId2, fragmentEntryLink1.getPosition());
		}

		hideDefaultSuccessMessage(actionRequest);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, JSONFactoryUtil.createJSONObject());
	}

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

}