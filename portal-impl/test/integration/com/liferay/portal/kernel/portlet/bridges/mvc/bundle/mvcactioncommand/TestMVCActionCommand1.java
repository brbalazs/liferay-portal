/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet.bridges.mvc.bundle.mvcactioncommand;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TestPortlet.PORTLET_NAME,
		"mvc.command.name=" + TestMVCActionCommand1.TEST_MVC_ACTION_COMMAND_NAME
	},
	service = MVCActionCommand.class
)
public class TestMVCActionCommand1 implements MVCActionCommand {

	public static final String TEST_MVC_ACTION_COMMAND_ATTRIBUTE =
		"TEST_MVC_ACTION_COMMAND_ATTRIBUTE";

	public static final String TEST_MVC_ACTION_COMMAND_NAME =
		"TEST_MVC_ACTION_COMMAND_NAME";

	@Override
	public boolean processAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		actionRequest.setAttribute(
			TEST_MVC_ACTION_COMMAND_ATTRIBUTE,
			TEST_MVC_ACTION_COMMAND_ATTRIBUTE);

		return true;
	}

}