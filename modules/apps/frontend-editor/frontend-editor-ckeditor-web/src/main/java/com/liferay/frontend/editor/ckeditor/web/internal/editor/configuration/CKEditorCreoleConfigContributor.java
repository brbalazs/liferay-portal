/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.editor.ckeditor.web.internal.editor.configuration;

import com.liferay.frontend.editor.ckeditor.web.internal.constants.CKEditorConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Ambrín Chaudhary
 */
@Component(
	property = "editor.name=ckeditor_creole",
	service = EditorConfigContributor.class
)
public class CKEditorCreoleConfigContributor
	extends BaseCKEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		super.populateConfigJSONObject(
			jsonObject, inputEditorTaglibAttributes, themeDisplay,
			requestBackedPortletURLFactory);

		jsonObject.put(
			"allowedContent",
			"b strong i hr h1 h2 h3 h4 h5 h6 em ul ol li pre table tr th; " +
				"img a[*]");

		Map<String, String> fileBrowserParams =
			(Map<String, String>)inputEditorTaglibAttributes.get(
				CKEditorConstants.ATTRIBUTE_NAMESPACE + ":fileBrowserParams");

		if (fileBrowserParams != null) {
			String attachmentURLPrefix = fileBrowserParams.get(
				"attachmentURLPrefix");

			if (Validator.isNotNull(attachmentURLPrefix)) {
				jsonObject.put("attachmentURLPrefix", attachmentURLPrefix);
			}
		}

		jsonObject.put("decodeLinks", Boolean.TRUE);
		jsonObject.put("disableObjectResizing", Boolean.TRUE);
		jsonObject.put(
			"extraPlugins",
			"a11yhelpbtn,creole,itemselector,lfrpopup,wikilink");
		jsonObject.put(
			"filebrowserWindowFeatures",
			"title=" + LanguageUtil.get(themeDisplay.getLocale(), "browse"));
		jsonObject.put("format_tags", "p;h1;h2;h3;h4;h5;h6;pre");

		StringBundler sb = new StringBundler(4);

		sb.append("bidi,colorbutton,colordialog,div,elementspath,flash,font,");
		sb.append("forms,indentblock,justify,keystrokes,link,maximize,");
		sb.append("newpage,pagebreak,preview,print,save,showblocks,smiley,");
		sb.append("stylescombo,templates,video");

		jsonObject.put("removePlugins", sb.toString());

		jsonObject.put(
			"toolbar_creole",
			getToolbarsCreoleJSONArray(inputEditorTaglibAttributes));
		jsonObject.put(
			"toolbar_phone",
			getToolbarsPhoneJSONArray(inputEditorTaglibAttributes));
		jsonObject.put(
			"toolbar_tablet",
			getToolbarsTabletJSONArray(inputEditorTaglibAttributes));
	}

	protected JSONArray getToolbarsCreoleJSONArray(
		Map<String, Object> inputEditorTaglibAttributes) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put(toJSONArray("['Bold', 'Italic', '-' ,'RemoveFormat']"));
		jsonArray.put(
			toJSONArray(
				"['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent']"));
		jsonArray.put(toJSONArray("['Format']"));
		jsonArray.put(toJSONArray("['Link', 'Unlink']"));
		jsonArray.put(
			toJSONArray(
				"['Table', '-','ImageSelector', '-', 'HorizontalRule', '-', " +
					"'SpecialChar']"));
		jsonArray.put("/");
		jsonArray.put(
			toJSONArray(
				"['Cut', 'Copy', 'Paste', '-', 'PasteText', 'PasteFromWord', " +
					"'-', 'SelectAll', '-', 'Undo', 'Redo']"));
		jsonArray.put(toJSONArray("['Find','Replace']"));

		if (isShowSource(inputEditorTaglibAttributes)) {
			jsonArray.put(toJSONArray("['Source']"));
		}

		jsonArray.put(toJSONArray("['A11YBtn']"));

		return jsonArray;
	}

	protected JSONArray getToolbarsPhoneJSONArray(
		Map<String, Object> inputEditorTaglibAttributes) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put(toJSONArray("['Bold', 'Italic']"));
		jsonArray.put(toJSONArray("['NumberedList', 'BulletedList']"));
		jsonArray.put(toJSONArray("['Link', 'Unlink']"));
		jsonArray.put(toJSONArray("['ImageSelector']"));

		if (isShowSource(inputEditorTaglibAttributes)) {
			jsonArray.put(toJSONArray("['Source']"));
		}

		return jsonArray;
	}

	protected JSONArray getToolbarsTabletJSONArray(
		Map<String, Object> inputEditorTaglibAttributes) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put(toJSONArray("['Bold', 'Italic']"));
		jsonArray.put(
			toJSONArray(
				"['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent']"));
		jsonArray.put(toJSONArray("['Format']"));
		jsonArray.put(toJSONArray("['Link', 'Unlink']"));
		jsonArray.put(toJSONArray("['ImageSelector']"));

		if (isShowSource(inputEditorTaglibAttributes)) {
			jsonArray.put(toJSONArray("['Source']"));
		}

		return jsonArray;
	}

}