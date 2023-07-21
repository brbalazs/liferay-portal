/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.web.internal.servlet.taglib.ui;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.users.admin.configuration.UserFileUploadsConfiguration;

import java.io.IOException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;

/**
 * @author     Pei-Jung Lan
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.users.admin.web.internal.frontend.taglib.servlet.taglib.UserInformationScreenNavigationEntry}
 */
@Deprecated
public class UserDetailsFormNavigatorEntry extends BaseUserFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.CATEGORY_KEY_USER_USER_INFORMATION;
	}

	@Override
	public String getKey() {
		return "details";
	}

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		request.setAttribute(
			UserFileUploadsConfiguration.class.getName(),
			_userFileUploadsConfiguration);

		super.include(request, response);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_userFileUploadsConfiguration = ConfigurableUtil.createConfigurable(
			UserFileUploadsConfiguration.class, properties);
	}

	@Override
	protected String getJspPath() {
		return "/user/details.jsp";
	}

	private volatile UserFileUploadsConfiguration _userFileUploadsConfiguration;

}