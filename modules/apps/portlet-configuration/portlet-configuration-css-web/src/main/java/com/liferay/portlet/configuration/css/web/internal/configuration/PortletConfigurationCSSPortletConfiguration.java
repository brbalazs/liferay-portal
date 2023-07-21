/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.configuration.css.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Jürgen Kappler
 */
@ExtendedObjectClassDefinition(category = "widget-tools")
@Meta.OCD(
	id = "com.liferay.portlet.configuration.css.web.internal.configuration.PortletConfigurationCSSPortletConfiguration",
	localization = "content/Language",
	name = "portlet-configuration-css-portlet-configuration-name"
)
public interface PortletConfigurationCSSPortletConfiguration {

	@Meta.AD(
		deflt = "false",
		description = "show-link-application-urls-to-page-description",
		name = "show-link-application-urls-to-page-name", required = false
	)
	public boolean showLinkToPage();

}