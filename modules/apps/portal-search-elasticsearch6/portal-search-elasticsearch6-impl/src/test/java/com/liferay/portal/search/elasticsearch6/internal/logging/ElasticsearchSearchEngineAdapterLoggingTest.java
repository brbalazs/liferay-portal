/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch6.internal.logging;

import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.HealthExpectations;
import com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.ElasticsearchEngineAdapterFixture;
import com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search.CountSearchRequestExecutorImpl;
import com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search.MultisearchSearchRequestExecutorImpl;
import com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search.SearchSearchRequestExecutorImpl;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.test.util.logging.ExpectedLogTestRule;

import java.util.logging.Level;

import org.elasticsearch.cluster.health.ClusterHealthStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 * @author André de Oliveira
 */
public class ElasticsearchSearchEngineAdapterLoggingTest {

	@Before
	public void setUp() throws Exception {
		ElasticsearchFixture elasticsearchFixture = new ElasticsearchFixture(
			getClass());

		ElasticsearchEngineAdapterFixture elasticsearchEngineAdapterFixture =
			new ElasticsearchEngineAdapterFixture() {
				{
					setElasticsearchClientResolver(elasticsearchFixture);
				}
			};

		elasticsearchEngineAdapterFixture.setUp();

		elasticsearchFixture.setUp();

		waitForElasticsearchToStart(elasticsearchFixture);

		_elasticsearchFixture = elasticsearchFixture;

		_searchEngineAdapter =
			elasticsearchEngineAdapterFixture.getSearchEngineAdapter();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testCountSearchRequestExecutorLogs() {
		expectedLogTestRule.configure(
			CountSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		_searchEngineAdapter.execute(
			new CountSearchRequest() {
				{
					setIndexNames("_all");
					setQuery(new MatchAllQuery());
				}
			});
	}

	@Test
	public void testMultisearchSearchRequestExecutorLogs() {
		expectedLogTestRule.configure(
			MultisearchSearchRequestExecutorImpl.class, Level.FINE);

		expectedLogTestRule.expectMessage("The search engine processed");

		_searchEngineAdapter.execute(
			new MultisearchSearchRequest() {
				{
					addSearchSearchRequest(
						new SearchSearchRequest() {
							{
								setIndexNames("_all");
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

		_searchEngineAdapter.execute(
			new SearchSearchRequest() {
				{
					setIndexNames("_all");
					setQuery(new MatchAllQuery());
				}
			});
	}

	@Rule
	public ExpectedLogTestRule expectedLogTestRule = ExpectedLogTestRule.none();

	protected void waitForElasticsearchToStart(
		ElasticsearchFixture elasticsearchFixture) {

		elasticsearchFixture.getClusterHealthResponse(
			new HealthExpectations() {
				{
					setActivePrimaryShards(0);
					setActiveShards(0);
					setNumberOfDataNodes(1);
					setNumberOfNodes(1);
					setStatus(ClusterHealthStatus.GREEN);
					setUnassignedShards(0);
				}
			});
	}

	private ElasticsearchFixture _elasticsearchFixture;
	private SearchEngineAdapter _searchEngineAdapter;

}