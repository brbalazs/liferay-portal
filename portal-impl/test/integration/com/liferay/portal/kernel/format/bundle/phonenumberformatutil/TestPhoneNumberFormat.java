/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.format.bundle.phonenumberformatutil;

import com.liferay.portal.kernel.format.PhoneNumberFormat;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = PhoneNumberFormat.class
)
public class TestPhoneNumberFormat implements PhoneNumberFormat {

	public static final String FORMATTED = "123-456-7890";

	public static final String UNFORMATTED = "1234567890";

	@Override
	public String format(String phoneNumber) {
		if (phoneNumber.equals(UNFORMATTED)) {
			return FORMATTED;
		}

		return "";
	}

	@Override
	public String strip(String phoneNumber) {
		if (phoneNumber.equals(FORMATTED)) {
			return UNFORMATTED;
		}

		return "";
	}

	@Override
	public boolean validate(String phoneNumber) {
		if (phoneNumber.equals(FORMATTED)) {
			return true;
		}

		return false;
	}

}