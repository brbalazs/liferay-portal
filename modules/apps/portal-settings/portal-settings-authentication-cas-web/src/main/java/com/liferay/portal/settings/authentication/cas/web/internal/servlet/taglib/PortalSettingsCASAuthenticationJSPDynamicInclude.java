/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.settings.authentication.cas.web.internal.servlet.taglib;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.taglib.BaseJSPDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Adds a CAS tab to the Authentication section of the Portal Settings user
 * interface in the Control Panel.
 *
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true, property = "portal.settings.authentication.tabs.name=cas",
	service = DynamicInclude.class
)
public class PortalSettingsCASAuthenticationJSPDynamicInclude
	extends BaseJSPDynamicInclude {

	@Override
	protected String getJspPath() {
		return "/dynamic_include/com.liferay.portal.settings.web/cas.jsp";
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.portal.settings.authentication.cas.web)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalSettingsCASAuthenticationJSPDynamicInclude.class);

}