/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.findbugs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author André Miranda
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {

	String[] value() default {};

}