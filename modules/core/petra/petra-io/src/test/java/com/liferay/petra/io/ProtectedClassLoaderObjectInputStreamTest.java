/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author Preston Crary
 */
public class ProtectedClassLoaderObjectInputStreamTest
	extends ClassLoaderObjectInputStreamTest {

	@Override
	protected ObjectInputStream getObjectInputStream(
			InputStream inputStream, ClassLoader classLoader)
		throws IOException {

		return new ProtectedClassLoaderObjectInputStream(
			inputStream, classLoader);
	}

}