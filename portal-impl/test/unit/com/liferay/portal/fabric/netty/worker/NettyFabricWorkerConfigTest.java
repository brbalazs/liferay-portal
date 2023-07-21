/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.fabric.netty.worker;

import com.liferay.petra.process.ProcessCallable;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.process.ProcessException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.fabric.ReturnProcessCallable;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.util.SerializableUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricWorkerConfigTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() throws ProcessException {
		long id = 10;

		try {
			new NettyFabricWorkerConfig<String>(id, null, null, null);

			Assert.fail();
		}
		catch (NullPointerException npe) {
			Assert.assertEquals("Process config is null", npe.getMessage());
		}

		ProcessConfig.Builder builder = new ProcessConfig.Builder();

		ProcessConfig processConfig = builder.build();

		try {
			new NettyFabricWorkerConfig<String>(0, processConfig, null, null);

			Assert.fail();
		}
		catch (NullPointerException npe) {
			Assert.assertEquals("Process callable is null", npe.getMessage());
		}

		ProcessCallable<String> processCallable = new ReturnProcessCallable<>(
			StringPool.BLANK);

		try {
			new NettyFabricWorkerConfig<String>(
				0, processConfig, processCallable, null);

			Assert.fail();
		}
		catch (NullPointerException npe) {
			Assert.assertEquals("Input path map is null", npe.getMessage());
		}

		NettyFabricWorkerConfig<String> nettyFabricWorkerConfig =
			new NettyFabricWorkerConfig<>(
				id, processConfig, processCallable,
				Collections.<Path, Path>emptyMap());

		Assert.assertEquals(id, nettyFabricWorkerConfig.getId());
		Assert.assertEquals(
			Collections.emptyMap(), nettyFabricWorkerConfig.getInputPathMap());
		Assert.assertSame(
			processConfig, nettyFabricWorkerConfig.getProcessConfig());

		ProcessCallable<String> nettyFabricWorkerProcessCallable =
			nettyFabricWorkerConfig.getProcessCallable();

		Assert.assertNotSame(processCallable, nettyFabricWorkerProcessCallable);
		Assert.assertEquals(
			processCallable.toString(),
			nettyFabricWorkerProcessCallable.toString());
		Assert.assertEquals(
			processCallable.call(), nettyFabricWorkerProcessCallable.call());
	}

	@Test
	public void testSerialization() throws ProcessException {
		ProcessConfig.Builder builder = new ProcessConfig.Builder();

		List<String> arguments = Arrays.asList("x", "y", "z");

		builder.setArguments(arguments);

		String bootstrapClassPath = "bootstrapClassPath";

		builder.setBootstrapClassPath(bootstrapClassPath);

		String javaExecutable = "java";

		builder.setJavaExecutable(javaExecutable);

		builder.setReactClassLoader(
			NettyFabricWorkerConfigTest.class.getClassLoader());

		String runtimeClassPath = "runtimeClassPath";

		builder.setRuntimeClassPath(runtimeClassPath);

		long id = 10;

		ProcessCallable<String> processCallable = new ReturnProcessCallable<>(
			"Test ProcessCallable");

		Map<Path, Path> inputPathMap = new HashMap<>();

		inputPathMap.put(Paths.get("path1"), Paths.get("path2"));
		inputPathMap.put(Paths.get("path3"), Paths.get("path4"));

		NettyFabricWorkerConfig<String> copyNettyFabricWorkerConfig =
			(NettyFabricWorkerConfig<String>)SerializableUtil.deserialize(
				SerializableUtil.serialize(
					new NettyFabricWorkerConfig<String>(
						id, builder.build(), processCallable, inputPathMap)));

		Assert.assertEquals(id, copyNettyFabricWorkerConfig.getId());
		Assert.assertEquals(
			inputPathMap, copyNettyFabricWorkerConfig.getInputPathMap());

		ProcessConfig copyProcessConfig =
			copyNettyFabricWorkerConfig.getProcessConfig();

		Assert.assertEquals(arguments, copyProcessConfig.getArguments());
		Assert.assertEquals(
			bootstrapClassPath, copyProcessConfig.getBootstrapClassPath());
		Assert.assertEquals(
			javaExecutable, copyProcessConfig.getJavaExecutable());
		Assert.assertNull(copyProcessConfig.getReactClassLoader());
		Assert.assertEquals(
			runtimeClassPath, copyProcessConfig.getRuntimeClassPath());

		ProcessCallable<String> copyProcessCallable =
			copyNettyFabricWorkerConfig.getProcessCallable();

		Assert.assertEquals(processCallable.call(), copyProcessCallable.call());
	}

}