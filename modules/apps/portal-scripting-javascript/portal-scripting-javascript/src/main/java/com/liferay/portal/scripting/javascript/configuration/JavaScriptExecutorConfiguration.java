/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.javascript.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Michael C. Han
 */
@ExtendedObjectClassDefinition(category = "scripting-engines")
@Meta.OCD(
	id = "com.liferay.portal.scripting.javascript.configuration.JavaScriptExecutorConfiguration",
	localization = "content/Language",
	name = "javascript-executor-configuration-name"
)
public interface JavaScriptExecutorConfiguration {

	@Meta.AD(
		deflt = "com.liferay.portal.kernel.scripting.ScriptingUtil|com.liferay.portal.scripting.internal.ScriptingImpl|java.lang.System",
		name = "forbidden-class-names", required = false
	)
	public String[] forbiddenClassNames();

}