/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting;

import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ToolDependencies;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Miguel Pastor
 */
public abstract class ScriptingExecutorTestCase {

	@BeforeClass
	public static void setUpClass() throws Exception {
		ToolDependencies.wireCaches();
	}

	public abstract String getScriptExtension();

	public abstract ScriptingExecutor getScriptingExecutor();

	@Before
	public void setUp() {
		_scriptingExecutor = getScriptingExecutor();
	}

	@Test
	public void testBindingInputVariables() throws Exception {
		Map<String, Object> inputObjects = new HashMap<>();

		inputObjects.put("variable", "string");

		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "binding-input");
	}

	@Test
	public void testSimpleScript() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "simple");
	}

	protected Map<String, Object> execute(
			Map<String, Object> inputObjects, Set<String> outputNames,
			String fileName)
		throws Exception {

		String script = getScript(fileName + getScriptExtension());

		return _scriptingExecutor.eval(null, inputObjects, outputNames, script);
	}

	protected String getScript(String name) throws IOException {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + name);

		return StringUtil.read(inputStream);
	}

	private ScriptingExecutor _scriptingExecutor;

}