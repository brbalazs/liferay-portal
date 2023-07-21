/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.admin.web.internal.handler.LayoutPageTemplateEntryExceptionRequestHandler;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.layout.util.GroupControlPanelLayoutUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout/add_layout_page_template_entry"
	},
	service = MVCActionCommand.class
)
public class AddLayoutPageTemplateEntryMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long layoutPageTemplateCollectionId = ParamUtil.getLong(
			actionRequest, "layoutPageTemplateCollectionId");

		String name = ParamUtil.getString(actionRequest, "name");

		int type = ParamUtil.getInteger(
			actionRequest, "type",
			LayoutPageTemplateEntryTypeConstants.TYPE_BASIC);

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryService.addLayoutPageTemplateEntry(
					serviceContext.getScopeGroupId(),
					layoutPageTemplateCollectionId, name, type,
					WorkflowConstants.STATUS_DRAFT, serviceContext);

			if (SessionErrors.contains(
					actionRequest, "layoutPageTemplateEntryNameInvalid")) {

				addSuccessMessage(actionRequest, actionResponse);
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put(
				"redirectURL",
				getRedirectURL(
					actionRequest, actionResponse, layoutPageTemplateEntry));

			JSONPortletResponseUtil.writeJSON(
				actionRequest, actionResponse, jsonObject);
		}
		catch (PortalException pe) {
			SessionErrors.add(
				actionRequest, "layoutPageTemplateEntryNameInvalid");

			hideDefaultErrorMessage(actionRequest);

			_layoutPageTemplateEntryExceptionRequestHandler.
				handlePortalException(actionRequest, actionResponse, pe);
		}
	}

	protected String getRedirectURL(
			ActionRequest actionRequest, ActionResponse actionResponse,
			LayoutPageTemplateEntry layoutPageTemplateEntry)
		throws PortalException {

		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(actionResponse);

		Group group = _groupLocalService.getGroup(
			layoutPageTemplateEntry.getGroupId());

		long groupControlPanelPlid =
			GroupControlPanelLayoutUtil.getGroupControlPanelPlid(group);

		PortletURL portletURL = liferayPortletResponse.createRenderURL(
			groupControlPanelPlid);

		portletURL.setParameter(
			"mvcRenderCommandName", "/layout/edit_layout_page_template_entry");

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		portletURL.setParameter("redirect", redirect);

		portletURL.setParameter(
			"layoutPageTemplateCollectionId",
			String.valueOf(
				layoutPageTemplateEntry.getLayoutPageTemplateCollectionId()));
		portletURL.setParameter(
			"layoutPageTemplateEntryId",
			String.valueOf(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));

		return portletURL.toString();
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutPageTemplateEntryExceptionRequestHandler
		_layoutPageTemplateEntryExceptionRequestHandler;

	@Reference
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Reference
	private Portal _portal;

}