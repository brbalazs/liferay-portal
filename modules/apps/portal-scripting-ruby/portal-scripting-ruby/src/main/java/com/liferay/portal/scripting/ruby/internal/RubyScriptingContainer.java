/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.ruby.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.scripting.ScriptingContainer;
import com.liferay.portal.kernel.scripting.ScriptingException;

import org.jruby.RubyArray;
import org.jruby.RubyException;
import org.jruby.exceptions.RaiseException;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * @author Michael C. Han
 */
public class RubyScriptingContainer
	implements ScriptingContainer<org.jruby.embed.ScriptingContainer> {

	public RubyScriptingContainer(
		org.jruby.embed.ScriptingContainer scriptingContainer) {

		_scriptingContainer = scriptingContainer;
	}

	@Override
	public <T> T callMethod(
			Object scriptObject, String methodName, Object[] arguments,
			Class<T> returnClass)
		throws ScriptingException {

		try {
			return _scriptingContainer.callMethod(
				scriptObject, methodName, arguments, returnClass);
		}
		catch (RaiseException re) {
			RubyException rubyException = re.getException();

			IRubyObject iRubyObject = rubyException.getBacktrace();

			RubyArray rubyArray = (RubyArray)iRubyObject.toJava(
				RubyArray.class);

			StringBuilder sb = new StringBuilder((2 * rubyArray.size()) + 2);

			sb.append(String.valueOf(rubyException.toJava(String.class)));
			sb.append(StringPool.NEW_LINE);

			for (Object object : rubyArray) {
				sb.append(String.valueOf(object));
				sb.append(StringPool.NEW_LINE);
			}

			throw new ScriptingException(sb.toString(), re);
		}
	}

	@Override
	public void destroy() {
		_scriptingContainer.terminate();
	}

	@Override
	public org.jruby.embed.ScriptingContainer getWrappedScriptingContainer() {
		return _scriptingContainer;
	}

	@Override
	public Object runScriptlet(String scriptlet) {
		return _scriptingContainer.runScriptlet(scriptlet);
	}

	@Override
	public void setCurrentDirName(String currentDirName) {
		_scriptingContainer.setCurrentDirectory(currentDirName);
	}

	private final org.jruby.embed.ScriptingContainer _scriptingContainer;

}