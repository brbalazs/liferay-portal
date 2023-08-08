/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.InterestTopic;
import com.liferay.osb.asah.common.repository.InterestTopicRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Leilany Ulisses
 */
@Import(JDBCTestConfiguration.class)
public class InterestTopicRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		InterestTopic interestTopic1 = new InterestTopic();

		interestTopic1.setId(545783521905348140L);
		interestTopic1.setIsNew(Boolean.TRUE);
		interestTopic1.setTerm("javascript");
		interestTopic1.setTermType("keyword");
		interestTopic1.setTermWeight(0.011695906432748537);
		interestTopic1.setTopic(1);
		interestTopic1.setTopicWeight(0.07511374008317741);

		_interestTopicRepository.save(interestTopic1);

		InterestTopic interestTopic2 = new InterestTopic();

		interestTopic2.setId(545783521894161908L);
		interestTopic2.setIsNew(Boolean.TRUE);
		interestTopic2.setTerm("objective-c");
		interestTopic2.setTermType("keyword");
		interestTopic2.setTermWeight(0.07818930041152264);
		interestTopic2.setTopic(2);
		interestTopic2.setTopicWeight(0.050497765526731);

		_interestTopicRepository.save(interestTopic2);
	}

	@AfterEach
	public void tearDown() {
		_interestTopicRepository.deleteAll();
	}

	@Test
	public void testFindTopicsByTermInAndTermTypeAndTermWeightGreaterThanEqual() {
		List<Integer> interestTopics =
			_interestTopicRepository.
				findTopicsByTermInAndTermTypeAndTermWeightGreaterThanEqual(
					Arrays.asList("javascript", "objective-c"), "keyword",
					0.011695906432748537);

		Assertions.assertEquals(2, interestTopics.size());
	}

	@Test
	public void testFindTopInterestTopicsByTermRankLessThanEqualAndTermTypeAndTopicIn() {
		List<InterestTopic> interests =
			_interestTopicRepository.
				findTopInterestTopicsByTermRankLessThanEqualAndTermTypeAndTopicIn(
					2, "keyword", Arrays.asList(2, 1));

		Assertions.assertEquals(2, interests.size());
	}

	@Test
	public void testFindTopTermsByTermRankBetweenAndTermsNotInAndTermTypeAndTopicIn() {
		List<String> interestTerms =
			_interestTopicRepository.
				findTopTermsByTermRankBetweenAndTermNotInAndTermTypeAndTopicIn(
					0, 2, Arrays.asList("objective-c"), "keyword",
					Arrays.asList(2, 1));

		Assertions.assertEquals("javascript", interestTerms.get(0));
	}

	@Autowired
	private InterestTopicRepository _interestTopicRepository;

}