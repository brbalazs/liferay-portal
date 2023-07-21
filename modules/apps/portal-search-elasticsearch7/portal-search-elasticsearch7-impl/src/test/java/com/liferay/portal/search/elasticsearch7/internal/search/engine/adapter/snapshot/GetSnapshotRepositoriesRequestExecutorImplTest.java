/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.snapshot;

import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.index.AnalyzeIndexRequestExecutorTest;
import com.liferay.portal.search.engine.adapter.snapshot.GetSnapshotRepositoriesRequest;

import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequestBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class GetSnapshotRepositoriesRequestExecutorImplTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			AnalyzeIndexRequestExecutorTest.class.getSimpleName());

		_elasticsearchFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testGetSnapshotRepositoriesRequestBuilder() {
		GetSnapshotRepositoriesRequest getSnapshotRepositoriesRequest =
			new GetSnapshotRepositoriesRequest("repository1", "repository2");

		GetSnapshotRepositoriesRequestExecutorImpl
			getSnapshotRepositoriesRequestExecutorImpl =
				new GetSnapshotRepositoriesRequestExecutorImpl() {
					{
						setElasticsearchClientResolver(_elasticsearchFixture);
					}
				};

		GetRepositoriesRequestBuilder getRepositoriesRequestBuilder =
			getSnapshotRepositoriesRequestExecutorImpl.
				createGetRepositoriesRequestBuilder(
					getSnapshotRepositoriesRequest);

		GetRepositoriesRequest getRepositoriesRequest =
			getRepositoriesRequestBuilder.request();

		Assert.assertArrayEquals(
			getSnapshotRepositoriesRequest.getRepositoryNames(),
			getRepositoriesRequest.repositories());
	}

	private ElasticsearchFixture _elasticsearchFixture;

}