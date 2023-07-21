/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.emailaddressgeneratorfactory;

import com.liferay.portal.kernel.security.auth.EmailAddressGenerator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = EmailAddressGenerator.class
)
public class TestEmailAddressGenerator implements EmailAddressGenerator {

	@Override
	public String generate(long companyId, long userId) {
		return userId + "@generated.com";
	}

	@Override
	public boolean isFake(String emailAddress) {
		if (emailAddress.endsWith("@generated.com")) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isGenerated(String emailAddress) {
		return emailAddress.endsWith("@generated.com");
	}

}