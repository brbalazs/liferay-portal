/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr7.internal.logging;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.solr7.internal.SolrIndexingFixture;
import com.liferay.portal.search.solr7.internal.search.engine.adapter.document.BulkDocumentRequestExecutorImpl;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.logging.ExpectedLogTestRule;

import java.util.Collections;
import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexWriterLoggingTest extends BaseIndexingTestCase {

	@Test
	public void testBulkDocumentRequestExecutorLogs() {
		expectedLogTestRule.configure(
			BulkDocumentRequestExecutorImpl.class, Level.INFO);

		expectedLogTestRule.expectMessage("response={responseHeader={status=0");

		IndexWriter indexWriter = getIndexWriter();

		try {
			indexWriter.addDocuments(
				createSearchContext(),
				Collections.singletonList(getTestDocument()));
		}
		catch (SearchException searchException) {
		}
	}

	@Rule
	public ExpectedLogTestRule expectedLogTestRule = ExpectedLogTestRule.none();

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

	protected Document getTestDocument() {
		Document document = new DocumentImpl();

		document.addUID(
			RandomTestUtil.randomString(), RandomTestUtil.randomLong());

		return document;
	}

}