/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.trash;

import com.liferay.petra.string.StringPool;
import com.liferay.trash.kernel.util.TrashUtil;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Alexander Chow
 */
public abstract class BaseTrashRenderer implements TrashRenderer {

	@Override
	public String getIconCssClass() {
		return StringPool.BLANK;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public String getIconPath(PortletRequest portletRequest) {
		return StringPool.BLANK;
	}

	@Override
	public String getNewName(String oldName, String token) {
		return TrashUtil.getNewName(oldName, token);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getSummary(PortletRequest, javax.portlet.PortletResponse)}
	 */
	@Deprecated
	@Override
	public String getSummary(Locale locale) {
		return getSummary(null, null);
	}

	@Override
	public String renderActions(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return null;
	}

}