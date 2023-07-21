/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.bookmarks.web.internal.portlet.action;

import com.liferay.bookmarks.exception.NoSuchEntryException;
import com.liferay.bookmarks.model.BookmarksEntry;
import com.liferay.bookmarks.service.BookmarksEntryService;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "path=/bookmarks/open_entry", service = StrutsAction.class
)
public class OpenEntryAction extends BaseStrutsAction {

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {
			long entryId = ParamUtil.getLong(request, "entryId");

			BookmarksEntry entry = _bookmarksEntryService.getEntry(entryId);

			if (entry.isInTrash()) {
				int status = ParamUtil.getInteger(
					request, "status", WorkflowConstants.STATUS_APPROVED);

				if (status != WorkflowConstants.STATUS_IN_TRASH) {
					throw new NoSuchEntryException("{entryId=" + entryId + "}");
				}
			}

			entry = _bookmarksEntryService.openEntry(entry);

			response.sendRedirect(entry.getUrl());

			return null;
		}
		catch (Exception e) {
			_portal.sendError(e, request, response);

			return null;
		}
	}

	@Reference(unbind = "-")
	protected void setBookmarksEntryService(
		BookmarksEntryService bookmarksEntryService) {

		_bookmarksEntryService = bookmarksEntryService;
	}

	private BookmarksEntryService _bookmarksEntryService;

	@Reference
	private Portal _portal;

}