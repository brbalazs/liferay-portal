/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.web.internal.portlet.action;

import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.service.MBThreadLocalService;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.BaseFindActionHelper;
import com.liferay.portal.struts.FindActionHelper;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.message.boards.model.MBThread",
	service = FindActionHelper.class
)
public class ThreadFindActionHelper extends BaseFindActionHelper {

	@Override
	public long getGroupId(long primaryKey) throws Exception {
		MBThread thread = _mbThreadLocalService.getThread(primaryKey);

		return thread.getGroupId();
	}

	@Override
	public String getPrimaryKeyParameterName() {
		return "threadId";
	}

	@Override
	public PortletURL processPortletURL(
			HttpServletRequest request, PortletURL portletURL)
		throws Exception {

		long threadId = ParamUtil.getLong(
			request, getPrimaryKeyParameterName());

		MBThread thread = _mbThreadLocalService.getThread(threadId);

		portletURL.setParameter(
			"messageId", String.valueOf(thread.getRootMessageId()));

		return portletURL;
	}

	@Override
	public void setPrimaryKeyParameter(PortletURL portletURL, long primaryKey)
		throws Exception {
	}

	@Override
	protected void addRequiredParameters(
		HttpServletRequest request, String portletId, PortletURL portletURL) {

		portletURL.setParameter(
			"mvcRenderCommandName", "/message_boards/view_message");
	}

	@Override
	protected PortletLayoutFinder getPortletLayoutFinder() {
		return _portletPageFinder;
	}

	@Reference(unbind = "-")
	protected void setMBThreadLocalService(
		MBThreadLocalService mbThreadLocalService) {

		_mbThreadLocalService = mbThreadLocalService;
	}

	@Reference(
		target = "(model.class.name=com.liferay.message.boards.model.MBThread)",
		unbind = "-"
	)
	protected void setPortletLayoutFinder(
		PortletLayoutFinder portletPageFinder) {

		_portletPageFinder = portletPageFinder;
	}

	private MBThreadLocalService _mbThreadLocalService;
	private PortletLayoutFinder _portletPageFinder;

}