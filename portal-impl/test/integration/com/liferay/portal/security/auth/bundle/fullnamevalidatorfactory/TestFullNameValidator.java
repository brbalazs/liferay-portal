/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.fullnamevalidatorfactory;

import com.liferay.portal.kernel.security.auth.FullNameValidator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = FullNameValidator.class
)
public class TestFullNameValidator implements FullNameValidator {

	@Override
	public boolean validate(
		long companyId, String firstName, String middleName, String lastName) {

		if ((companyId == 1) && firstName.equals("Brian")) {
			return true;
		}

		return false;
	}

}