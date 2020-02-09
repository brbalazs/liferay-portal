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

	public static final String CLAY_CREATION_MENU_ITEM_TARGET_INLINE = "inline";

	public static final String CLAY_CREATION_MENU_ITEM_TARGET_LINK = "";

	public static final String CLAY_CREATION_MENU_ITEM_TARGET_MODAL = "modal";

	public static final String CLAY_CREATION_MENU_ITEM_TARGET_SIDE_PANEL = "sidePanel";

	public static final String CLAY_CREATION_MENU_ITEM_TARGET_EVENT = "event";

	public ClayCreationMenuItem(String href, String label) {
		_href = href;
		_label = label;
		_target = CLAY_CREATION_MENU_ITEM_TARGET_LINK;
	}

	public ClayCreationMenuItem(String href, String label, String target) {
		_href = href;
		_label = label;
		_target = target;
	}

	public String getHref() {
		return _href;
	}

	public String getLabel() {
		return _label;
	}

	public int getOrder() {
		return _order;
	}

	public String getTarget() {
		return _target;
	}

	public void setHref(String href) {
		_href = href;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public void setOrder(int order) {
		_order = order;
	}

	public void setTarget(String target) {
		_target = target;
	}

	private String _href;
	private String _label;
	private int _order;
	private String _target;

}