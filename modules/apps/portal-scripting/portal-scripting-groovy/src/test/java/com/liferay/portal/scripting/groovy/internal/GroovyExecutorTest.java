/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.scripting.groovy.internal;

import com.liferay.portal.kernel.io.Deserializer;
import com.liferay.portal.kernel.io.Serializer;
import com.liferay.portal.kernel.scripting.ScriptingException;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.scripting.ScriptingExecutorTestCase;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Pastor
 */
@RunWith(PowerMockRunner.class)
public class GroovyExecutorTest extends ScriptingExecutorTestCase {

	@Override
	public String getScriptExtension() {
		return ".groovy";
	}

	@Override
	public ScriptingExecutor getScriptingExecutor() {
		return new GroovyExecutor();
	}

	@Test
	public void testGroovyRuntimeExceptionSerialization() throws Exception {
		try {
			execute(
				Collections.emptyMap(), Collections.emptySet(),
				"missing-method");

			Assert.fail();
		}
		catch (ScriptingException scriptingException) {
			Assert.assertEquals(
				"No signature of method: static Test.missingMethod() is " +
					"applicable for argument types: () values: []",
				scriptingException.getMessage());

			_writeAndReadObject(scriptingException);
		}
	}

	@Test(expected = RuntimeException.class)
	public void testRuntimeError() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "runtime-error");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSyntaxError() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "syntax-error");
	}

	private void _writeAndReadObject(Exception exception) throws Exception {
		Serializer serializer = new Serializer();

		serializer.writeObject(exception);

		Deserializer deserializer = new Deserializer(serializer.toByteBuffer());

		deserializer.readObject();
	}

}