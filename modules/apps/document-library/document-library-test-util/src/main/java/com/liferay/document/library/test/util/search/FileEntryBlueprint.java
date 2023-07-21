/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.test.util.search;

import java.io.InputStream;

/**
 * @author Wade Cao
 */
public class FileEntryBlueprint {

	public String[] getAssetTagNames() {
		return assetTagNames;
	}

	public String getFileName() {
		return fileName;
	}

	public long getGroupId() {
		return groupId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getTitle() {
		return title;
	}

	public Long getUserId() {
		return userId;
	}

	protected String[] assetTagNames;
	protected String fileName;
	protected long groupId;
	protected InputStream inputStream;
	protected String title;
	protected Long userId;

}