/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.InterestTopic;
import com.liferay.osb.asah.common.repository.InterestTopicRepository;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class InterestTopicDog {

	public List<InterestTopic> addInterestTopics(
		List<InterestTopic> interestTopics) {

		return IterableUtils.toList(
			_interestTopicRepository.saveAll(interestTopics));
	}

	public void deleteInterestTopics() {
		_interestTopicRepository.deleteAll();
	}

	public List<InterestTopic> getInterestTopics() {
		return IterableUtils.toList(_interestTopicRepository.findAll());
	}

	public List<InterestTopic> getInterestTopics(
		Integer termsPerTopic, String termType, List<Integer> topics) {

		return _interestTopicRepository.
			findTopInterestTopicsByTermRankLessThanEqualAndTermTypeAndTopicIn(
				termsPerTopic, termType, topics);
	}

	public Page<String> getInterestTopicTermsPage(
		int page, int size, List<String> termsExclude, String termType,
		List<Integer> topics) {

		List<String> topTerms =
			_interestTopicRepository.
				findTopTermsByTermRankBetweenAndTermNotInAndTermTypeAndTopicIn(
					_calculateTermRankStart(page, size, topics.size()),
					_calculateTermRankEnd(size, topics.size()), termsExclude,
					termType, topics);

		return PageableExecutionUtils.getPage(
			topTerms.subList(0, size),
			PageRequest.of(page, Math.min(size, topTerms.size())),
			() ->
				_interestTopicRepository.countByTermNotInAndTermTypeAndTopicIn(
					termsExclude, termType, topics));
	}

	public List<Integer> getInterestTopicTopics(
		List<String> terms, String termType, double termWeightThreshold) {

		return _interestTopicRepository.
			findTopicsByTermInAndTermTypeAndTermWeightGreaterThanEqual(
				terms, termType, termWeightThreshold);
	}

	private int _calculateTermRankEnd(int size, int topicsCount) {
		int defaultSize = 2;

		if ((topicsCount * defaultSize) < size) {
			return (int)Math.ceil((double)size / topicsCount);
		}

		return defaultSize;
	}

	private int _calculateTermRankStart(int page, int size, int topicsCount) {
		if (page > 0) {
			return (int)Math.ceil((double)(page * size) / topicsCount);
		}

		return 0;
	}

	@Autowired
	private InterestTopicRepository _interestTopicRepository;

}