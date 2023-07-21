/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.editor.configuration;

import com.liferay.fragment.constants.FragmentPortletKeys;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.url.criterion.URLItemSelectorCriterion;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.item.selector.criterion.LayoutItemSelectorCriterion;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = {
		"editor.config.key=fragmenEntryLinkEditor",
		"javax.portlet.name=" + FragmentPortletKeys.FRAGMENT_DISPLAY,
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES
	},
	service = EditorConfigContributor.class
)
public class FragmentEntryLinkEditorConfigContributor
	extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			requestBackedPortletURLFactory, "_EDITOR_NAME_selectItem",
			getFileItemSelectorCriterion(), getLayoutItemSelectorURL());
		PortletURL imageSelectorURL = _itemSelector.getItemSelectorURL(
			requestBackedPortletURLFactory, "_EDITOR_NAME_selectItem",
			getImageItemSelectorCriterion(), getURLItemSelectorCriterion());

		jsonObject.put("allowedContent", "");

		jsonObject.put("disallowedContent", "br");

		jsonObject.put("documentBrowseLinkUrl", itemSelectorURL.toString());

		jsonObject.put("enterMode", 2);

		jsonObject.put("extraPlugins", getExtraPluginsLists());

		jsonObject.put(
			"filebrowserImageBrowseLinkUrl", imageSelectorURL.toString());
		jsonObject.put(
			"filebrowserImageBrowseUrl", imageSelectorURL.toString());

		jsonObject.put("removePlugins", getRemovePluginsLists());

		jsonObject.put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/lexicon/icons.svg");

		jsonObject.put("toolbars", JSONFactoryUtil.createJSONObject());
	}

	protected String getExtraPluginsLists() {
		return "ae_autolink,ae_dragresize,ae_addimages,ae_imagealignment," +
			"ae_placeholder,ae_selectionregion,ae_tableresize," +
				"ae_tabletools,ae_uicore,itemselector,media,adaptivemedia";
	}

	protected ItemSelectorCriterion getFileItemSelectorCriterion() {
		ItemSelectorCriterion fileItemSelectorCriterion =
			new FileItemSelectorCriterion();

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(new URLItemSelectorReturnType());

		fileItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		return fileItemSelectorCriterion;
	}

	protected ItemSelectorCriterion getImageItemSelectorCriterion() {
		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(
			new FileEntryItemSelectorReturnType());

		ItemSelectorCriterion imageItemSelectorCriterion =
			new ImageItemSelectorCriterion();

		imageItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		return imageItemSelectorCriterion;
	}

	protected ItemSelectorCriterion getLayoutItemSelectorURL() {
		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(new URLItemSelectorReturnType());

		LayoutItemSelectorCriterion layoutItemSelectorCriterion =
			new LayoutItemSelectorCriterion();

		layoutItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);
		layoutItemSelectorCriterion.setShowHiddenPages(true);

		return layoutItemSelectorCriterion;
	}

	protected String getRemovePluginsLists() {
		return "contextmenu,elementspath,floatingspace,image,link,liststyle," +
			"magicline,resize,table,tabletools,toolbar,ae_embed";
	}

	protected ItemSelectorCriterion getURLItemSelectorCriterion() {
		ItemSelectorCriterion urlItemSelectorCriterion =
			new URLItemSelectorCriterion();

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(new URLItemSelectorReturnType());

		urlItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		return urlItemSelectorCriterion;
	}

	@Reference
	private ItemSelector _itemSelector;

}