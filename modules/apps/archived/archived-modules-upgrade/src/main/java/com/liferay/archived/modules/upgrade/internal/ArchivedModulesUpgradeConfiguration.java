/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.archived.modules.upgrade.internal;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Preston Crary
 */
@ExtendedObjectClassDefinition(category = "upgrades")
@Meta.OCD(
	id = "com.liferay.archived.modules.upgrade.internal.ArchivedModulesUpgradeConfiguration",
	name = "deprecated-modules-upgrade-configuration-name"
)
public interface ArchivedModulesUpgradeConfiguration {

	@Meta.AD(
		deflt = "false", name = "remove-chat-module-data", required = false
	)
	public boolean removeChatModuleData();

	@Meta.AD(
		deflt = "false", name = "remove-mail-reader-module-data",
		required = false
	)
	public boolean removeMailReaderModuleData();

	@Meta.AD(deflt = "false", name = "remove-shopping-module", required = false)
	public boolean removeShoppingModuleData();

	@Meta.AD(
		deflt = "false", name = "remove-private-messaging-module",
		required = false
	)
	public boolean removePrivateMessagingModuleData();

	@Meta.AD(deflt = "false", name = "remove-twitter-module", required = false)
	public boolean removeTwitterModuleData();

}