/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.wiki.editor.configuration.internal;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.wiki.constants.WikiPortletKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ambrín Chaudhary
 */
@Component(
	property = {
		"editor.config.key=contentEditor", "editor.name=alloyeditor_creole",
		"editor.name=ckeditor_creole",
		"javax.portlet.name=" + WikiPortletKeys.WIKI,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_ADMIN,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_DISPLAY,
		"service.ranking:Integer=100"
	},
	service = EditorConfigContributor.class
)
public class WikiAttachmentImageCreoleEditorConfigContributor
	extends BaseWikiAttachmentImageEditorConfigContributor {

	@Override
	protected String getItemSelectorURL(
		RequestBackedPortletURLFactory requestBackedPortletURLFactory,
		String itemSelectedEventName, long wikiPageResourcePrimKey,
		ThemeDisplay themeDisplay) {

		ItemSelectorCriterion urlItemSelectorCriterion =
			getURLItemSelectorCriterion();

		PortletURL itemSelectorURL = null;

		if (wikiPageResourcePrimKey == 0) {
			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, itemSelectedEventName,
				urlItemSelectorCriterion);
		}
		else {
			List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
				new ArrayList<>();

			desiredItemSelectorReturnTypes.add(
				new FileEntryItemSelectorReturnType());

			ItemSelectorCriterion attachmentItemSelectorCriterion =
				getWikiAttachmentItemSelectorCriterion(
					wikiPageResourcePrimKey, desiredItemSelectorReturnTypes);

			ItemSelectorCriterion uploadItemSelectorCriterion =
				getUploadItemSelectorCriterion(
					wikiPageResourcePrimKey, themeDisplay,
					requestBackedPortletURLFactory);

			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, itemSelectedEventName,
				attachmentItemSelectorCriterion, urlItemSelectorCriterion,
				uploadItemSelectorCriterion);
		}

		return itemSelectorURL.toString();
	}

	@Reference(unbind = "-")
	protected void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	private ItemSelector _itemSelector;

}