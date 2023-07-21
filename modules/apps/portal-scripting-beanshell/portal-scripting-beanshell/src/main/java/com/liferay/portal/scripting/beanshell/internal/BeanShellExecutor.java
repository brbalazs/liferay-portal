/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.beanshell.internal;

import bsh.Interpreter;

import com.liferay.portal.kernel.scripting.ExecutionException;
import com.liferay.portal.kernel.scripting.ScriptingException;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.scripting.BaseScriptingExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Component(
	immediate = true,
	property = "scripting.language=" + BeanShellExecutor.LANGUAGE,
	service = ScriptingExecutor.class
)
@Deprecated
public class BeanShellExecutor extends BaseScriptingExecutor {

	public static final String LANGUAGE = "beanshell";

	@Override
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String script)
		throws ScriptingException {

		if (allowedClasses != null) {
			throw new ExecutionException(
				"Constrained execution not supported for BeanShell");
		}

		try {
			Interpreter interpreter = new Interpreter();

			for (Map.Entry<String, Object> entry : inputObjects.entrySet()) {
				interpreter.set(entry.getKey(), entry.getValue());
			}

			interpreter.setClassLoader(getClassLoader());

			interpreter.eval(script);

			if (outputNames == null) {
				return null;
			}

			Map<String, Object> outputObjects = new HashMap<>();

			for (String outputName : outputNames) {
				outputObjects.put(outputName, interpreter.get(outputName));
			}

			return outputObjects;
		}
		catch (Exception e) {
			throw new ScriptingException(e.getMessage(), e);
		}
	}

	@Override
	public String getLanguage() {
		return LANGUAGE;
	}

	@Override
	public ScriptingExecutor newInstance(boolean executeInSeparateThread) {
		return new BeanShellExecutor();
	}

}