/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.trash;

import com.liferay.asset.kernel.model.Renderer;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Zsolt Berentey
 */
public interface TrashRenderer extends Renderer {

	public String getNewName(String oldName, String token);

	public String getPortletId();

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getSummary(PortletRequest, PortletResponse)}
	 */
	@Deprecated
	public String getSummary(Locale locale);

	public String getType();

	public String renderActions(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception;

}