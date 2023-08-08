/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.InterestTopic;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcellus Tavares
 */
public interface InterestTopicRepository
	extends Repository<InterestTopic, Long> {

	@Cacheable
	public long countByTermNotInAndTermTypeAndTopicIn(
		List<String> termsExclude, String termType, List<Integer> topics);

	@Cacheable
	public List<Integer>
		findTopicsByTermInAndTermTypeAndTermWeightGreaterThanEqual(
			@Param("terms") List<String> terms,
			@Param("termType") String termType,
			@Param("termWeight") Double termWeight);

	@Cacheable
	public List<InterestTopic>
		findTopInterestTopicsByTermRankLessThanEqualAndTermTypeAndTopicIn(
			@Param("termRankEnd") Integer termRankEnd,
			@Param("termType") String termType,
			@Param("topics") List<Integer> topics);

	@Cacheable
	public List<String>
		findTopTermsByTermRankBetweenAndTermNotInAndTermTypeAndTopicIn(
			@Param("termRank1") Integer termRank1,
			@Param("termRank2") Integer termRank2,
			@Param("terms") List<String> terms,
			@Param("termType") String termType,
			@Param("topics") List<Integer> topics);

}