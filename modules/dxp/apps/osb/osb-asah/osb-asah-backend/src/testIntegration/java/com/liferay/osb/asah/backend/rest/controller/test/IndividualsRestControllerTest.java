/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.FieldDTO;
import com.liferay.osb.asah.backend.dto.IndividualDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.dto.SegmentDTO;
import com.liferay.osb.asah.backend.rest.controller.IndividualsRestController;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leslie Wong
 */
public class IndividualsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_active_membership_returned_bq.sql")
	@SQLResource(resourcePath = "test_active_membership_returned.sql")
	@Test
	public void testActiveMembershipReturned() {
		JSONObject segmentsJSONObject = _objectMapper.convertValue(
			_individualsRestController.getSegmentDTOPageDTO(
				"123", "active-membership", null, 0, 10, null),
			JSONObject.class);

		JSONObject embeddedJSONObject = segmentsJSONObject.getJSONObject(
			"_embedded");

		JSONArray segmentsJSONArray = embeddedJSONObject.getJSONArray(
			"individual-segments");

		Map<String, JSONObject> jsonObjectMap = JSONUtil.toJSONObjectMap(
			segmentsJSONArray, "id");

		JSONObject segmentJSONObject = jsonObjectMap.get("910");

		embeddedJSONObject = segmentJSONObject.getJSONObject("_embedded");

		JSONObject activeMembershipJSONObject =
			embeddedJSONObject.getJSONObject("active-membership");

		Assertions.assertEquals(
			"123", activeMembershipJSONObject.getString("individualId"));
		Assertions.assertEquals(
			"910", activeMembershipJSONObject.getString("individualSegmentId"));
		Assertions.assertEquals(
			"ACTIVE", activeMembershipJSONObject.getString("status"));
	}

	@BQSQLResource(resourcePath = "test_expand_data_sources_bq.sql")
	@SQLResource(resourcePath = "test_expand_data_sources.sql")
	@Test
	public void testExpandDataSources() throws Exception {
		IndividualDTO individualDTO =
			_individualsRestController.getIndividualDTO(
				"123", null, "data-sources");

		Map<String, Object> embedded = individualDTO.getEmbedded();

		JSONArray dataSourcesJSONArray = (JSONArray)embedded.get(
			"data-sources");

		Assertions.assertEquals(2, dataSourcesJSONArray.length());
	}

	@BQSQLResource(resourcePath = "test_expand_individual_segments_bq.sql")
	@SQLResource(resourcePath = "test_expand_individual_segments.sql")
	@Test
	public void testExpandIndividualSegments() throws Exception {
		IndividualDTO individualDTO =
			_individualsRestController.getIndividualDTO(
				"123", null, "individual-segments");

		Map<String, Object> embedded = individualDTO.getEmbedded();

		JSONArray individualSegmentsJSONArray = (JSONArray)embedded.get(
			"individual-segments");

		Assertions.assertEquals(2, individualSegmentsJSONArray.length());
	}

	@BQSQLResource(resourcePath = "test_get_individual_dto_page_dto_bq.sql")
	@Test
	public void testGetIndividualDTOPageDTO() throws Exception {
		PageDTO<IndividualDTO> individualDTOPageDTO =
			_individualsRestController.getIndividualDTOPageDTO(
				null, 100L, null, null, null, null, null, null, 0, null, null,
				1, null);

		Map<String, IndividualDTO> contents = individualDTOPageDTO.getContent();

		IndividualDTO individualDTO = contents.get("_embedded");

		Set<IndividualDTO> individualDTOs = individualDTO.getIndividualDTOs();

		Assertions.assertEquals(
			1, individualDTOs.size(), individualDTOs.toString());

		Stream<IndividualDTO> stream = individualDTOs.stream();

		individualDTO = stream.findFirst(
		).orElse(
			null
		);

		Assertions.assertEquals(1, (long)individualDTO.getActivitiesCount());
		Assertions.assertEquals(
			DateUtil.toUTCDate("2019-03-11T17:10:00.666Z"),
			individualDTO.getLastActivityDate());
	}

	@BQSQLResource(resourcePath = "test_get_individuals_date_fields_bq.sql")
	@Test
	public void testGetIndividualsDateFields() throws Exception {
		IndividualDTO individualDTO =
			_individualsRestController.getIndividualDTO("123", null, null);

		IndividualDTO.IndividualFieldDTO individualFieldDTO =
			individualDTO.getIndividualFieldDTO();

		Map<String, Object> fields = individualFieldDTO.getField();

		List<FieldDTO> fieldDTOs = (List<FieldDTO>)fields.get("birthDate");

		FieldDTO birthDateFieldDTO = fieldDTOs.get(0);

		Assertions.assertEquals(
			"1970-01-01T00:00:00.000Z",
			String.valueOf(birthDateFieldDTO.getValue()));

		fieldDTOs = (List<FieldDTO>)fields.get("createDate");

		FieldDTO createDateFieldDTO = fieldDTOs.get(0);

		Assertions.assertEquals(
			"2021-11-11T18:17:39.072Z",
			String.valueOf(createDateFieldDTO.getValue()));

		fieldDTOs = (List<FieldDTO>)fields.get("modifiedDate");

		FieldDTO modifiedDateFieldDTO = fieldDTOs.get(0);

		Assertions.assertEquals(
			"2021-11-11T18:18:13.657Z",
			String.valueOf(modifiedDateFieldDTO.getValue()));

		individualDTO = _individualsRestController.getIndividualDTO(
			"124", null, null);

		individualFieldDTO = individualDTO.getIndividualFieldDTO();

		fields = individualFieldDTO.getField();

		fieldDTOs = (List<FieldDTO>)fields.get("birthDate");

		birthDateFieldDTO = fieldDTOs.get(0);

		Assertions.assertEquals(
			"1964-06-30T12:00:00.000Z",
			String.valueOf(birthDateFieldDTO.getValue()));
	}

	@BQSQLResource(resourcePath = "test_get_individuals_distribution_bq.sql")
	@SQLResource(resourcePath = "test_get_individuals_distribution.sql")
	@Test
	public void testGetIndividualsDistribution() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_individuals_distribution_sorted.json",
				this),
			_objectMapper.convertValue(
				_individualsRestController.getDistributionDTOPageDTO(
					null, "emailAddress", null, 10, 100,
					new String[] {"name", "desc"}),
				JSONObject.class),
			false);
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_individuals_terms_distribution.json",
				this),
			_objectMapper.convertValue(
				_individualsRestController.getDistributionDTOPageDTO(
					null, "emailAddress", null, 0, 100, null),
				JSONObject.class),
			false);
	}

	@Test
	public void testGetIndividualsDistributionInvalidFieldMappings() {
		Exception exception = Assertions.assertThrows(
			Exception.class,
			() -> _individualsRestController.getDistributionDTOPageDTO(
				null, "331238757947565234", null, 10, 100, null));

		MatcherAssert.assertThat(
			exception.getMessage(),
			CoreMatchers.containsString("Invalid field name"));
	}

	@Disabled
	@Test
	public void testGetIndividualsDistributionWithBinning() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_individuals_distribution_filtered.json",
				this),
			_objectMapper.convertValue(
				_individualsRestController.getDistributionDTOPageDTO(
					null, "366588394714972833", 327968823603500655L, 10, 100,
					null),
				JSONObject.class),
			false);
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_individuals_numbers_distribution.json",
				this),
			_objectMapper.convertValue(
				_individualsRestController.getDistributionDTOPageDTO(
					null, "366588394714972833", null, 5, 100, null),
				JSONObject.class),
			false);
	}

	@BQSQLResource(resourcePath = "test_transformation_dto_page_dto.sql")
	@Test
	public void testGetTransformationDTOPageDTO() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_transformation_dto_page_dto.json", this),
			_objectMapper.convertValue(
				_individualsRestController.getTransformationDTOPageDTO(
					"groupby((demographics/givenName/value))", null, null,
					false, 0, 10),
				JSONObject.class),
			false);
	}

	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships_2.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipChangeRepository.class,
		resourcePath = "osbasahfaroinfo/bq_membership_changes_2.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments_2.json"
	)
	@Test
	public void testInactiveMembershipNotReturned() {
		PageDTO<SegmentDTO> segmentDTOPageDTO =
			_individualsRestController.getSegmentDTOPageDTO(
				"456", "active-membership", null, 0, 10, null);

		Map<String, SegmentDTO> contents = segmentDTOPageDTO.getContent();

		SegmentDTO segmentDTO = contents.get("_embedded");

		Assertions.assertTrue(
			CollectionUtils.isEmpty(segmentDTO.getSegmentDTOs()));
	}

	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships_2.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipChangeRepository.class,
		resourcePath = "osbasahfaroinfo/bq_membership_changes_2.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments_2.json"
	)
	@Test
	public void testNotSegmentedIndividualNotReturned() {
		PageDTO<SegmentDTO> segmentDTOPageDTO =
			_individualsRestController.getSegmentDTOPageDTO(
				"321", "active-membership", null, 0, 10, null);

		Map<String, SegmentDTO> contents = segmentDTOPageDTO.getContent();

		SegmentDTO segmentDTO = contents.get("_embedded");

		Assertions.assertTrue(
			CollectionUtils.isEmpty(segmentDTO.getSegmentDTOs()));
	}

	@Autowired
	private IndividualsRestController _individualsRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}