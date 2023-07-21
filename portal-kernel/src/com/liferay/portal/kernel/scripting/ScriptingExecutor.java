/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.scripting;

import java.io.File;

import java.util.Map;
import java.util.Set;

/**
 * @author Alberto Montero
 * @author Brian Wing Shun Chan
 */
public interface ScriptingExecutor {

	public void clearCache();

	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile)
		throws ScriptingException;

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #eval(Set, Map,
	 *             Set, File)}
	 */
	@Deprecated
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile,
			ClassLoader... classloaders)
		throws ScriptingException;

	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String script)
		throws ScriptingException;

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #eval(Set, Map,
	 *             Set, String)}
	 */
	@Deprecated
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String script, ClassLoader... classloaders)
		throws ScriptingException;

	public String getLanguage();

	public ScriptingContainer<?> getScriptingContainer();

	public ScriptingExecutor newInstance(boolean executeInSeparateThread);

}