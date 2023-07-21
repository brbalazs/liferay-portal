/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.soy;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.base.BaseClayCardTag;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;

import java.util.List;
import java.util.Map;

/**
 * @author Julien Castelain
 */
public class FileCardTag extends BaseClayCardTag {

	@Override
	public int doStartTag() {
		setComponentBaseName("ClayFileCard");

		return super.doStartTag();
	}

	public void setIcon(String icon) {
		putValue("icon", icon);
	}

	public void setLabels(List<LabelItem> labelItems) {
		putValue("labels", labelItems);
	}

	public void setLabelStylesMap(Map<String, String> labelStylesMap) {
		putValue("labelStylesMap", labelStylesMap);
	}

	public void setStickerLabel(String stickerLabel) {
		putValue("stickerLabel", stickerLabel);
	}

	public void setStickerShape(String stickerShape) {
		putValue("stickerShape", stickerShape);
	}

	public void setStickerStyle(String stickerStyle) {
		putValue("stickerStyle", stickerStyle);
	}

	public void setSubtitle(String subtitle) {
		putValue("subtitle", subtitle);
	}

	public void setTitle(String title) {
		putValue("title", title);
	}

}