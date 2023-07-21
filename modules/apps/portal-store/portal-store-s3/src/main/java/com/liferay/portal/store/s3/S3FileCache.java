/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.s3;

import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;

/**
 * @author Michael C. Han
 */
public interface S3FileCache {

	public void cleanUpCacheFiles();

	public File getCacheFile(S3Object s3Object, String fileName)
		throws IOException;

}