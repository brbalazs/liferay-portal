/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.target.platform.indexer;

import java.io.OutputStream;

/**
 * @author Raymond Augé
 */
public interface Indexer {

	public static final String DIR_NAME_TARGET_PLATFORM = "target-platform";

	public void index(OutputStream outputStream) throws Exception;

}