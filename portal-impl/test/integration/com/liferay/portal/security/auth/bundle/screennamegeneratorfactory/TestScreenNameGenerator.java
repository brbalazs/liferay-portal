/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.screennamegeneratorfactory;

import com.liferay.portal.kernel.security.auth.ScreenNameGenerator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = ScreenNameGenerator.class
)
public class TestScreenNameGenerator implements ScreenNameGenerator {

	@Override
	public String generate(long companyId, long userId, String emailAddress)
		throws Exception {

		return companyId + "-" + userId;
	}

}