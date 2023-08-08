/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.profile;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@Profile("default")
public class ProfileChecker {

	@PostConstruct
	public void init() {
		throw new IllegalStateException(
			"No profile was specified. Using \"default\" is not allowed.");
	}

}