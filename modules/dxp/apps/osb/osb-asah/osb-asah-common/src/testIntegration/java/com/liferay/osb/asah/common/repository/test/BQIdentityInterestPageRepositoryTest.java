/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQIdentityInterestPageRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leslie Wong
 */
public class BQIdentityInterestPageRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testCountActivePagesTransformationsIndividual() {
		Assertions.assertEquals(
			2,
			_bqIdentityInterestPageRepository.countActivePagesTransformations(
				1L, "interestName eq 'ratio'",
				"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c32f4f" +
					"d9f4",
				"individual"));

		Assertions.assertEquals(
			1,
			_bqIdentityInterestPageRepository.countActivePagesTransformations(
				1L, "interestName eq 'Liferay''s Experience'",
				"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c32f4f" +
					"d9f4",
				"individual"));

		Assertions.assertEquals(
			1,
			_bqIdentityInterestPageRepository.countActivePagesTransformations(
				1L, "interestName eq 'You''re'",
				"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c32f4f" +
					"d9f4",
				"individual"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testCountActivePagesTransformationsIndividualSegment() {
		Assertions.assertEquals(
			5,
			_bqIdentityInterestPageRepository.countActivePagesTransformations(
				1L, "interestName eq 'ratio'", "1234567891011",
				"individual-segment"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testCountInactivePagesTransformationsIndividual() {
		Assertions.assertEquals(
			3,
			_bqIdentityInterestPageRepository.countInactivePagesTransformations(
				1L, "interestName eq 'ratio'",
				"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c32f4f" +
					"d9f4",
				"individual"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testCountInactivePagesTransformationsIndividualSegment() {
		Assertions.assertEquals(
			0,
			_bqIdentityInterestPageRepository.countInactivePagesTransformations(
				1L, "interestName eq 'ratio'", "1234567891011",
				"individual-segment"));
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetActivePagesTransformationsIndividual() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/expected_active_pages_transformations_individual.json",
				this),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual", PageRequest.of(0, 5))),
			true);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetActivePagesTransformationsIndividualSegment()
		throws Exception {

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/expected_active_pages_transformations_individual_" +
						"segment.json",
				this),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'", "1234567891011",
					"individual-segment", PageRequest.of(0, 5))),
			true);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetActivePagesTransformationsIndividualWithSort() {
		JSONObject transformationJSONObject1 = JSONUtil.put(
			"title", "Know Your Ratios - Weight"
		).put(
			"uniqueVisitsCount", BigDecimal.valueOf(6)
		).put(
			"url", "https://www.know-your-ratios.com/weight"
		);

		JSONObject transformationJSONObject2 = JSONUtil.put(
			"title", "Know Your Ratios - Home"
		).put(
			"uniqueVisitsCount", BigDecimal.valueOf(2)
		).put(
			"url", "https://www.know-your-ratios.com"
		);

		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject2, transformationJSONObject1),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5, SortUtil.getSort(new String[] {"url", "asc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject1, transformationJSONObject2),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5, SortUtil.getSort(new String[] {"url", "desc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject2, transformationJSONObject1),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5,
						SortUtil.getSort(
							new String[] {"uniqueVisitsCount", "asc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject1, transformationJSONObject2),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5,
						SortUtil.getSort(
							new String[] {"uniqueVisitsCount", "desc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject2, transformationJSONObject1),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5,
						SortUtil.getSort(new String[] {"title", "asc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject1, transformationJSONObject2),
			new JSONArray(
				_bqIdentityInterestPageRepository.getActivePagesTransformations(
					1L, "interestName eq 'ratio'",
					"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142a6c3" +
						"2f4fd9f4",
					"individual",
					PageRequest.of(
						0, 5,
						SortUtil.getSort(new String[] {"title", "desc"})))),
			true);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetInactivePagesTransformationsIndividual()
		throws Exception {

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/expected_inactive_pages_transformations_individual.json",
				this),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'",
						"ae9fbeefab123032b0ce91e946ec50930aeb2f55116ee887d142" +
							"a6c32f4fd9f4",
						"individual", PageRequest.of(0, 5))),
			true);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetInactivePagesTransformationsIndividualSegment()
		throws Exception {

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/expected_inactive_pages_transformations_individual_" +
						"segment.json",
				this),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'", "3456789101112",
						"individual-segment", PageRequest.of(0, 5))),
			true);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_interest_page_repository.sql"
	)
	@Test
	public void testGetInactivePagesTransformationsIndividualSegmentWithSort() {
		JSONObject transformationJSONObject1 = JSONUtil.put(
			"title", "Know Your Ratios - Distance"
		).put(
			"uniqueVisitsCount", BigDecimal.valueOf(0)
		).put(
			"url", "https://www.know-your-ratios.com/distance"
		);

		JSONObject transformationJSONObject2 = JSONUtil.put(
			"title", "Know Your Ratios - Time"
		).put(
			"uniqueVisitsCount", BigDecimal.valueOf(0)
		).put(
			"url", "https://www.know-your-ratios.com/time"
		);

		JSONObject transformationJSONObject3 = JSONUtil.put(
			"title", "Know Your Ratios - Volume"
		).put(
			"uniqueVisitsCount", BigDecimal.valueOf(0)
		).put(
			"url", "https://www.know-your-ratios.com/volume"
		);

		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject1, transformationJSONObject2,
				transformationJSONObject3),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'", "3456789101112",
						"individual-segment",
						PageRequest.of(
							0, 5,
							SortUtil.getSort(new String[] {"title", "asc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject3, transformationJSONObject2,
				transformationJSONObject1),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'", "3456789101112",
						"individual-segment",
						PageRequest.of(
							0, 5,
							SortUtil.getSort(new String[] {"title", "desc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject1, transformationJSONObject2,
				transformationJSONObject3),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'", "3456789101112",
						"individual-segment",
						PageRequest.of(
							0, 5,
							SortUtil.getSort(new String[] {"url", "asc"})))),
			true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(
				transformationJSONObject3, transformationJSONObject2,
				transformationJSONObject1),
			new JSONArray(
				_bqIdentityInterestPageRepository.
					getInactivePagesTransformations(
						1L, "interestName eq 'ratio'", "3456789101112",
						"individual-segment",
						PageRequest.of(
							0, 5,
							SortUtil.getSort(new String[] {"url", "desc"})))),
			true);
	}

	@Autowired
	private BQIdentityInterestPageRepository _bqIdentityInterestPageRepository;

}