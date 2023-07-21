/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.tck.bridge.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Shuyang Zhou
 */
@ExtendedObjectClassDefinition(category = "infrastructure")
@Meta.OCD(
	id = "com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration",
	localization = "content/Language",
	name = "portlet-tck-bridge-configuration-name"
)
public interface PortletTCKBridgeConfiguration {

	@Meta.AD(deflt = "8239", required = false)
	public int handshakeServerPort();

	@Meta.AD(deflt = "", required = false)
	public String[] servletContextNames();

	@Meta.AD(deflt = "1200", required = false)
	public long timeout();

}