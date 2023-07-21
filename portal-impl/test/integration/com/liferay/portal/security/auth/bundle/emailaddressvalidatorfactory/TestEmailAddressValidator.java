/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.emailaddressvalidatorfactory;

import com.liferay.portal.kernel.security.auth.EmailAddressValidator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = EmailAddressValidator.class
)
public class TestEmailAddressValidator implements EmailAddressValidator {

	@Override
	public boolean validate(long companyId, String emailAddress) {
		if ((companyId == 1) &&
			emailAddress.equals("TestEmailAddressValidator@liferay-test.com")) {

			return true;
		}

		return false;
	}

}