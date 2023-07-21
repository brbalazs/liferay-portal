/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.web.internal.servlet.taglib.ui;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;
import com.liferay.users.admin.web.internal.CustomFieldsUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pei-Jung Lan
 */
@Component(
	property = "form.navigator.entry.order:Integer=10",
	service = FormNavigatorEntry.class
)
public class OrganizationCustomFieldsFormNavigatorEntry
	extends BaseOrganizationFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.CATEGORY_KEY_ORGANIZATION_MISCELLANEOUS;
	}

	@Override
	public String getKey() {
		return "custom-fields";
	}

	@Override
	public boolean isVisible(User user, Organization organization) {
		if ((organization != null) &&
			CustomFieldsUtil.hasVisibleCustomFields(
				organization.getCompanyId(), Organization.class)) {

			return true;
		}

		return false;
	}

	@Override
	protected String getJspPath() {
		return "/organization/custom_fields.jsp";
	}

}