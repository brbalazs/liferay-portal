/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.dog.SiteInterestCompositionDog;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class SiteInterestCompositionDogTest extends BaseCompositionDogTestCase {

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagCustomRange() {
		LocalDate localDate = LocalDate.now(ZoneOffset.UTC);

		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0,
				TimeRange.of(localDate.minusDays(9), localDate.minusDays(90))),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 1L);
				}
			},
			1, 1, 1);

		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				null, 10, 0,
				TimeRange.of(localDate.minusDays(9), localDate.minusDays(90))),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 1L);
				}
			},
			1, 1, 1);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLast7Days() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(7)),
			new LinkedHashMap<String, Long>() {
				{
					put("holistic roi", 1L);
				}
			},
			1, 1, 1);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLast28Days() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(28)),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 1L);
					put("holistic roi", 1L);
				}
			},
			1, 2, 2);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLast30Days() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(30)),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 1L);
					put("holistic roi", 1L);
				}
			},
			1, 2, 2);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLast90Days() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(90)),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 1L);
					put("holistic roi", 1L);
				}
			},
			1, 2, 2);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLast180Days() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(180)),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 2L);
					put("holistic roi", 1L);
				}
			},
			2, 2, 3);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagLastYear() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(365)),
			new LinkedHashMap<String, Long>() {
				{
					put("compelling action-items", 4L);
					put("holistic roi", 1L);
				}
			},
			4, 2, 4);
	}

	@BQSQLResource(resourcePath = "session_interest_score_info.sql")
	@Test
	public void testGetCompositionResultBagYesterday() {
		checkResults(
			_siteInterestCompositionDog.getCompositionResultBag(
				1L, 10, 0, TimeRange.of(1)),
			new LinkedHashMap<String, Long>() {
				{
					put("holistic roi", 1L);
				}
			},
			1, 1, 1);
	}

	@Autowired
	private SiteInterestCompositionDog _siteInterestCompositionDog;

}