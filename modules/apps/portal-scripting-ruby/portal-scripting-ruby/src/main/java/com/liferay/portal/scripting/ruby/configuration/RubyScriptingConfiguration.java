/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.ruby.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Michael C. Han
 */
@ExtendedObjectClassDefinition(category = "scripting-engines")
@Meta.OCD(
	id = "com.liferay.portal.scripting.ruby.configuration.RubyScriptingConfiguration",
	localization = "content/Language",
	name = "ruby-scripting-configuration-name"
)
public interface RubyScriptingConfiguration {

	@Meta.AD(
		deflt = "jit", name = "compile-mode",
		optionValues = {"force", "jit", "none"}, required = false
	)
	public String compileMode();

	@Meta.AD(deflt = "5", name = "compile-threshold", required = false)
	public int compileThreshold();

	@Meta.AD(
		deflt = "classpath:/META-INF/jruby.home/lib/ruby/2.0|classpath:/META-INF/jruby.home/lib/ruby/shared",
		name = "load-paths", required = false
	)
	public String[] loadPaths();

}