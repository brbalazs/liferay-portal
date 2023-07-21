/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.bundle.portletpreferencesfactoryimplgetpreferencesids;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Juergen Kappler
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.preferences-company-wide=false",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=true",
		"javax.portlet.name=" + TestGroupLayoutPortlet.PORTLET_NAME
	},
	service = Portlet.class
)
public class TestGroupLayoutPortlet extends MVCPortlet {

	public static final String PORTLET_NAME =
		"com_liferay_portlet_bundle_portletpreferencesfactoryimplget" +
			"preferencesids_TestGroupLayoutPortlet";

}