/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.process;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class ConsumerOutputProcessorTest extends BaseOutputProcessorTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConsumeFail() {
		testFailToRead(new ConsumerOutputProcessor());
	}

	@Test
	public void testConsumeSuccess() throws ProcessException {
		ConsumerOutputProcessor consumerOutputProcessor =
			new ConsumerOutputProcessor();

		UnsyncByteArrayInputStream unsyncByteArrayInputStream =
			new UnsyncByteArrayInputStream(new byte[1024]);

		Assert.assertNull(
			consumerOutputProcessor.processStdErr(unsyncByteArrayInputStream));
		Assert.assertEquals(0, unsyncByteArrayInputStream.available());

		unsyncByteArrayInputStream = new UnsyncByteArrayInputStream(
			new byte[1024]);

		Assert.assertNull(
			consumerOutputProcessor.processStdOut(unsyncByteArrayInputStream));
		Assert.assertEquals(0, unsyncByteArrayInputStream.available());
	}

}