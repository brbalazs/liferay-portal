/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.internal.batch;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class BatchIndexingHelperImplBulkSizesTest {

	@Test
	public void testConfiguration() {
		String entryClassName1 = RandomTestUtil.randomString();
		String entryClassName2 = RandomTestUtil.randomString();

		activate(entryClassName1 + "=200", entryClassName2 + "=500");

		assertBulkSize(200, entryClassName1);
		assertBulkSize(500, entryClassName2);
	}

	@Test
	public void testDefault() {
		activateWithoutConfiguration();

		assertBulkSize(10000, "com.liferay.journal.model.JournalArticle");

		assertBulkSize(10000, RandomTestUtil.randomString());
	}

	@Test
	public void testDefaultWithConfiguration() {
		activate("com.liferay.journal.model.JournalArticle=200");

		assertBulkSize(200, "com.liferay.journal.model.JournalArticle");

		assertBulkSize(10000, RandomTestUtil.randomString());
	}

	@Test
	public void testMalformed() {
		String entryClassName1 = RandomTestUtil.randomString();
		String entryClassName2 = RandomTestUtil.randomString();

		activate(
			entryClassName1 + "= ", StringPool.SPACE, entryClassName2 + "?200");

		assertBulkSize(10000, entryClassName1);
		assertBulkSize(10000, entryClassName2);
	}

	protected void activate(String... indexingBatchSizes) {
		_batchIndexingHelperImpl.activate(
			Collections.singletonMap(
				"indexingBatchSizes", Arrays.asList(indexingBatchSizes)));
	}

	protected void activateWithoutConfiguration() {
		_batchIndexingHelperImpl.activate(Collections.emptyMap());
	}

	protected void assertBulkSize(int bulkSize, String entryClassName) {
		Assert.assertEquals(
			bulkSize, _batchIndexingHelperImpl.getBulkSize(entryClassName));
	}

	private final BatchIndexingHelperImpl _batchIndexingHelperImpl =
		new BatchIndexingHelperImpl();

}