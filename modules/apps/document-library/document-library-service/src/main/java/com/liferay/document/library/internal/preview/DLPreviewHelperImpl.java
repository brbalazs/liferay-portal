/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.internal.preview;

import com.liferay.document.library.model.DLFileVersionPreview;
import com.liferay.document.library.service.DLFileVersionPreviewLocalService;
import com.liferay.portlet.preview.DLPreviewHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lianne Louie
 */
@Component(immediate = true, service = DLPreviewHelper.class)
public class DLPreviewHelperImpl implements DLPreviewHelper {

	@Override
	public boolean hasDLFileVersionPreview(
		long fileEntryId, long fileVersionId, int previewStatus) {

		DLFileVersionPreview dlFileVersionPreview =
			_dlFileVersionPreviewLocalService.fetchDLFileVersionPreview(
				fileEntryId, fileVersionId, previewStatus);

		if (dlFileVersionPreview == null) {
			return false;
		}

		return true;
	}

	@Reference
	private DLFileVersionPreviewLocalService _dlFileVersionPreviewLocalService;

}