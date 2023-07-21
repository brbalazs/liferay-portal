/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr8.internal.logging;

import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.search.solr8.internal.SolrIndexSearcher;
import com.liferay.portal.search.solr8.internal.SolrIndexingFixture;
import com.liferay.portal.search.solr8.internal.search.engine.adapter.search.CountSearchRequestExecutorImpl;
import com.liferay.portal.search.solr8.internal.search.engine.adapter.search.SearchSearchRequestExecutorImpl;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.logging.ExpectedLogTestRule;

import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexSearcherLoggingTest extends BaseIndexingTestCase {

	@Test
	public void testCountSearchRequestExecutorLogsViaIndexer() {
		expectedLogTestRule.configure(
			CountSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		searchCount(createSearchContext(), new MatchAllQuery());
	}

	@Test
	public void testIndexerSearchCountLogs() {
		expectedLogTestRule.configure(SolrIndexSearcher.class, Level.INFO);

		expectedLogTestRule.expectMessage("The search engine processed");

		searchCount(createSearchContext(), new MatchAllQuery());
	}

	@Test
	public void testIndexerSearchLogs() {
		expectedLogTestRule.configure(SolrIndexSearcher.class, Level.INFO);

		expectedLogTestRule.expectMessage("The search engine processed");

		search(createSearchContext());
	}

	@Test
	public void testSearchSearchRequestExecutorLogsViaIndexer() {
		expectedLogTestRule.configure(
			SearchSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		search(createSearchContext());
	}

	@Rule
	public ExpectedLogTestRule expectedLogTestRule = ExpectedLogTestRule.none();

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

}