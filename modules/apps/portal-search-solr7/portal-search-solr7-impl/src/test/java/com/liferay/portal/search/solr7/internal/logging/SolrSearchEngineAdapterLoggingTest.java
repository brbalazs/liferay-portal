/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr7.internal.logging;

import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.solr7.internal.SolrIndexingFixture;
import com.liferay.portal.search.solr7.internal.search.engine.adapter.search.CountSearchRequestExecutorImpl;
import com.liferay.portal.search.solr7.internal.search.engine.adapter.search.SearchSearchRequestExecutorImpl;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.logging.ExpectedLogTestRule;

import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Bryan Engler
 */
public class SolrSearchEngineAdapterLoggingTest extends BaseIndexingTestCase {

	@Test
	public void testCountSearchRequestExecutorLogs() {
		expectedLogTestRule.configure(
			CountSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		searchEngineAdapter.execute(
			new CountSearchRequest() {
				{
					setIndexNames("liferay");
					setQuery(new MatchAllQuery());
				}
			});
	}

	@Test
	public void testMultisearchSearchRequestExecutorLogs() {
		expectedException.expect(UnsupportedOperationException.class);

		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		searchEngineAdapter.execute(
			new MultisearchSearchRequest() {
				{
					addSearchSearchRequest(
						new SearchSearchRequest() {
							{
								setIndexNames("liferay");
								setQuery(new MatchAllQuery());
							}
						});
				}
			});
	}

	@Test
	public void testSearchSearchRequestExecutorLogs() {
		expectedLogTestRule.configure(
			SearchSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		searchEngineAdapter.execute(
			new SearchSearchRequest() {
				{
					setIndexNames("liferay");
					setQuery(new MatchAllQuery());
				}
			});
	}

	@Test
	public void testSearchSearchRequestWithPortalSearchQuery() {
		SearchEngineAdapter searchEngineAdapter = getSearchEngineAdapter();

		searchEngineAdapter.execute(
			new SearchSearchRequest() {
				{
					setIndexNames("liferay");
					setQuery(queries.matchAll());
				}
			});
	}

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Rule
	public ExpectedLogTestRule expectedLogTestRule = ExpectedLogTestRule.none();

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return new SolrIndexingFixture();
	}

}