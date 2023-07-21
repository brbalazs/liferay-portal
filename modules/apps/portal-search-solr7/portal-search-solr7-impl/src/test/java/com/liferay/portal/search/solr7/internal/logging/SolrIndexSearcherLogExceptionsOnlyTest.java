/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr7.internal.logging;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.search.solr7.internal.SolrIndexingFixture;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.logging.ExpectedLogTestRule;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bryan Engler
 */
public class SolrIndexSearcherLogExceptionsOnlyTest
	extends BaseIndexingTestCase {

	@Test
	public void testExceptionOnlyLoggedWhenQueryMalformedSearch() {
		expectedLogTestRule.expectMessage("Cannot parse '+f^eld:text'");

		search(createSearchContext(), getMalformedQuery());
	}

	@Test
	public void testExceptionOnlyLoggedWhenQueryMalformedSearchCount() {
		expectedLogTestRule.expectMessage("Cannot parse '+f^eld:text'");

		searchCount(createSearchContext(), getMalformedQuery());
	}

	@Rule
	public ExpectedLogTestRule expectedLogTestRule = ExpectedLogTestRule.none();

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		Map<String, Object> solrConfigurationProperties =
			HashMapBuilder.<String, Object>put(
				"logExceptionsOnly", true
			).build();

		return new SolrIndexingFixture(solrConfigurationProperties);
	}

	protected Query getMalformedQuery() {
		BooleanQueryImpl booleanQueryImpl = new BooleanQueryImpl();

		booleanQueryImpl.add(
			new TermQueryImpl("f^eld", "text"), BooleanClauseOccur.MUST);

		return booleanQueryImpl;
	}

}