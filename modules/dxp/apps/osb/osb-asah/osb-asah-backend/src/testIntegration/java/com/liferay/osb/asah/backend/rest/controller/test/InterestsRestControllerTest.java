/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.InterestDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.rest.controller.InterestsRestController;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.repository.InterestTopicRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ResourceTemplateUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * @author Marcellus Tavares
 * @author Victor Oliveira
 */
public class InterestsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "osbasahfaroinfo/bq_identity_interest_page.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores.json"
	)
	@Test
	public void testGetInterestDTOPageDTO() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_interests.json", this),
			(JSONArray)JSONUtil.getValue(
				_objectMapper.convertValue(
					_interestsRestController.getInterestDTOPageDTO(
						null, null, null, 0, null, 20,
						new String[] {"name", "desc"}),
					JSONObject.class),
				"JSONObject/_embedded", "JSONArray/interests"),
			false);
	}

	@BQSQLResource(
		resourcePath = "osbasahfaroinfo/bq_identity_interest_page.sql"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_score_identities.json"
	)
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores.json"
	)
	@Test
	public void testGetInterestDTOsPageDTOOrderByName() {
		List<InterestDTO> interestDTOs = _getInterestDTOs(
			new String[] {"name", "desc"});

		InterestDTO firstInterestDTO = interestDTOs.get(0);

		InterestDTO lastInterestDTO = interestDTOs.get(interestDTOs.size() - 1);

		Assertions.assertEquals("web", firstInterestDTO.getName());

		Assertions.assertEquals(
			"clicks-and-mortar e-tailers", lastInterestDTO.getName());

		interestDTOs = _getInterestDTOs(new String[] {"name", "asc"});

		firstInterestDTO = interestDTOs.get(0);

		lastInterestDTO = interestDTOs.get(interestDTOs.size() - 1);

		Assertions.assertEquals(
			"clicks-and-mortar e-tailers", firstInterestDTO.getName());

		Assertions.assertEquals("web", lastInterestDTO.getName());
	}

	@BQSQLResource(resourcePath = "test_get_interest_keywords.sql")
	@Test
	public void testGetInterestKeywords() throws Exception {

		// No search

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_interest_keywords.json", this),
			(JSONArray)JSONUtil.getValue(
				new JSONObject(
					_interestsRestController.getInterestKeywords(null, 0, 20)),
				"JSONObject/_embedded", "JSONArray/interest-keywords"),
			false);

		// Search

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_interest_keywords_search.json", this),
			(JSONArray)JSONUtil.getValue(
				new JSONObject(
					_interestsRestController.getInterestKeywords(
						"compel", 0, 20)),
				"JSONObject/_embedded", "JSONArray/interest-keywords"),
			false);
	}

	@Test
	public void testGetInterestKeywordsNoAssets() throws Exception {
		JSONArray keywordsJSONArray = (JSONArray)JSONUtil.getValue(
			new JSONObject(
				_interestsRestController.getInterestKeywords(null, 0, 20)),
			"JSONObject/_embedded", "JSONArray/interest-keywords");

		Assertions.assertEquals(0, keywordsJSONArray.length());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/interests_2.json"
	)
	@Test
	public void testGetInterestTransformations() throws Exception {
		JSONAssert.assertEquals(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies/expected_interest_transformations.json",
					this)),
			new JSONObject(
				_interestsRestController.getInterestTransformations(
					null, null, 0, 20)),
			false);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/interests_2.json"
	)
	@Test
	public void testGetInterestTransformationsByApplyAndFilter()
		throws Exception {

		JSONAssert.assertEquals(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies" +
						"/expected_interest_transformations_filtered.json",
					this)),
			new JSONObject(
				_interestsRestController.getInterestTransformations(
					"compute(day(dateRecorded))", "name eq 'abc'", 0, 20)),
			false);
	}

	@BQSQLResource(resourcePath = "test_get_terms.sql")
	@RepositoryResource(
		repositoryClass = InterestTopicRepository.class,
		resourcePath = "osbasahfaroinfo/interest_topics.json"
	)
	@Test
	public void testGetTerms() throws Exception {
		_assertTermsByTopic(
			new JSONObject(
				_interestsRestController.getTerms(
					3, 0.01, 3, "953be104-5540-abf8-59b8-55f895200acc")),
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_interest_topics_per_user.json", this));
	}

	@Test
	public void testGetTermsOverLengthLimit() throws Exception {
		try {
			_interestsRestController.getTerms(100, 0.01, 11, "xxx-yyy-zzz");
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				HttpStatus.BAD_REQUEST, osbAsahException.getHttpStatus());
			Assertions.assertEquals(
				"termsPerTopic * topicsLength must not exceed 1000",
				osbAsahException.getMessage());
		}
	}

	@RepositoryResource(
		repositoryClass = InterestTopicRepository.class,
		resourcePath = "osbasahfaroinfo/interest_topics.json"
	)
	@Test
	public void testGetTermsRelated() throws Exception {
		_assertTerms(
			_objectMapper.convertValue(
				_interestsRestController.getTermsRelated(
					0, 5, Arrays.asList("javascript", "php"), 0.01),
				JSONObject.class),
			"jquery", "html", "sql", "mysql", "java");
	}

	@Test
	public void testGetTermsRelatedOverPageLimit() throws Exception {
		try {
			_interestsRestController.getTermsRelated(
				10, 15, Collections.emptyList(), 0.01);
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				HttpStatus.BAD_REQUEST, osbAsahException.getHttpStatus());
			Assertions.assertEquals(
				"page * size + size must not exceed 100",
				osbAsahException.getMessage());
		}
	}

	@RepositoryResource(
		repositoryClass = InterestTopicRepository.class,
		resourcePath = "osbasahfaroinfo/interest_topics.json"
	)
	@Test
	public void testGetTermsRelatedPaged() throws Exception {
		_assertTerms(
			_objectMapper.convertValue(
				_interestsRestController.getTermsRelated(
					1, 5, Arrays.asList("javascript", "php"), 0.01),
				JSONObject.class),
			"css", "canvas", "sql-server", "python", "multithreading");
	}

	@RepositoryResource(
		repositoryClass = InterestTopicRepository.class,
		resourcePath = "osbasahfaroinfo/interest_topics.json"
	)
	@Test
	public void testGetTermsRelatedWithSizeGreaterThanTheDefaultBucketSize()
		throws Exception {

		_assertTerms(
			_objectMapper.convertValue(
				_interestsRestController.getTermsRelated(
					0, 10, Arrays.asList("javascript", "php"), 0.01),
				JSONObject.class),
			"jquery", "html", "css", "sql", "mysql", "sql-server", "java",
			"android", "multithreading", "net");
	}

	@BQSQLResource(resourcePath = "test_get_terms.sql")
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores.json"
	)
	@Test
	public void testGetTermsWithNoIndividualInterestTerms() throws Exception {
		_assertTermsByTopic(
			new JSONObject(
				_interestsRestController.getTerms(
					3, 0.01, 3, "5666a530-7021-11e9-9f43-21e859277abc")),
			new JSONArray());
	}

	@BQSQLResource(resourcePath = "test_get_terms.sql")
	@RepositoryResource(
		repositoryClass = BQIdentityInterestScoreRepository.class,
		resourcePath = "osbasahfaroinfo/bq_identity_interest_scores.json"
	)
	@Test
	public void testGetTermsWithNoTopicsRelatedToIndividualInterestTerms()
		throws Exception {

		_assertTermsByTopic(
			new JSONObject(
				_interestsRestController.getTerms(
					3, 0.01, 3, "953be104-5540-abf8-59b8-55f895200acc")),
			new JSONArray());
	}

	@Test
	public void testGetTermsWithUnknownIndividual() throws Exception {
		try {
			_interestsRestController.getTerms(3, 0.01, 3, "xxx-yyy-zzz");
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				HttpStatus.BAD_REQUEST, osbAsahException.getHttpStatus());
			Assertions.assertEquals(
				"There is no individual with user ID xxx-yyy-zzz",
				osbAsahException.getMessage());
		}
	}

	private void _assertTerms(
		JSONObject responseJSONObject, String... expectedTerms) {

		JSONArray interestTopicsJSONArray = (JSONArray)JSONUtil.getValue(
			responseJSONObject, "JSONObject/_embedded",
			"JSONArray/interest-terms");

		for (int i = 0; i < interestTopicsJSONArray.length(); i++) {
			Assertions.assertEquals(
				expectedTerms[i], interestTopicsJSONArray.getString(i));
		}
	}

	private void _assertTermsByTopic(
		JSONObject responseJSONObject, JSONArray expectedTerms) {

		JSONAssert.assertEquals(
			expectedTerms,
			(JSONArray)JSONUtil.getValue(
				responseJSONObject, "JSONObject/_embedded",
				"JSONArray/interest-topics"),
			false);
	}

	private List<InterestDTO> _getInterestDTOs(String[] sorts) {
		PageDTO pageDTO = _interestsRestController.getInterestDTOPageDTO(
			null, null, null, 0, null, 15, sorts);

		Map<String, InterestDTO> contents = pageDTO.getContent();

		InterestDTO interestDTO = contents.get("_embedded");

		Set<InterestDTO> interestDTOs = interestDTO.getInterestDTOs();

		Stream<InterestDTO> stream = interestDTOs.stream();

		return stream.collect(Collectors.toList());
	}

	@Autowired
	private InterestsRestController _interestsRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}