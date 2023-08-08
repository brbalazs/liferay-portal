/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.ItemRecommendation;
import com.liferay.osb.asah.common.repository.ItemRecommendationRepository;
import com.liferay.osb.asah.common.repository.JobRunRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leilany Ulisses
 */
@Import(JDBCTestConfiguration.class)
public class ItemRecommendationRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		ItemRecommendation itemRecommendation = new ItemRecommendation();

		itemRecommendation.setId("fd98459b712bf8a15c6f9a71e4ae52f641b25eb3");
		itemRecommendation.setIsNew(Boolean.TRUE);
		itemRecommendation.setItemId(
			"https://www-prd.liferay.com/services/training/online");
		itemRecommendation.setJobId(1L);
		itemRecommendation.setRecommendedItemIds(
			Arrays.asList(
				"https://www-prd.liferay.com/blog",
				"https://www-prd.liferay.com/resource"));

		_itemRecommendationRepository.save(itemRecommendation);
	}

	@Test
	public void testCountByJobId() {
		Assertions.assertEquals(
			1, _itemRecommendationRepository.countByJobId(1L));
	}

	@Test
	public void testDeleteByJobId() {
		_itemRecommendationRepository.deleteByJobId(1L);

		Assertions.assertEquals(
			0, _itemRecommendationRepository.countByJobId(1L));
	}

	@Test
	public void testFindByJobId() {
		List<ItemRecommendation> itemRecommendations =
			_itemRecommendationRepository.findByJobId(
				1L, PageRequest.of(0, 10));

		Assertions.assertEquals(1, itemRecommendations.size());
	}

	@Autowired
	private ItemRecommendationRepository _itemRecommendationRepository;

	@Autowired
	private JobRunRepository _jobRunRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}