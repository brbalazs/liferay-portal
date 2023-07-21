/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.soy.utils;

import java.util.Map;

/**
 * @author Bruno Basto
 * @deprecated As of Mueller (7.2.x), see {@link
 *             com.liferay.portal.template.soy.util.SoyContext}
 */
@Deprecated
public interface SoyContext extends Map<String, Object> {

	public void clearInjectedData();

	/**
	 * Put an HTML parameter in the SoyContext container. This is the same as
	 * calling {@link SoyContext#put(Object, Object)} with a
	 * {@link com.liferay.portal.template.soy.data.SoyHTMLData} value.
	 * @param key
	 * @param value
	 * @see com.liferay.portal.template.soy.data.SoyHTMLData
	 * @review
	 */
	public void putHTML(String key, String value);

	public void putInjectedData(String key, Object value);

	public void removeInjectedData(String key);

}