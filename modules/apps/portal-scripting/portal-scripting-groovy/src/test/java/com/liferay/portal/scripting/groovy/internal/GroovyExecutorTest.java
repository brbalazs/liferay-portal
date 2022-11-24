/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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