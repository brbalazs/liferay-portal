/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch7.internal.filter.groupid;

import com.liferay.portal.search.elasticsearch7.internal.LiferayElasticsearchIndexingFixtureFactory;
import com.liferay.portal.search.test.util.filter.groupid.BaseGroupIdQueryPreFilterContributorTestCase;
import com.liferay.portal.search.test.util.indexing.IndexingFixture;

/**
 * @author Tibor Lipusz
 */
public class GroupIdQueryPreFilterContributorTest
	extends BaseGroupIdQueryPreFilterContributorTestCase {

	@Override
	protected IndexingFixture createIndexingFixture() throws Exception {
		return LiferayElasticsearchIndexingFixtureFactory.getInstance();
	}

}