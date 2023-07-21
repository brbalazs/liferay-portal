/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.javascript.internal;

import com.liferay.portal.scripting.ClassVisibilityChecker;

import java.util.Set;

import org.mozilla.javascript.ClassShutter;

/**
 * @author Alberto Montero
 */
public class JavaScriptClassVisibilityChecker
	extends ClassVisibilityChecker implements ClassShutter {

	public JavaScriptClassVisibilityChecker(
		Set<String> allowedClassNames, Set<String> forbiddenClassNames) {

		super(allowedClassNames, forbiddenClassNames);
	}

	@Override
	public boolean visibleToScripts(String className) {
		return isVisible(className);
	}

}