/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.scripting;

import java.util.Map;
import java.util.Set;

/**
 * @author Alberto Montero
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public interface Scripting {

	public void clearCache(String language) throws ScriptingException;

	public ScriptingExecutor createScriptingExecutor(
		String language, boolean executeInSeparateThread);

	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String language, String script)
		throws ScriptingException;

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #eval(Set, Map,
	 *             Set, String, String)}
	 */
	@Deprecated
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String language, String script,
			String... servletContextNames)
		throws ScriptingException;

	public void exec(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			String language, String script)
		throws ScriptingException;

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #exec(Set, Map,
	 *             String, String)}
	 */
	@Deprecated
	public void exec(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			String language, String script, String... servletContextNames)
		throws ScriptingException;

	public Set<String> getSupportedLanguages();

}