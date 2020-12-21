/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.asset.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyServiceUtil;
import com.liferay.asset.test.util.AssetTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author José Manuel Navarro
 */
@RunWith(Arquillian.class)
public class AssetCategoryServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testDeleteVocabularyAlsoUpdatesCategoriesTree()
		throws Exception {

		long groupId = _group.getGroupId();

		AssetVocabulary vocabulary1 = AssetTestUtil.addVocabulary(groupId);
		AssetVocabulary vocabulary2 = AssetTestUtil.addVocabulary(groupId);

		AssetCategory category1a = AssetTestUtil.addCategory(
			groupId, vocabulary1.getVocabularyId());

		assertLeftRightCategory(1, category1a);

		AssetCategory category1b = AssetTestUtil.addCategory(
			groupId, vocabulary1.getVocabularyId());

		assertLeftRightCategory(3, category1b);

		AssetCategory category1c = AssetTestUtil.addCategory(
			groupId, vocabulary1.getVocabularyId());

		assertLeftRightCategory(5, category1c);

		AssetCategory category2a = AssetTestUtil.addCategory(
			groupId, vocabulary2.getVocabularyId());

		assertLeftRightCategory(7, category2a);

		AssetCategory category2b = AssetTestUtil.addCategory(
			groupId, vocabulary2.getVocabularyId());

		assertLeftRightCategory(9, category2b);

		AssetCategory category2c = AssetTestUtil.addCategory(
			groupId, vocabulary2.getVocabularyId());

		assertLeftRightCategory(11, category2c);

		AssetVocabularyServiceUtil.deleteVocabulary(
			vocabulary1.getVocabularyId());

		int count = AssetCategoryServiceUtil.getVocabularyCategoriesCount(
			groupId, vocabulary1.getVocabularyId());

		Assert.assertEquals(0, count);

		count = AssetCategoryServiceUtil.getVocabularyCategoriesCount(
			groupId, vocabulary2.getVocabularyId());

		Assert.assertEquals(3, count);

		category2a = AssetCategoryServiceUtil.getCategory(
			category2a.getCategoryId());

		assertLeftRightCategory(1, category2a);

		category2b = AssetCategoryServiceUtil.getCategory(
			category2b.getCategoryId());

		assertLeftRightCategory(3, category2b);

		category2c = AssetCategoryServiceUtil.getCategory(
			category2c.getCategoryId());

		assertLeftRightCategory(5, category2c);
	}

	@Test
	public void testUniqueCategoryIdsWhenAddingCategoriesSimultaneously()
		throws Exception {

		long groupId = _group.getGroupId();

		AssetVocabulary vocabulary = AssetTestUtil.addVocabulary(groupId);

		List<Callable<Void>> callables = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			callables.add(
				() -> {
					AssetTestUtil.addCategory(
						groupId, vocabulary.getVocabularyId());

					return null;
				});
		}

		for (int i = 0; i < 2; i++) {
			callables.add(
				() -> {
					AssetCategory parentCategory = AssetTestUtil.addCategory(
						groupId, vocabulary.getVocabularyId());

					AssetTestUtil.addCategory(
						groupId, vocabulary.getVocabularyId(),
						parentCategory.getCategoryId());

					AssetTestUtil.addCategory(
						groupId, vocabulary.getVocabularyId(),
						parentCategory.getCategoryId());

					return null;
				});
		}

		Collections.shuffle(callables);

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		executorService.invokeAll(callables);

		executorService.shutdown();

		List<AssetCategory> assetCategories =
			AssetCategoryServiceUtil.getVocabularyCategories(
				vocabulary.getVocabularyId(), -1, -1, null);

		long expectedAssetCategoriesCount = 9;
		long expectedLeftRightCategoryIdsCount = 18;
		List<Long> expectedLeftRightCategoryIdsSorted = Arrays.asList(
			1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L,
			16L, 17L, 18L);

		assertUniqueLeftRightCategories(
			expectedAssetCategoriesCount, expectedLeftRightCategoryIdsCount,
			expectedLeftRightCategoryIdsSorted, assetCategories);
	}

	protected void assertLeftRightCategory(
			long expectedLeft, AssetCategory category)
		throws Exception {

		Assert.assertEquals(expectedLeft, category.getLeftCategoryId());
		Assert.assertEquals(expectedLeft + 1, category.getRightCategoryId());
	}

	protected void assertUniqueLeftRightCategories(
		long expectedAssetCategoriesCount,
		long expectedLeftRightCategoryIdsCount,
		List<Long> expectedLeftRightCategoryIdsSorted,
		List<AssetCategory> assetCategories) {

		List<Long> actualLeftRightCategoryIdsSorted = new ArrayList<>();

		for (AssetCategory assetCategory : assetCategories) {
			actualLeftRightCategoryIdsSorted.add(
				assetCategory.getLeftCategoryId());
			actualLeftRightCategoryIdsSorted.add(
				assetCategory.getRightCategoryId());
		}

		Collections.sort(actualLeftRightCategoryIdsSorted);

		long actualAssetCategoriesCount = assetCategories.size();
		long actualLeftRightCategoryIdsCount =
			actualLeftRightCategoryIdsSorted.size();

		Assert.assertEquals(
			expectedAssetCategoriesCount, actualAssetCategoriesCount);
		Assert.assertEquals(
			expectedLeftRightCategoryIdsCount, actualLeftRightCategoryIdsCount);
		Assert.assertEquals(
			expectedLeftRightCategoryIdsSorted.toString(),
			actualLeftRightCategoryIdsSorted.toString());
	}

	@DeleteAfterTestRun
	private Group _group;

}