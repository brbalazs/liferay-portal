/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import java.net.URL;

/**
 * @author     Brian Wing Shun Chan
 * @author     Miguel Pastor
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class DefaultServiceLoaderCondition implements ServiceLoaderCondition {

	@Override
	public boolean isLoad(URL url) {
		return _LOAD;
	}

	private static final boolean _LOAD = true;

}