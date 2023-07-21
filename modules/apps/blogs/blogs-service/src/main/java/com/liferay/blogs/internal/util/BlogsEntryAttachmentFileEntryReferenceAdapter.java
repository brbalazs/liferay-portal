/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.internal.util;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portlet.blogs.BlogsEntryAttachmentFileEntryReference;

/**
 * @author Alejandro Tardín
 */
public class BlogsEntryAttachmentFileEntryReferenceAdapter
	extends BlogsEntryAttachmentFileEntryReference {

	public BlogsEntryAttachmentFileEntryReferenceAdapter(
		long tempBlogsEntryAttachmentFileEntryId,
		FileEntry blogsEntryAttachmentFileEntry) {

		super(
			tempBlogsEntryAttachmentFileEntryId, blogsEntryAttachmentFileEntry);

		_tempBlogsEntryAttachmentFileEntryId =
			tempBlogsEntryAttachmentFileEntryId;
		_blogsEntryAttachmentFileEntry = blogsEntryAttachmentFileEntry;
	}

	@Override
	public FileEntry getBlogsEntryAttachmentFileEntry() {
		return _blogsEntryAttachmentFileEntry;
	}

	@Override
	public long getTempBlogsEntryAttachmentFileEntryId() {
		return _tempBlogsEntryAttachmentFileEntryId;
	}

	private final FileEntry _blogsEntryAttachmentFileEntry;
	private final long _tempBlogsEntryAttachmentFileEntryId;

}