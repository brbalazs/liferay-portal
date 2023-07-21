/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.PortletKeys;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TestMVCRenderCommand.TEST_PORTLET_ID,
		"mvc.command.name=" + TestMVCRenderCommand.TEST_MVC_COMMAND_NAME,
		"portlet.add.default.resource.check.whitelist.mvc.action=1",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = MVCRenderCommand.class
)
public class TestMVCRenderCommand implements MVCRenderCommand {

	public static final String TEST_MVC_COMMAND_NAME =
		"TEST_MVC_RENDER_COMMAND_NAME";

	public static final String TEST_PORTLET_ID = PortletKeys.PORTAL;

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		return null;
	}

}