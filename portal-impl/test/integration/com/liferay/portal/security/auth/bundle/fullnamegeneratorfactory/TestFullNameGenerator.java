/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.fullnamegeneratorfactory;

import com.liferay.portal.kernel.security.auth.FullNameGenerator;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = FullNameGenerator.class
)
public class TestFullNameGenerator implements FullNameGenerator {

	@Override
	public String getFullName(
		String firstName, String middleName, String lastName) {

		return StringBundler.concat(firstName, " ", middleName, " ", lastName);
	}

	@Override
	public String getLocalizedFullName(
		String firstName, String middleName, String lastName, Locale locale,
		long prefixId, long suffixId) {

		if (firstName.equals("James") && locale.equals(LocaleUtil.FRENCH)) {
			return "Jacques";
		}

		return "not Jacques";
	}

	@Override
	public String[] splitFullName(String fullName) {
		return new String[] {"firstName", "middleName", "lastName"};
	}

}