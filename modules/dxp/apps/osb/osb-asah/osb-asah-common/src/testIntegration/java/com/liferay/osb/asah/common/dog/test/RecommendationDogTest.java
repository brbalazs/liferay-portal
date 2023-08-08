/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.RecommendationDog;
import com.liferay.osb.asah.common.entity.ItemRecommendation;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ItemRecommendationRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Marcellus Tavares
 */
public class RecommendationDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = ItemRecommendationRepository.class,
		resourcePath = "osbasahfaroinfo/recommended_items_info.json"
	)
	@Test
	public void testDeleteItemRecommendationsByJobId() {
		_recommendationDog.deleteItemRecommendationsByJobId(1L);

		Page<ItemRecommendation> itemRecommendationPage =
			_recommendationDog.getItemRecommendationPage(
				1L, 0, 20, Sort.desc("id"));

		Assertions.assertEquals(0, itemRecommendationPage.getTotalElements());
	}

	@RepositoryResource(
		repositoryClass = ItemRecommendationRepository.class,
		resourcePath = "osbasahfaroinfo/recommended_items_info.json"
	)
	@Test
	public void testGetItemRecommendation() {
		ItemRecommendation itemRecommendation =
			_recommendationDog.getItemRecommendation(
				"fd98459b712bf8a15c6f9a71e4ae52f641b25eb2");

		Assertions.assertEquals(
			"https://www-prd.liferay.com/services/training/online",
			itemRecommendation.getItemId());
		Assertions.assertEquals(1, (long)itemRecommendation.getJobId());
		Assertions.assertEquals(
			Arrays.asList(
				"https://www-prd.liferay.com/blog",
				"https://www-prd.liferay.com/resource"),
			itemRecommendation.getRecommendedItemIds());
	}

	@RepositoryResource(
		repositoryClass = ItemRecommendationRepository.class,
		resourcePath = "osbasahfaroinfo/recommended_items_info.json"
	)
	@Test
	public void testGetItemRecommendationResultBag() {
		Page<ItemRecommendation> itemRecommendationPage =
			_recommendationDog.getItemRecommendationPage(
				1L, 0, 20, Sort.desc("id"));

		List<ItemRecommendation> itemRecommendations =
			itemRecommendationPage.getContent();

		Assertions.assertEquals(
			3, itemRecommendations.size(), itemRecommendations.toString());

		Assertions.assertEquals(3, itemRecommendationPage.getTotalElements());
	}

	@Autowired
	private RecommendationDog _recommendationDog;

}