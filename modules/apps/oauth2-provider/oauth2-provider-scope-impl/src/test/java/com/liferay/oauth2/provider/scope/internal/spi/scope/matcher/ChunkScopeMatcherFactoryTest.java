/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.internal.spi.scope.matcher;

import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Carlos Sierra Andrés
 */
public class ChunkScopeMatcherFactoryTest {

	@Test
	public void testMatch() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher = chunkScopeMatcherFactory.create(
			"everything.readonly");

		Assert.assertTrue(scopeMatcher.match("everything.readonly"));
		Assert.assertFalse(scopeMatcher.match("everything"));
	}

	@Test
	public void testMatch2() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher = chunkScopeMatcherFactory.create(
			"everything");

		Assert.assertTrue(scopeMatcher.match("everything.readonly"));
		Assert.assertTrue(scopeMatcher.match("everything"));
	}

	@Test
	public void testMatchMatchesWholeChunksOnly() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher = chunkScopeMatcherFactory.create(
			"everything");

		Assert.assertFalse(scopeMatcher.match("everything2.readonly"));
		Assert.assertFalse(scopeMatcher.match("everything2"));
	}

}