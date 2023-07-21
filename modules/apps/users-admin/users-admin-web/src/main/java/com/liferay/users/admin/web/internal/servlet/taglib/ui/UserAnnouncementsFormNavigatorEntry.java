/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.web.internal.servlet.taglib.ui;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;

/**
 * @author     Pei-Jung Lan
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.users.admin.web.internal.frontend.taglib.servlet.taglib.UserAlertsAndAnnouncementsDeliveryScreenNavigationEntry}
 */
@Deprecated
public class UserAnnouncementsFormNavigatorEntry
	extends BaseUserFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.CATEGORY_KEY_USER_MISCELLANEOUS;
	}

	@Override
	public String getKey() {
		return "announcements";
	}

	@Override
	public boolean isVisible(User user, User selUser) {
		if (selUser == null) {
			return false;
		}

		return true;
	}

	@Override
	protected String getJspPath() {
		return "/user/announcements.jsp";
	}

}