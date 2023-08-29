/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.BQIdentityInterestScoreDog;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.model.IdentityInterestScore;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Marcellus Tavares
 */
public class BQIdentityInterestScoreDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "test_get_bq_identity_interest_scores_page.sql"
	)
	@Test
	public void testGetBQIdentityInterestScorePage() {
		Page<BQIdentityInterestScore> interestPage =
			_bqIdentityInterestScoreDog.getBQIdentityInterestScorePage(
				null, "374790572703144534", 20, 0);

		Assertions.assertEquals(1, interestPage.getTotalElements());

		List<BQIdentityInterestScore> bqIdentityInterestScores =
			interestPage.getContent();

		BQIdentityInterestScore bqIdentityInterestScore =
			bqIdentityInterestScores.get(0);

		Assertions.assertEquals(
			"compelling metrics", bqIdentityInterestScore.getKeyword());
	}

	@BQSQLResource(
		resourcePath = "osbasahfaroinfo/bq_identity_interest_page.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_info.json"
	)
	@Test
	public void testGetBQIdentityInterestScorePageByFilterString() {
		Page<IdentityInterestScore> individualInterestScorePage =
			_bqIdentityInterestScoreDog.getIdentityInterestScorePage(
				null, null, null, 0, 20, new String[] {"keyword,ASC"});

		Assertions.assertEquals(
			2, individualInterestScorePage.getTotalElements());

		List<IdentityInterestScore> individualInterestScores =
			individualInterestScorePage.getContent();

		IdentityInterestScore identityInterestScore1 =
			individualInterestScores.get(0);

		Assertions.assertEquals("java", identityInterestScore1.getKeyword());
		Assertions.assertEquals(
			1L, identityInterestScore1.getContributingPagesCount());

		IdentityInterestScore identityInterestScore2 =
			individualInterestScores.get(1);

		Assertions.assertEquals(
			"javascript", identityInterestScore2.getKeyword());
		Assertions.assertEquals(
			2L, identityInterestScore2.getContributingPagesCount());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_identity_interest_scores_page_with_suppression.sql"
	)
	@Test
	public void testGetBQIdentityInterestScorePageWithSuppression() {
		Page<BQIdentityInterestScore> interestPage =
			_bqIdentityInterestScoreDog.getBQIdentityInterestScorePage(
				null,
				"c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7" +
					"e485",
				20, 0);

		Assertions.assertEquals(0, interestPage.getTotalElements());

		List<BQIdentityInterestScore> bqIdentityInterestScores =
			interestPage.getContent();

		Assertions.assertTrue(bqIdentityInterestScores.isEmpty());
	}

	@Autowired
	private BQIdentityInterestScoreDog _bqIdentityInterestScoreDog;

}