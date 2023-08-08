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
public class DocumentLibraryMetricTypeTest
	extends BaseEnumTestCase<DocumentLibraryMetricType> {

	@Test
	public void testComments() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.of("commentsMetric");

		Assertions.assertEquals(
			DocumentLibraryMetricType.COMMENTS, documentLibraryMetricType);
	}

	@Test
	public void testCommentsFieldName() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.COMMENTS;

		Assertions.assertEquals(
			"comments", documentLibraryMetricType.getFieldName());
	}

	@Test
	public void testCommentsTrendClassificationOrder() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.COMMENTS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			documentLibraryMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testDownloads() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.of("downloadsMetric");

		Assertions.assertEquals(
			DocumentLibraryMetricType.DOWNLOADS, documentLibraryMetricType);
	}

	@Test
	public void testDownloadsFieldName() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.DOWNLOADS;

		Assertions.assertEquals(
			"downloads", documentLibraryMetricType.getFieldName());
	}

	@Test
	public void testDownloadsTrendClassificationOrder() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.DOWNLOADS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			documentLibraryMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testPreviews() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.of("previewsMetric");

		Assertions.assertEquals(
			DocumentLibraryMetricType.PREVIEWS, documentLibraryMetricType);
	}

	@Test
	public void testPreviewsFieldName() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.PREVIEWS;

		Assertions.assertEquals(
			"previews", documentLibraryMetricType.getFieldName());
	}

	@Test
	public void testPreviewsTrendClassificationOrder() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.PREVIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			documentLibraryMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testRatings() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.of("ratingsMetric");

		Assertions.assertEquals(
			DocumentLibraryMetricType.RATINGS, documentLibraryMetricType);
	}

	@Test
	public void testRatingsFieldName() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.RATINGS;

		Assertions.assertEquals(
			"ratingsScore", documentLibraryMetricType.getFieldName());
	}

	@Test
	public void testRatingsTrendClassificationOrder() {
		DocumentLibraryMetricType documentLibraryMetricType =
			DocumentLibraryMetricType.RATINGS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			documentLibraryMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return DocumentLibraryMetricType.class;
	}

}