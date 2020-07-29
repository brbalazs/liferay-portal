/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.elasticsearch7.internal.mappings;

import com.liferay.portal.search.elasticsearch7.internal.LiferayElasticsearchIndexingFixtureFactory;
import com.liferay.portal.search.engine.SearchEngineInformation;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.mappings.BaseLiferayFieldQueryFactoryTestCase;

import org.junit.Before;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class LiferayFieldQueryFactoryTest
	extends BaseLiferayFieldQueryFactoryTestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();

		searchEngineInformation = Mockito.mock(SearchEngineInformation.class);

		Mockito.when(
			searchEngineInformation.getVendorString()
		).thenReturn(
			"Elasticsearch"
		);

		Mockito.when(
			searchEngineInformation.getClientVersionString()
		).thenReturn(
			"7"
		);
	}

	@Override
	protected IndexingFixture createIndexingFixture() {
		return LiferayElasticsearchIndexingFixtureFactory.getInstance();
	}

}