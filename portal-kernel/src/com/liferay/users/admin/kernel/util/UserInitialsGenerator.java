/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.kernel.util;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.User;

import java.util.Locale;

/**
 * @author Pei-Jung Lan
 */
@ProviderType
public interface UserInitialsGenerator {

	public String getInitials(
		Locale locale, String firstName, String middleName, String lastName);

	public String getInitials(User user);

}