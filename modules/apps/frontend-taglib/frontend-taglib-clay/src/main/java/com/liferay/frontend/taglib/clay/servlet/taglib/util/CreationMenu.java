/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Carlos Lancha
 */
public class CreationMenu extends HashMap {

	public CreationMenu() {
		put("primaryItems", _primaryDropdownItems);
	}

	public void addDropdownItem(Consumer<DropdownItem> consumer) {
		addPrimaryDropdownItem(consumer);
	}

	public void addFavoriteDropdownItem(Consumer<DropdownItem> consumer) {
		DropdownItem dropdownItem = new DropdownItem();

		consumer.accept(dropdownItem);

		_favoriteDropdownItems.add(dropdownItem);

		put("secondaryItems", _buildSecondaryDropdownItems());
	}

	public void addPrimaryDropdownItem(Consumer<DropdownItem> consumer) {
		DropdownItem dropdownItem = new DropdownItem();

		consumer.accept(dropdownItem);

		_primaryDropdownItems.add(dropdownItem);
	}

	public void addRestDropdownItem(Consumer<DropdownItem> consumer) {
		DropdownItem dropdownItem = new DropdownItem();

		consumer.accept(dropdownItem);

		_restDropdownItems.add(dropdownItem);

		put("secondaryItems", _buildSecondaryDropdownItems());
	}

	public void setCaption(String caption) {
		put("caption", caption);
	}

	public void setHelpText(String helpText) {
		put("helpText", helpText);
	}

	public void setViewMoreURL(String viewMoreURL) {
		put("viewMoreURL", viewMoreURL);
	}

	private List<DropdownItem> _buildSecondaryDropdownItems() {
		DropdownItemList secondaryDropdownItemList = new DropdownItemList();

		if (!_favoriteDropdownItems.isEmpty()) {
			secondaryDropdownItemList.addGroup(
				dropdownGroupItem -> {
					dropdownGroupItem.setDropdownItems(_favoriteDropdownItems);
					dropdownGroupItem.setLabel(
						LanguageUtil.get(
							LocaleUtil.getMostRelevantLocale(), "favorites"));

					if (!_restDropdownItems.isEmpty()) {
						dropdownGroupItem.setSeparator(true);
					}
				});
		}

		if (!_restDropdownItems.isEmpty()) {
			secondaryDropdownItemList.addGroup(
				dropdownGroupItem -> dropdownGroupItem.setDropdownItems(
					_restDropdownItems));
		}

		return secondaryDropdownItemList;
	}

	private final List<DropdownItem> _favoriteDropdownItems =
		new DropdownItemList();
	private final List<DropdownItem> _primaryDropdownItems =
		new DropdownItemList();
	private final List<DropdownItem> _restDropdownItems =
		new DropdownItemList();

}