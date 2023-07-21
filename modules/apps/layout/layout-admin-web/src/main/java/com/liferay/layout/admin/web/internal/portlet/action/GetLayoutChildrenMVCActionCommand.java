/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.admin.web.internal.display.context.LayoutsAdminDisplayContext;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout/get_layout_children"
	},
	service = MVCActionCommand.class
)
public class GetLayoutChildrenMVCActionCommand
	extends BaseAddLayoutMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long plid = ParamUtil.getLong(actionRequest, "plid");

		writeChildLayoutsAsJSON(actionRequest, actionResponse, plid);
	}

	@Reference(unbind = "-")
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	@Reference(unbind = "-")
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portal = portal;
	}

	protected void writeChildLayoutsAsJSON(
			ActionRequest actionRequest, ActionResponse actionResponse,
			long plid)
		throws Exception {

		Layout layout = _layoutLocalService.fetchLayout(plid);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		JSONArray jsonArray = null;

		if (layout != null) {
			LayoutsAdminDisplayContext layoutsAdminDisplayContext =
				new LayoutsAdminDisplayContext(
					_portal.getLiferayPortletRequest(actionRequest),
					_portal.getLiferayPortletResponse(actionResponse));

			jsonArray = layoutsAdminDisplayContext.getLayoutsJSONArray(
				layout.getLayoutId(), layout.isPrivateLayout());
		}
		else {
			jsonArray = _jsonFactory.createJSONArray();
		}

		jsonObject.put("children", jsonArray);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private JSONFactory _jsonFactory;
	private LayoutLocalService _layoutLocalService;
	private Portal _portal;

}