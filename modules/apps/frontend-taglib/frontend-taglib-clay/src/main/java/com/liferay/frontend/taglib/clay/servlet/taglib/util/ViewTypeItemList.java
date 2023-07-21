/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import javax.portlet.PortletURL;

/**
 * @author Carlos Lancha
 */
public class ViewTypeItemList extends ArrayList<ViewTypeItem> {

	public ViewTypeItemList() {
		_portletURL = null;
		_selectedType = null;
	}

	public ViewTypeItemList(PortletURL portletURL, String selectedType) {
		_portletURL = portletURL;
		_selectedType = selectedType;
	}

	public void add(Consumer<ViewTypeItem> consumer) {
		ViewTypeItem viewTypeItem = new ViewTypeItem();

		consumer.accept(viewTypeItem);

		add(viewTypeItem);
	}

	public ViewTypeItem addCardViewTypeItem() {
		ViewTypeItem viewTypeItem = new ViewTypeItem();

		if (Validator.isNotNull(_selectedType)) {
			viewTypeItem.setActive(Objects.equals(_selectedType, "icon"));
		}

		if (_portletURL != null) {
			viewTypeItem.setHref(_portletURL, "displayStyle", "icon");
		}

		viewTypeItem.setIcon("cards2");
		viewTypeItem.setLabel(
			LanguageUtil.get(LocaleUtil.getMostRelevantLocale(), "cards"));

		add(viewTypeItem);

		return viewTypeItem;
	}

	public void addCardViewTypeItem(Consumer<ViewTypeItem> consumer) {
		consumer.accept(addCardViewTypeItem());
	}

	public ViewTypeItem addListViewTypeItem() {
		ViewTypeItem viewTypeItem = new ViewTypeItem();

		if (Validator.isNotNull(_selectedType)) {
			viewTypeItem.setActive(
				Objects.equals(_selectedType, "descriptive"));
		}

		if (_portletURL != null) {
			viewTypeItem.setHref(_portletURL, "displayStyle", "descriptive");
		}

		viewTypeItem.setIcon("list");
		viewTypeItem.setLabel(
			LanguageUtil.get(LocaleUtil.getMostRelevantLocale(), "list"));

		add(viewTypeItem);

		return viewTypeItem;
	}

	public void addListViewTypeItem(Consumer<ViewTypeItem> consumer) {
		consumer.accept(addListViewTypeItem());
	}

	public ViewTypeItem addTableViewTypeItem() {
		ViewTypeItem viewTypeItem = new ViewTypeItem();

		if (Validator.isNotNull(_selectedType)) {
			viewTypeItem.setActive(Objects.equals(_selectedType, "list"));
		}

		if (_portletURL != null) {
			viewTypeItem.setHref(_portletURL, "displayStyle", "list");
		}

		viewTypeItem.setIcon("table");
		viewTypeItem.setLabel(
			LanguageUtil.get(LocaleUtil.getMostRelevantLocale(), "table"));

		add(viewTypeItem);

		return viewTypeItem;
	}

	public void addTableViewTypeItem(Consumer<ViewTypeItem> consumer) {
		consumer.accept(addTableViewTypeItem());
	}

	private final PortletURL _portletURL;
	private final String _selectedType;

}