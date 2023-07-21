/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.memberships.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Rodrigo Paulino
 */
@ExtendedObjectClassDefinition(category = "site-memberships")
@Meta.OCD(
	id = "com.liferay.site.memberships.web.internal.configuration.UserGroupsWebConfiguration",
	localization = "content/Language",
	name = "user-groups-web-configuration-name"
)
public interface UserGroupsWebConfiguration {

	@Meta.AD(
		description = "enable-assign-unassign-role-actions-help",
		name = "enable-assign-unassign-role-actions", required = false
	)
	public boolean enableAssignUnassignRoleActions();

}