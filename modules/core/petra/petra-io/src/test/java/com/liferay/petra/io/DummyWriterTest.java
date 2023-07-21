/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.io;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class DummyWriterTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testDummyOutputStream() {
		try (DummyWriter dummyWriter = new DummyWriter()) {
			Assert.assertSame(dummyWriter, dummyWriter.append('a'));
			Assert.assertSame(dummyWriter, dummyWriter.append("test"));
			Assert.assertSame(dummyWriter, dummyWriter.append("test", 0, 0));

			dummyWriter.write('a');
			dummyWriter.write(new char[0]);
			dummyWriter.write(new char[0], 0, 0);
			dummyWriter.write("test");
			dummyWriter.write("test", 0, 0);

			dummyWriter.flush();
		}
	}

}