/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.io;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class DummyOutputStreamTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testDummyOutputStream() {
		try (DummyOutputStream dummyOutputStream = new DummyOutputStream()) {
			dummyOutputStream.write(0);
			dummyOutputStream.write(new byte[0]);
			dummyOutputStream.write(new byte[0], 0, 0);

			dummyOutputStream.flush();
		}
	}

}