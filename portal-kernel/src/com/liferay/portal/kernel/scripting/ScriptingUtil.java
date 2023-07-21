/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.scripting;

import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Alberto Montero
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class ScriptingUtil {

	public static void clearCache(String language) throws ScriptingException {
		_getScripting().clearCache(language);
	}

	public static ScriptingExecutor createScriptingExecutor(
		String language, boolean executeInSeparateThread) {

		return _getScripting().createScriptingExecutor(
			language, executeInSeparateThread);
	}

	public static Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String language, String script)
		throws ScriptingException {

		return _getScripting().eval(
			allowedClasses, inputObjects, outputNames, language, script);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #eval(Set, Map,
	 *             Set, String, String)}
	 */
	@Deprecated
	public static Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String language, String script,
			String... servletContextNames)
		throws ScriptingException {

		return _getScripting().eval(
			allowedClasses, inputObjects, outputNames, language, script,
			servletContextNames);
	}

	public static void exec(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			String language, String script)
		throws ScriptingException {

		_getScripting().exec(allowedClasses, inputObjects, language, script);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #exec(Set, Map,
	 *             String, String)}
	 */
	@Deprecated
	public static void exec(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			String language, String script, String... servletContextNames)
		throws ScriptingException {

		_getScripting().exec(
			allowedClasses, inputObjects, language, script,
			servletContextNames);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link #_getScripting()}
	 */
	@Deprecated
	public static Scripting getScripting() {
		return _getScripting();
	}

	public static Set<String> getSupportedLanguages() {
		return _getScripting().getSupportedLanguages();
	}

	private static Scripting _getScripting() {
		return _scripting;
	}

	private static volatile Scripting _scripting =
		ServiceProxyFactory.newServiceTrackedInstance(
			Scripting.class, ScriptingUtil.class, "_scripting", false);

}