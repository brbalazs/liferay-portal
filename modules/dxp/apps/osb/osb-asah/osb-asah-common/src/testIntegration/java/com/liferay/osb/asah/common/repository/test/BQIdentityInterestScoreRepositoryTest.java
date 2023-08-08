/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * @author Robson Pastor
 */
public class BQIdentityInterestScoreRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		_bqIdentityInterestScore1 = new BQIdentityInterestScore();

		_bqIdentityInterestScore1.setChannelId(1L);
		_bqIdentityInterestScore1.setIdentityId("374790569167317525");
		_bqIdentityInterestScore1.setInterested(Boolean.TRUE);
		_bqIdentityInterestScore1.setInterestScore(1.7676619);
		_bqIdentityInterestScore1.setKeyword("clicks-and-mortar e-tailers");
		_bqIdentityInterestScore1.setRecordedDate(
			DateUtil.toUTCDate("2021-09-12T00:00:00.000Z"));

		_bqIdentityInterestScore2 = new BQIdentityInterestScore();

		_bqIdentityInterestScore2.setChannelId(1L);
		_bqIdentityInterestScore2.setIdentityId("374790575409131096");
		_bqIdentityInterestScore2.setInterested(Boolean.TRUE);
		_bqIdentityInterestScore2.setInterestScore(2.6149597);
		_bqIdentityInterestScore2.setKeyword("javascript");
		_bqIdentityInterestScore2.setRecordedDate(
			DateUtil.toUTCDate("2021-09-13T00:00:00.000Z"));

		_bqIdentityInterestScore3 = new BQIdentityInterestScore();

		_bqIdentityInterestScore3.setChannelId(1L);
		_bqIdentityInterestScore3.setIdentityId("374790572703144534");
		_bqIdentityInterestScore3.setInterested(Boolean.FALSE);
		_bqIdentityInterestScore3.setInterestScore(0.77022254);
		_bqIdentityInterestScore3.setKeyword("compelling metrics");
		_bqIdentityInterestScore3.setRecordedDate(
			DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		_bqIdentityInterestScore4 = new BQIdentityInterestScore();

		_bqIdentityInterestScore4.setChannelId(1L);
		_bqIdentityInterestScore4.setIdentityId("374790572703144534");
		_bqIdentityInterestScore4.setInterested(Boolean.TRUE);
		_bqIdentityInterestScore4.setInterestScore(1.454685);
		_bqIdentityInterestScore4.setKeyword("sales");
		_bqIdentityInterestScore4.setRecordedDate(
			DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		_bqIdentityInterestScore5 = new BQIdentityInterestScore();

		_bqIdentityInterestScore5.setChannelId(1L);
		_bqIdentityInterestScore5.setIdentityId("374790572703144535");
		_bqIdentityInterestScore5.setInterested(Boolean.TRUE);
		_bqIdentityInterestScore5.setInterestScore(1.454685);
		_bqIdentityInterestScore5.setKeyword("sales");
		_bqIdentityInterestScore5.setRecordedDate(
			DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testCountByChannelIdAndIndividualIdAndKeywords() {
		Assertions.assertEquals(
			2,
			_bqIdentityInterestScoreRepository.
				countByChannelIdAndIndividualIdAndKeywords(null, null, null));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testCountByIndividualId() {
		Assertions.assertEquals(
			2,
			_bqIdentityInterestScoreRepository.countByChannelIdAndIndividualId(
				null, "374790572703144534"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testCountKeywords() {
		Assertions.assertEquals(
			3, _bqIdentityInterestScoreRepository.countKeywords(null));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testCountKeywordsWithFilter() {
		Assertions.assertEquals(
			1, _bqIdentityInterestScoreRepository.countKeywords("le"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testDeleteByNameAndRecordedDateGreaterThanEqual() {
		_bqIdentityInterestScoreRepository.
			deleteByKeywordAndRecordedDateGreaterThanEqual(
				"individual", DateUtil.toUTCDate("2021-09-13T00:00:00.000Z"));

		Assertions.assertEquals(6, _bqIdentityInterestScoreRepository.count());
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testDeleteByRecordedDateLessThanEqual() {
		_bqIdentityInterestScoreRepository.deleteByRecordedDateLessThanEqual(
			DateUtil.toUTCDate("2021-09-13T00:00:00.000Z"));

		Assertions.assertEquals(4, _bqIdentityInterestScoreRepository.count());
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testFindByIndividualId() {
		List<BQIdentityInterestScore> bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.findByChannelIdAndIndividualId(
				null, "374790572703144534", PageRequest.of(0, 10));

		Assertions.assertEquals(
			2, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());
		Assertions.assertEquals(
			Arrays.asList(_bqIdentityInterestScore3, _bqIdentityInterestScore4),
			bqIdentityInterestScores);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testFindByNameAndIndividualIdAndRecordedDateBetween() {
		List<BQIdentityInterestScore> bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.
				findByIndividualIdAndKeywordAndRecordedDateBetween(
					"374790572703144535", "sales",
					DateUtil.toUTCDate("2021-09-13T00:00:00.000Z"),
					DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		Assertions.assertEquals(
			1, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());
		Assertions.assertEquals(
			Arrays.asList(_bqIdentityInterestScore5), bqIdentityInterestScores);

		bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.
				findByIndividualIdAndKeywordAndRecordedDateBetween(
					"374790572703144535", "sales",
					DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"),
					DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		Assertions.assertEquals(
			1, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());
		Assertions.assertEquals(
			Arrays.asList(_bqIdentityInterestScore5), bqIdentityInterestScores);

		bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.
				findByIndividualIdAndKeywordAndRecordedDateBetween(
					"374790572703144535", "sales",
					DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"),
					DateUtil.toUTCDate("2021-09-15T00:00:00.000Z"));

		Assertions.assertEquals(
			1, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());

		bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.
				findByIndividualIdAndKeywordAndRecordedDateBetween(
					"374790572703144535", "sales",
					DateUtil.toUTCDate("2021-09-15T00:00:00.000Z"),
					DateUtil.toUTCDate("2021-09-16T00:00:00.000Z"));

		Assertions.assertEquals(
			0, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testFindByRecordedDate() {
		List<BQIdentityInterestScore> bqIdentityInterestScores =
			_bqIdentityInterestScoreRepository.findByRecordedDate(
				DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"), 10);

		Assertions.assertEquals(
			4, bqIdentityInterestScores.size(),
			bqIdentityInterestScores.toString());
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testFindIndividualIdsByFilterStringAndIndividualId() {
		List<String> individualIds =
			_bqIdentityInterestScoreRepository.
				findIndividualIdsByFilterStringAndIndividualId(
					"(keyword eq 'sales')", "374790572703144534");

		Assertions.assertEquals(
			Arrays.asList("374790572703144534"), individualIds,
			individualIds.toString());

		individualIds =
			_bqIdentityInterestScoreRepository.
				findIndividualIdsByFilterStringAndIndividualId(
					"(keyword eq 'rick''s garage')", "374790575409131096");

		Assertions.assertEquals(
			Arrays.asList("374790575409131096"), individualIds,
			individualIds.toString());
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetByNameAndIndividualIdAndRecordedDate() {
		BQIdentityInterestScore bqIdentityInterestScore =
			_bqIdentityInterestScoreRepository.
				getByIndividualIdAndKeywordAndRecordedDate(
					"374790575409131096", "javascript",
					DateUtil.toUTCDate("2021-09-13T00:00:00.000Z"));

		Assertions.assertEquals(
			_bqIdentityInterestScore2, bqIdentityInterestScore);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetInterestTransformationsByDay() {
		List<Map<String, Object>> transformations =
			_bqIdentityInterestScoreRepository.getTransformations(
				DateUtil.toUTCDate("2021-09-11T00:00:00.000Z"), null, "day",
				DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		Assertions.assertEquals(
			4, transformations.size(), transformations.toString());

		_assertTransformation(
			"2021-09-11T00:00:00.000Z", 0, 0, transformations.get(0));
		_assertTransformation(
			"2021-09-12T00:00:00.000Z", 1.7676619, 1, transformations.get(1));
		_assertTransformation(
			"2021-09-13T00:00:00.000Z", 2.6149597, 1, transformations.get(2));
		_assertTransformation(
			"2021-09-14T00:00:00.000Z", 1.283569385, 4, transformations.get(3));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetInterestTransformationsByWeek() {
		List<Map<String, Object>> transformations =
			_bqIdentityInterestScoreRepository.getTransformations(
				DateUtil.toUTCDate("2021-09-01T00:00:00.000Z"), null, "week",
				DateUtil.toUTCDate("2021-09-14T00:00:00.000Z"));

		Assertions.assertEquals(
			3, transformations.size(), transformations.toString());
		_assertTransformation(
			"2021-08-29T00:00:00.000Z", 0D, 0, transformations.get(0));
		_assertTransformation(
			"2021-09-05T00:00:00.000Z", 0D, 0, transformations.get(1));
		_assertTransformation(
			"2021-09-12T00:00:00.000Z", 1.5861498566666665, 6,
			transformations.get(2));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetKeywords() {
		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("compelling metrics");
					add("rick's garage");
					add("sales");
				}
			},
			_bqIdentityInterestScoreRepository.getKeywords(
				null, PageRequest.of(0, 20)));

		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("rick's garage");
				}
			},
			_bqIdentityInterestScoreRepository.getKeywords(
				"rick's", PageRequest.of(0, 20)));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetKeywordsPagination() {
		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("rick's garage");
				}
			},
			_bqIdentityInterestScoreRepository.getKeywords(
				null, PageRequest.of(1, 1)));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetKeywordsWithFilter() {
		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("sales");
				}
			},
			_bqIdentityInterestScoreRepository.getKeywords(
				"le", PageRequest.of(0, 20)));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_score_repository.sql"
	)
	@Test
	public void testGetTopNamesByIndividualId() {
		List<String> names =
			_bqIdentityInterestScoreRepository.getTopKeywordsByIndividualId(
				"374790572703144534", 10);

		Assertions.assertEquals(
			Arrays.asList("sales", "compelling metrics"), names,
			names.toString());
	}

	private void _assertTransformation(
		String intervalInitDate, double scoreAvg, long totalElements,
		Map<String, Object> transformation) {

		Assertions.assertEquals(
			intervalInitDate, transformation.get("intervalInitDate"));
		Assertions.assertEquals(scoreAvg, transformation.get("scoreAvg"));
		Assertions.assertEquals(
			BigDecimal.valueOf(totalElements),
			transformation.get("totalElements"));
	}

	private BQIdentityInterestScore _bqIdentityInterestScore1;
	private BQIdentityInterestScore _bqIdentityInterestScore2;
	private BQIdentityInterestScore _bqIdentityInterestScore3;
	private BQIdentityInterestScore _bqIdentityInterestScore4;
	private BQIdentityInterestScore _bqIdentityInterestScore5;

	@Autowired
	private BQIdentityInterestScoreRepository
		_bqIdentityInterestScoreRepository;

}