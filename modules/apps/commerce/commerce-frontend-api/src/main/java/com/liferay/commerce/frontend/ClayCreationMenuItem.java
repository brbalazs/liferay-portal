/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.frontend;

/**
 * @author Marco Leo
 */
public class ClayCreationMenuItem {

	public static final String CLAY_CREATION_MENU_ITEM_TYPE_MODAL = "modal";
	public static final String CLAY_CREATION_MENU_ITEM_TYPE_INLINE = "inline";
	public static final String CLAY_CREATION_MENU_ITEM_TYPE_LINK = "";

	public ClayCreationMenuItem(String url, String label) {
		_url = url;
		_label = label;
		_type = CLAY_CREATION_MENU_ITEM_TYPE_LINK;
	}

	public ClayCreationMenuItem(String url, String label, String type) {
		_url = url;
		_label = label;
		_type = type;
	}

	public String getUrl() {
		return _url;
	}

	public String getLabel() {
		return _label;
	}

	public int getOrder() {
		return _order;
	}

	public String getType() {
		return _type;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public void setOrder(int order) {
		_order = order;
	}

	public void setType(String type) {
		_type = type;
	}

	private String _url;
	private String _label;
	private String _type;
	private int _order;

}