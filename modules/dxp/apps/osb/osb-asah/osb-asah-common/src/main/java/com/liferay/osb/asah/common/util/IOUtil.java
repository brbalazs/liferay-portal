/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Marcellus Tavares
 */
public class IOUtil {

	public static long countLines(byte[] bytes) throws IOException {
		long lines = 0;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(bytes)))) {

			while (bufferedReader.readLine() != null) {
				lines++;
			}
		}

		return lines;
	}

}