/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.PortletKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TestMVCResourceCommand.TEST_PORTLET_ID,
		"mvc.command.name=" + TestMVCResourceCommand.TEST_MVC_COMMAND_NAME,
		"portlet.add.default.resource.check.whitelist.mvc.action=1",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = MVCResourceCommand.class
)
public class TestMVCResourceCommand implements MVCResourceCommand {

	public static final String TEST_MVC_COMMAND_NAME =
		"TEST_MVC_RESOURCE_COMMAND_NAME";

	public static final String TEST_PORTLET_ID = PortletKeys.PORTAL;

	@Override
	public boolean serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		return false;
	}

}