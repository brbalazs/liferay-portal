/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.form.internal.dto.v1_0.util;

import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.headless.form.dto.v1_0.FormDocument;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;

/**
 * @author Victor Oliveira
 */
public class FormDocumentUtil {

	public static FormDocument toFormDocument(FileEntry fileEntry)
		throws Exception {

		FileVersion fileVersion = fileEntry.getFileVersion();

		return new FormDocument() {
			{
				contentUrl = DLUtil.getPreviewURL(
					fileEntry, fileVersion, null, "");
				encodingFormat = fileEntry.getMimeType();
				fileExtension = fileEntry.getExtension();
				id = fileEntry.getFileEntryId();
				siteId = fileEntry.getGroupId();
				sizeInBytes = fileEntry.getSize();
				title = fileEntry.getTitle();
			}
		};
	}

}