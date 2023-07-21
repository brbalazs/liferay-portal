/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.soy;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.base.BaseClayCardTag;

/**
 * @author Julien Castelain
 */
public class HorizontalCardTag extends BaseClayCardTag {

	@Override
	public int doStartTag() {
		setComponentBaseName("ClayHorizontalCard");

		return super.doStartTag();
	}

	public void setIcon(String icon) {
		putValue("icon", icon);
	}

	public void setTitle(String title) {
		putValue("title", title);
	}

}