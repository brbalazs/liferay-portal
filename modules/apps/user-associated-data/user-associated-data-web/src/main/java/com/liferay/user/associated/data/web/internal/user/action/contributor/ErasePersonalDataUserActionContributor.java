/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.user.action.contributor;

import com.liferay.users.admin.user.action.contributor.UserActionContributor;

import org.osgi.service.component.annotations.Component;

/**
 * @author Drew Brokke
 */
@Component(immediate = true, service = UserActionContributor.class)
public class ErasePersonalDataUserActionContributor
	extends BaseUADUserActionContributor {

	@Override
	protected String getKey() {
		return "delete-personal-data";
	}

	@Override
	protected String getMVCRenderCommandName() {
		return "/view_uad_summary";
	}

}