/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.util.PropsValues;

import java.util.List;

/**
 * @author Ricardo Couso
 */
public class GroupControlPanelLayoutUtil {

	public static Layout getGroupControlPanelLayout(Group group)
		throws PortalException {

		long groupId = group.getGroupId();

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, true, LayoutConstants.TYPE_CONTROL_PANEL);

		if (!layouts.isEmpty()) {
			return layouts.get(0);
		}

		long defaultUserId = UserLocalServiceUtil.getDefaultUserId(
			group.getCompanyId());

		String friendlyURL = FriendlyURLNormalizerUtil.normalize(
			PropsValues.CONTROL_PANEL_LAYOUT_FRIENDLY_URL);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAttribute(
			"layout.instanceable.allowed", Boolean.TRUE);

		return LayoutLocalServiceUtil.addLayout(
			defaultUserId, group.getGroupId(), true,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			PropsValues.CONTROL_PANEL_LAYOUT_NAME, StringPool.BLANK,
			StringPool.BLANK, LayoutConstants.TYPE_CONTROL_PANEL, false,
			friendlyURL, serviceContext);
	}

	public static long getGroupControlPanelPlid(Group group)
		throws PortalException {

		Layout groupControlPanelLayout = getGroupControlPanelLayout(group);

		return groupControlPanelLayout.getPlid();
	}

}