/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.editor.ckeditor.web.internal.editor.configuration;

import com.liferay.frontend.editor.ckeditor.web.internal.constants.CKEditorConstants;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Map;

/**
 * @author Ambrín Chaudhary
 */
public class BaseCKEditorConfigContributor extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		jsonObject.put("allowedContent", Boolean.TRUE);

		String cssClasses = GetterUtil.getString(
			inputEditorTaglibAttributes.get(
				CKEditorConstants.ATTRIBUTE_NAMESPACE + ":cssClasses"));

		jsonObject.put(
			"bodyClass", "html-editor " + HtmlUtil.escape(cssClasses));

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put(
			HtmlUtil.escape(
				PortalUtil.getStaticResourceURL(
					themeDisplay.getRequest(),
					themeDisplay.getPathThemeCss() + "/clay.css")));
		jsonArray.put(
			HtmlUtil.escape(
				PortalUtil.getStaticResourceURL(
					themeDisplay.getRequest(),
					themeDisplay.getPathThemeCss() + "/main.css")));

		jsonObject.put("contentsCss", jsonArray);

		String contentsLanguageDir = getContentsLanguageDir(
			inputEditorTaglibAttributes);

		jsonObject.put(
			"contentsLangDirection", HtmlUtil.escapeJS(contentsLanguageDir));

		String contentsLanguageId = getContentsLanguageId(
			inputEditorTaglibAttributes);

		contentsLanguageId = contentsLanguageId.replace("iw", "he");
		contentsLanguageId = contentsLanguageId.replace("_", "-");

		jsonObject.put("contentsLanguage", contentsLanguageId);

		jsonObject.put("height", 265);

		String languageId = getLanguageId(themeDisplay);

		languageId = languageId.replace("iw", "he");
		languageId = languageId.replace("_", "-");

		jsonObject.put("language", languageId);

		boolean resizable = GetterUtil.getBoolean(
			(String)inputEditorTaglibAttributes.get(
				CKEditorConstants.ATTRIBUTE_NAMESPACE + ":resizable"));

		if (resizable) {
			jsonObject.put("resize_dir", "vertical");
		}

		jsonObject.put("resize_enabled", resizable);
	}

	protected boolean isShowSource(
		Map<String, Object> inputEditorTaglibAttributes) {

		return GetterUtil.getBoolean(
			inputEditorTaglibAttributes.get(
				CKEditorConstants.ATTRIBUTE_NAMESPACE + ":showSource"));
	}

}