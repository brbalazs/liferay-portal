/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.dog.InterestCompositionDog;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matthew Kong
 */
public class InterestCompositionDogTest extends BaseCompositionDogTestCase {

	@BQSQLResource(
		resourcePath = "bq_identity_interest_score_identity_activities.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores_info.json"
	)
	@Test
	public void testGetIndividualCompositionResultBag() {
		checkResults(
			_interestCompositionDog.getIndividualCompositionResultBag(
				1L, "e", 1, Sort.asc("count"), 1),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling metrics", 2L);
				}
			},
			2, 2, 5);

		checkResults(
			_interestCompositionDog.getIndividualCompositionResultBag(
				null, "e", 1, Sort.asc("count"), 1),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling metrics", 2L);
				}
			},
			2, 2, 5);
	}

	@BQSQLResource(
		resourcePath = "bq_identity_interest_score_identity_activities.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores_info.json"
	)
	@Test
	public void testGetIndividualCompositionResultBagWithOrderByAsc() {
		checkResults(
			_interestCompositionDog.getIndividualCompositionResultBag(
				1L, null, 10, Sort.asc("name"), 0),
			new LinkedHashMap<String, Long>() {
				{
					put("clicks-and-mortar e-tailers", 2L);
					put("compelling metrics", 2L);
					put("javascript", 1L);
				}
			},
			2, 3, 5);
	}

	@BQSQLResource(
		resourcePath = "bq_identity_interest_score_identity_activities.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores_info.json"
	)
	@Test
	public void testGetIndividualSegmentCompositionResultBagWithKeyword() {
		checkResults(
			_interestCompositionDog.getIndividualSegmentCompositionResultBag(
				Boolean.TRUE, 1L, "compel", 366637689379787789L, 10,
				Sort.desc("count"), 0),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling metrics", 2L);
				}
			},
			2, 1, 2);
	}

	@BQSQLResource(
		resourcePath = "bq_identity_interest_score_identity_activities.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores_info.json"
	)
	@Test
	public void testGetIndividualSegmentCompositionResultBagWithSortAsc() {
		checkResults(
			_interestCompositionDog.getIndividualSegmentCompositionResultBag(
				Boolean.TRUE, 1L, null, 366637689379787789L, 10,
				Sort.asc("count"), 0),
			new LinkedHashMap<String, Long>() {
				{
					put("javascript", 1L);
					put("clicks-and-mortar e-tailers", 2L);
					put("compelling metrics", 2L);
				}
			},
			2, 3, 2);
	}

	@BQSQLResource(
		resourcePath = "bq_identity_interest_score_identity_activities.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores_info.json"
	)
	@Test
	public void testGetIndividualSegmentCompositionResultBagWithSortName() {
		checkResults(
			_interestCompositionDog.getIndividualSegmentCompositionResultBag(
				Boolean.TRUE, 1L, null, 366637689379787789L, 10,
				Sort.asc("name"), 0),
			new LinkedHashMap<String, Long>() {
				{
					put("clicks-and-mortar e-tailers", 2L);
					put("compelling metrics", 2L);
					put("javascript", 1L);
				}
			},
			2, 3, 2);
	}

	@Autowired
	private InterestCompositionDog _interestCompositionDog;

}