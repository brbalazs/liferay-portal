/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.soy;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.base.BaseClayCardTag;

/**
 * @author Julien Castelain
 */
public class UserCardTag extends BaseClayCardTag {

	@Override
	public int doStartTag() {
		setComponentBaseName("ClayUserCard");

		return super.doStartTag();
	}

	public void setImageAlt(String imageAlt) {
		putValue("imageAlt", imageAlt);
	}

	public void setImageSrc(String imageSrc) {
		putValue("imageSrc", imageSrc);
	}

	public void setInitials(String initials) {
		putValue("initials", initials);
	}

	public void setName(String name) {
		putValue("name", name);
	}

	public void setSubtitle(String subtitle) {
		putValue("subtitle", subtitle);
	}

	public void setUserColor(String userColor) {
		putValue("userColor", userColor);
	}

}