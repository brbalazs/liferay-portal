/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.portlet.action;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.struts.FindActionHelper;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.portal.kernel.repository.model.Folder",
	service = FindActionHelper.class
)
public class DLFolderFindActionHelper extends BaseDLFindActionHelper {

	@Override
	public long getGroupId(long primaryKey) throws Exception {
		Folder folder = _dlAppLocalService.getFolder(primaryKey);

		return folder.getRepositoryId();
	}

	@Override
	public String getPrimaryKeyParameterName() {
		return "folderId";
	}

	@Override
	public void setPrimaryKeyParameter(PortletURL portletURL, long primaryKey)
		throws Exception {

		if (primaryKey != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			Folder folder = _dlAppLocalService.getFolder(primaryKey);

			primaryKey = folder.getFolderId();
		}

		portletURL.setParameter("folderId", String.valueOf(primaryKey));
	}

	@Override
	protected void addRequiredParameters(
		HttpServletRequest request, String portletId, PortletURL portletURL) {

		String rootPortletId = PortletIdCodec.decodePortletName(portletId);

		if (rootPortletId.equals(DLPortletKeys.MEDIA_GALLERY_DISPLAY)) {
			portletURL.setParameter(
				"mvcRenderCommandName", "/image_gallery_display/view");
		}
		else {
			portletURL.setParameter(
				"mvcRenderCommandName", "/document_library/view_folder");
		}
	}

	@Override
	protected PortletLayoutFinder getPortletLayoutFinder() {
		return _portletPageFinder;
	}

	@Reference(unbind = "-")
	protected void setDLAppLocalService(DLAppLocalService dlAppLocalService) {
		_dlAppLocalService = dlAppLocalService;
	}

	@Reference(
		target = "(model.class.name=com.liferay.portal.kernel.repository.model.Folder)",
		unbind = "-"
	)
	protected void setPortletLayoutFinder(
		PortletLayoutFinder portletPageFinder) {

		_portletPageFinder = portletPageFinder;
	}

	private DLAppLocalService _dlAppLocalService;
	private PortletLayoutFinder _portletPageFinder;

}