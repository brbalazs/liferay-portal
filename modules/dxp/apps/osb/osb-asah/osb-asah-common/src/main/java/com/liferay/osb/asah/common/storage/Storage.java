/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage;

import java.io.File;
import java.io.InputStream;

import java.util.Date;

/**
 * @author Marcellus Tavares
 */
public interface Storage {

	public void close();

	public void flush();

	public File readSparkJobResult(
		Date sparkJobResultDateAfter, String sparkJobResultPathPrefix);

	public boolean write(InputStream inputStream);

	public boolean write(String data);

}