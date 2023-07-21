/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.jmx.bundle.jmxwhiteboard;

import javax.management.DynamicMBean;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 */
@Component(
	property = "jmx.objectname=" + JMXWhiteboardByDynamicMBean.OBJECT_NAME,
	service = DynamicMBean.class
)
public class JMXWhiteboardByDynamicMBean
	extends StandardMBean implements JMXWhiteboardByInterfaceMBean {

	public static final String OBJECT_NAME =
		"JMXWhiteboard:name=com.liferay.portal.jmx.bundle.jmxwhiteboard." +
			"JMXWhiteboardByDynamicMBean";

	public JMXWhiteboardByDynamicMBean() throws NotCompliantMBeanException {
		super(JMXWhiteboardByInterfaceMBean.class);
	}

	@Override
	public String returnValue(String value) {
		return "{" + value + "}";
	}

}