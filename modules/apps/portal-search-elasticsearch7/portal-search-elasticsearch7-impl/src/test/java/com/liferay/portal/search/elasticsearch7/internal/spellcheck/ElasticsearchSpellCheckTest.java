/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.spellcheck;

import com.liferay.portal.search.elasticsearch7.internal.ElasticsearchIndexingFixtureFactory;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;
import com.liferay.portal.search.test.util.spellcheck.BaseSpellCheckTestCase;

/**
 * @author André de Oliveira
 */
public class ElasticsearchSpellCheckTest extends BaseSpellCheckTestCase {

	@Override
	protected IndexingFixture createIndexingFixture() {
		return ElasticsearchIndexingFixtureFactory.getInstance();
	}

}