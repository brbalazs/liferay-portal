/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@ConditionalOnProperty(
	matchIfMissing = true, value = "osb.asah.verify.process.enabled"
)
public class VerifyProcessRunner {

	public void run() {
	}

}