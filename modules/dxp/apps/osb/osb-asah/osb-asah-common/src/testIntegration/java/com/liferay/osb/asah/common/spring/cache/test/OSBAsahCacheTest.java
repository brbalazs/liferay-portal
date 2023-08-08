/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Inácio Nery
 */
public class OSBAsahCacheTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testKeyGenerator() throws Exception {
		Assertions.assertEquals(0, _bar.getIncrementCount());
		Assertions.assertEquals(10, _foo.getIncrementCount());
		Assertions.assertEquals(0, _bar.getIncrementCount());
		Assertions.assertEquals(10, _foo.getIncrementCount());
	}

	@Autowired
	private Bar _bar;

	@Autowired
	private Foo _foo;

}