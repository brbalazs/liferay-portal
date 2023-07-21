/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;

/**
 * @author Jorge Ferrer
 */
public interface ControlPanelEntry {

	public boolean hasAccessPermission(
			PermissionChecker permissionChecker, Group group, Portlet portlet)
		throws Exception;

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement.<p>This
	 *             method was originally defined to determine if a portlet
	 *             should be displayed in the Control Panel. In this version,
	 *             this method should always return <code>false</code> and
	 *             remains only to preserve binary compatibility. This method
	 *             will be permanently removed in a future version.</p><p>In
	 *             lieu of this method, the Control Panel now uses {@link
	 *             #hasAccessPermission} to determine if a portlet should be
	 *             displayed in the Control Panel.</p>
	 */
	@Deprecated
	public boolean isVisible(
			PermissionChecker permissionChecker, Portlet portlet)
		throws Exception;

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement.<p>This
	 *             method was originally defined to determine if a portlet
	 *             should be displayed in the Control Panel. In this version,
	 *             this method should always return <code>false</code> and
	 *             remains only to preserve binary compatibility. This method
	 *             will be permanently removed in a future version.</p><p>In
	 *             lieu of this method, the Control Panel now uses {@link
	 *             #hasAccessPermission} to determine if a portlet should be
	 *             displayed in the Control Panel.</p>
	 */
	@Deprecated
	public boolean isVisible(
			Portlet portlet, String category, ThemeDisplay themeDisplay)
		throws Exception;

}