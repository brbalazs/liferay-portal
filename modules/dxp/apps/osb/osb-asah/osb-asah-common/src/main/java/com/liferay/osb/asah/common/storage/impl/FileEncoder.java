/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

/**
 * @author Marcellus Tavares
 */
public interface FileEncoder {

	public void close() throws Exception;

	public void encode(String data) throws Exception;

	public long getDataSize();

	public void open() throws Exception;

}