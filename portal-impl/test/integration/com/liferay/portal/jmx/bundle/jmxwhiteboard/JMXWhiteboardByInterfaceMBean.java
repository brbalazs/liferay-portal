/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.jmx.bundle.jmxwhiteboard;

/**
 * @author Raymond Augé
 */
public interface JMXWhiteboardByInterfaceMBean {

	public static final String OBJECT_NAME =
		"JMXWhiteboard:name=com.liferay.portal.jmx.bundle.jmxwhiteboard." +
			"JMXWhiteboardByInterfaceMBean";

	public String returnValue(String value);

}