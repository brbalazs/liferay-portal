/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.aui;

import com.liferay.portal.kernel.servlet.taglib.aui.ToolTag;
import com.liferay.taglib.aui.base.BaseToolTagImpl;

/**
 * @author     Julio Camarero
 * @author     Brian Wing Shun Chan
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ToolTagImpl extends BaseToolTagImpl implements ToolTag {

	@Override
	public void cleanUp() {
		super.cleanUp();
	}

	@Override
	public int doStartTag() {
		PanelTag panelTag = (PanelTag)findAncestorWithClass(
			this, PanelTag.class);

		panelTag.addToolTag(new ToolTagImpl());

		return EVAL_PAGE;
	}

}