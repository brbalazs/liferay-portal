/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.process;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.util.Random;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class CollectorOutputProcessorTest extends BaseOutputProcessorTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testCollectFail() {
		testFailToRead(new CollectorOutputProcessor());
	}

	@Test
	public void testCollectSuccess() throws ProcessException {
		CollectorOutputProcessor collectorOutputProcessor =
			new CollectorOutputProcessor();

		Random random = new Random();

		byte[] stdErrData = new byte[1024];

		random.nextBytes(stdErrData);

		Assert.assertArrayEquals(
			stdErrData,
			collectorOutputProcessor.processStdErr(
				new UnsyncByteArrayInputStream(stdErrData)));

		byte[] stdOutData = new byte[1024];

		random.nextBytes(stdOutData);

		Assert.assertArrayEquals(
			stdOutData,
			collectorOutputProcessor.processStdErr(
				new UnsyncByteArrayInputStream(stdOutData)));
	}

}