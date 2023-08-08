/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.UserDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author André Miranda
 */
public class UserDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_get_anonymous_users_count.sql")
	@Test
	public void testGetAnonymousUsersCount() {
		Assertions.assertEquals(
			2,
			_userDog.getAnonymousUsersCount(
				JournalMetricType.VIEWS, _searchQueryContext));
	}

	@BQSQLResource(resourcePath = "test_get_known_users_count.sql")
	@Test
	public void testGetKnownUsersCount() {
		Assertions.assertEquals(
			5,
			_userDog.getKnownUsersCount(
				JournalMetricType.VIEWS, _searchQueryContext));
	}

	@BQSQLResource(resourcePath = "test_get_nonsegmented_individuals_count.sql")
	@Test
	public void testGetNonsegmentedIndividualsCount() {
		Assertions.assertEquals(
			2,
			_userDog.getNonsegmentedIndividualsCount(
				JournalMetricType.VIEWS, _searchQueryContext));
	}

	@BQSQLResource(resourcePath = "test_get_segmented_individuals_count.sql")
	@Test
	public void testGetSegmentedIndividualsCount() {
		Assertions.assertEquals(
			3,
			_userDog.getSegmentedIndividualsCount(
				JournalMetricType.VIEWS, _searchQueryContext));
	}

	private final SearchQueryContext _searchQueryContext =
		new SearchQueryContext("1", AssetType.JOURNAL) {
			{
				setChannelId(1L);
				setTimeRange(TimeRange.LAST_7_DAYS);
			}
		};

	@Autowired
	private UserDog _userDog;

}