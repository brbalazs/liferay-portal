/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.PortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		"auth.token.ignore.mvc.action=1",
		"javax.portlet.name=" + TestMVCActionCommand.TEST_PORTLET_ID,
		"mvc.command.name=" + TestMVCActionCommand.TEST_MVC_COMMAND_NAME,
		"portlet.add.default.resource.check.whitelist.mvc.action=1",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = MVCActionCommand.class
)
public class TestMVCActionCommand implements MVCActionCommand {

	public static final String TEST_MVC_COMMAND_NAME =
		"TEST_MVC_ACTION_COMMAND_NAME";

	public static final String TEST_PORTLET_ID = PortletKeys.PORTAL;

	@Override
	public boolean processAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		return false;
	}

}