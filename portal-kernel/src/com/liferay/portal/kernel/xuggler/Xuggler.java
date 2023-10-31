/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.xuggler;

/**
 * @author Alexander Chow
 */
public interface Xuggler {

	public default void installNativeLibraries(String name) throws Exception {
		installNativeLibraries(name, null);
	}

	public void installNativeLibraries(String name, String sha1)
		throws Exception;

	public boolean isEnabled();

	public boolean isEnabled(boolean checkNativeLibraries);

	public boolean isNativeLibraryCopied();

	public boolean isNativeLibraryInstalled();

}