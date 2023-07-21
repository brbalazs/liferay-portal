/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.display.UADDisplay;
import com.liferay.user.associated.data.web.internal.constants.UADWebKeys;
import com.liferay.user.associated.data.web.internal.display.UADEntity;
import com.liferay.user.associated.data.web.internal.registry.UADRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/info_panel"
	},
	service = MVCResourceCommand.class
)
public class InfoPanelMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		String[] rowIds = ParamUtil.getStringValues(resourceRequest, "rowIds");

		List<UADEntity> uadEntities = new ArrayList<>();

		String uadRegistryKey = ParamUtil.getString(
			resourceRequest, "uadRegistryKey");

		UADDisplay uadDisplay = _uadRegistry.getUADDisplay(uadRegistryKey);

		resourceRequest.setAttribute(
			UADWebKeys.INFO_PANEL_UAD_DISPLAY, uadDisplay);

		for (String rowId : rowIds) {
			Object entity = uadDisplay.get(rowId);

			UADEntity uadEntity = new UADEntity(
				entity, uadDisplay.getPrimaryKey(entity), null);

			uadEntities.add(uadEntity);
		}

		resourceRequest.setAttribute(
			UADWebKeys.INFO_PANEL_UAD_ENTITIES, uadEntities);

		include(resourceRequest, resourceResponse, "/info_panel.jsp");
	}

	@Reference
	private UADRegistry _uadRegistry;

}