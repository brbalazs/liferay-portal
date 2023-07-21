/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet.bundle.resourcebundletracker;

import javax.portlet.GenericPortlet;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TestPortlet.PORTLET_NAME,
		"javax.portlet.resource-bundle=content.Language"
	},
	service = Portlet.class
)
public class TestPortlet extends GenericPortlet {

	public static final String PORTLET_NAME =
		"com_liferay_portal_kernel_portlet_bundle_resourcebundletracker_" +
			"TestPortlet";

}