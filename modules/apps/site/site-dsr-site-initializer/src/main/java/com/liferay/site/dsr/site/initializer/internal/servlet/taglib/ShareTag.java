/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.dsr.site.initializer.internal.servlet.taglib;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.site.dsr.site.initializer.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.PageContext;

import java.util.Objects;

/**
 * @author Balazs Breier
 */
public class ShareTag extends IncludeTag {

	public long getRoomId() {
		return _roomId;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setRoomId(long roomId) {
		_roomId = roomId;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_roomId = 0;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		long roomId = 0;

		if (_roomId != 0) {
			Group group = GroupLocalServiceUtil.fetchGroup(_roomId);

			if (group != null) {
				ObjectDefinition objectDefinition =
					ObjectDefinitionLocalServiceUtil.
						fetchObjectDefinitionByExternalReferenceCode(
							"L_DSR_ROOM", group.getCompanyId());

				if ((objectDefinition != null) &&
					Objects.equals(
						group.getClassName(),
						objectDefinition.getClassName())) {

					roomId = group.getClassPK();
				}
			}
		}

		httpServletRequest.setAttribute(
			"liferay-site-dsr-site-initializer:share:roomId", roomId);
	}

	private static final String _PAGE = "/share/page.jsp";

	private long _roomId;

}