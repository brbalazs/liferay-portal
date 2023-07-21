/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.screennamevalidatorfactory;

import com.liferay.portal.kernel.security.auth.ScreenNameValidator;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = ScreenNameValidator.class
)
public class TestScreenNameValidator implements ScreenNameValidator {

	@Override
	public String getAUIValidatorJS() {
		return "function() {return true;}";
	}

	@Override
	public String getDescription(Locale locale) {
		return locale.toString();
	}

	@Override
	public boolean validate(long companyId, String screenName) {
		if ((companyId == 1) && screenName.equals("lftest")) {
			return true;
		}

		return false;
	}

}