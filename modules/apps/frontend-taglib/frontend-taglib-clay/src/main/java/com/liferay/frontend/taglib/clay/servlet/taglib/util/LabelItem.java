/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import java.util.HashMap;

/**
 * @author Carlos Lancha
 */
public class LabelItem extends HashMap<String, Object> {

	public LabelItem() {
		put("closeable", false);
	}

	public void setCloseable(boolean closeable) {
		put("closeable", closeable);
	}

	public void setLabel(String label) {
		put("label", label);
	}

	public void setStyle(String style) {
		put("style", style);
	}

}