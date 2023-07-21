/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mentions.web.internal.editor.configuration;

import com.liferay.mentions.constants.MentionsPortletKeys;
import com.liferay.mentions.matcher.MentionsMatcherUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.portlet.PortletURL;

/**
 * @author Sergio González
 */
public class BaseMentionsEditorConfigContributor
	extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		JSONObject autoCompleteConfigJSONObject =
			JSONFactoryUtil.createJSONObject();

		autoCompleteConfigJSONObject.put("requestTemplate", "query={query}");

		JSONArray triggerJSONArray = JSONFactoryUtil.createJSONArray();

		JSONObject triggerJSONObject = JSONFactoryUtil.createJSONObject();

		triggerJSONObject.put(
			"regExp",
			"(?:\\strigger|^trigger)(" +
				MentionsMatcherUtil.getScreenNameRegularExpression() + ")");
		triggerJSONObject.put(
			"resultFilters", "function(query, results) {return results;}");
		triggerJSONObject.put("resultTextLocator", "screenName");

		PortletURL autoCompleteUserURL =
			requestBackedPortletURLFactory.createResourceURL(
				MentionsPortletKeys.MENTIONS);

		String source =
			autoCompleteUserURL.toString() + "&" +
				PortalUtil.getPortletNamespace(MentionsPortletKeys.MENTIONS);

		triggerJSONObject.put("source", source);

		triggerJSONObject.put("term", "@");
		triggerJSONObject.put("tplReplace", "{mention}");

		String tplResults = StringBundler.concat(
			"<div class=\"p-1 autofit-row autofit-row-center\">",
			"<div class=\"autofit-col inline-item-before\">{portraitHTML}",
			"</div><div class=\"autofit-col autofit-col-expand\">",
			"<strong class=\"truncate-text\">{fullName}</strong>",
			"<div class=\"autofit-col-expand\">",
			"<small class=\"truncate-text\">@{screenName}</small></div></div>",
			"</div>");

		triggerJSONObject.put("tplResults", tplResults);

		triggerJSONArray.put(triggerJSONObject);

		autoCompleteConfigJSONObject.put("trigger", triggerJSONArray);

		jsonObject.put("autocomplete", autoCompleteConfigJSONObject);

		String extraPlugins = jsonObject.getString("extraPlugins");

		if (Validator.isNotNull(extraPlugins)) {
			extraPlugins += ",autocomplete";
		}
		else {
			extraPlugins =
				"autocomplete,ae_placeholder,ae_selectionregion,ae_uicore";
		}

		jsonObject.put("extraPlugins", extraPlugins);
	}

}