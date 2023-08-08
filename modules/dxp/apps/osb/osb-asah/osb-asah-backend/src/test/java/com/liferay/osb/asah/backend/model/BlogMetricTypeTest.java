/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseEnumTestCase;
import com.liferay.osb.asah.common.model.TrendClassification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class BlogMetricTypeTest extends BaseEnumTestCase<BlogMetricType> {

	@Test
	public void testClicks() {
		BlogMetricType blogMetricType = BlogMetricType.of("clicksMetric");

		Assertions.assertEquals(BlogMetricType.CLICKS, blogMetricType);
	}

	@Test
	public void testClicksFieldName() {
		BlogMetricType blogMetricType = BlogMetricType.CLICKS;

		Assertions.assertEquals("clicks", blogMetricType.getFieldName());
	}

	@Test
	public void testClicksTrendClassificationOrder() {
		BlogMetricType blogMetricType = BlogMetricType.CLICKS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			blogMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testComments() {
		BlogMetricType blogMetricType = BlogMetricType.of("commentsMetric");

		Assertions.assertEquals(BlogMetricType.COMMENTS, blogMetricType);
	}

	@Test
	public void testCommentsFieldName() {
		BlogMetricType blogMetricType = BlogMetricType.COMMENTS;

		Assertions.assertEquals("comments", blogMetricType.getFieldName());
	}

	@Test
	public void testCommentsTrendClassificationOrder() {
		BlogMetricType blogMetricType = BlogMetricType.COMMENTS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			blogMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testRatings() {
		BlogMetricType blogMetricType = BlogMetricType.of("ratingsMetric");

		Assertions.assertEquals(BlogMetricType.RATINGS, blogMetricType);
	}

	@Test
	public void testRatingsFieldName() {
		BlogMetricType blogMetricType = BlogMetricType.RATINGS;

		Assertions.assertEquals("ratingsScore", blogMetricType.getFieldName());
	}

	@Test
	public void testRatingsTrendClassificationOrder() {
		BlogMetricType blogMetricType = BlogMetricType.RATINGS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			blogMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testReadingTime() {
		BlogMetricType blogMetricType = BlogMetricType.of("readingTimeMetric");

		Assertions.assertEquals(BlogMetricType.READING_TIME, blogMetricType);
	}

	@Test
	public void testReadingTimeFieldName() {
		BlogMetricType blogMetricType = BlogMetricType.READING_TIME;

		Assertions.assertEquals("readTime", blogMetricType.getFieldName());
	}

	@Test
	public void testReadingTimeTrendClassificationOrder() {
		BlogMetricType blogMetricType = BlogMetricType.READING_TIME;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			blogMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testViews() {
		BlogMetricType blogMetricType = BlogMetricType.of("viewsMetric");

		Assertions.assertEquals(BlogMetricType.VIEWS, blogMetricType);
	}

	@Test
	public void testViewsFieldName() {
		BlogMetricType blogMetricType = BlogMetricType.VIEWS;

		Assertions.assertEquals("views", blogMetricType.getFieldName());
	}

	@Test
	public void testViewsTrendClassificationOrder() {
		BlogMetricType blogMetricType = BlogMetricType.VIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			blogMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return BlogMetricType.class;
	}

}