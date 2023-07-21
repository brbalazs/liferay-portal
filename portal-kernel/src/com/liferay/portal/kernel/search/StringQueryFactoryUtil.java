/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

/**
 * @author     Raymond Augé
 * @author     Brian Wing Shun Chan
 * @deprecated As of Wilberforce (7.0.x)
 */
@Deprecated
public class StringQueryFactoryUtil {

	public static Query create(String query) {
		return getStringQueryFactory().create(query);
	}

	public static StringQueryFactory getStringQueryFactory() {
		return _stringQueryFactory;
	}

	public void setStringQueryFactory(StringQueryFactory stringQueryFactory) {
		_stringQueryFactory = stringQueryFactory;
	}

	private static StringQueryFactory _stringQueryFactory;

}