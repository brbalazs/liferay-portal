/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.AssetDisplayContributorTracker;
import com.liferay.asset.display.contributor.AssetDisplayField;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Set;

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
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout/get_mapping_fields"
	},
	service = MVCActionCommand.class
)
public class GetMappingFieldsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long classNameId = ParamUtil.getLong(actionRequest, "classNameId");

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		AssetDisplayContributor assetDisplayContributor =
			_assetDisplayContributorTracker.getAssetDisplayContributor(
				_portal.getClassName(classNameId));

		if (assetDisplayContributor != null) {
			long classTypeId = ParamUtil.getLong(actionRequest, "classTypeId");
			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			Set<AssetDisplayField> assetEntryFields =
				assetDisplayContributor.getAssetDisplayFields(
					classTypeId, themeDisplay.getLocale());

			for (AssetDisplayField assetEntryField : assetEntryFields) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("key", assetEntryField.getKey());
				jsonObject.put("label", assetEntryField.getLabel());
				jsonObject.put("type", assetEntryField.getType());

				jsonArray.put(jsonObject);
			}
		}

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonArray);
	}

	@Reference
	private AssetDisplayContributorTracker _assetDisplayContributorTracker;

	@Reference
	private Portal _portal;

}