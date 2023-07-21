/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.web.internal.servlet.taglib.ui;

import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pei-Jung Lan
 */
@Component(
	property = "form.navigator.entry.order:Integer=20",
	service = FormNavigatorEntry.class
)
public class OrganizationOrganizationSiteFormNavigatorEntry
	extends BaseOrganizationFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.
			CATEGORY_KEY_ORGANIZATION_ORGANIZATION_INFORMATION;
	}

	@Override
	public String getKey() {
		return "organization-site";
	}

	@Override
	protected String getJspPath() {
		return "/organization/organization_site.jsp";
	}

}