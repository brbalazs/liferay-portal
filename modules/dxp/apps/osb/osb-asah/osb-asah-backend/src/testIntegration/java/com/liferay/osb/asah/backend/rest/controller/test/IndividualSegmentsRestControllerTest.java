/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.SegmentDTO;
import com.liferay.osb.asah.backend.rest.controller.IndividualSegmentsRestController;
import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vishal Reddy
 * @author Rachael Koestartyo
 */
public class IndividualSegmentsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_segmentRepository.deleteAll();
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testAssignChannelAlreadyAssigned() {
		Assertions.assertThrows(
			OSBAsahException.class,
			() -> {
				_individualSegmentsRestController.assignChannel(
					1L, 327968823603500655L);

				_individualSegmentsRestController.assignChannel(
					1L, 327968823603500655L);
			});
	}

	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships_1.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testAssignChannelModifiesIndividualsCount() throws Exception {
		_individualSegmentsRestController.assignChannel(
			1L, 366637689379787789L);

		Segment segment = _segmentDog.getSegment(366637689379787789L);

		Assertions.assertEquals(1L, segment.getChannelId());
	}

	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testAssignChannelStaticSegment() throws Exception {
		_individualSegmentsRestController.assignChannel(
			1L, 338511451975440187L);

		Segment segment = _segmentDog.getSegment(338511451975440187L);

		Assertions.assertEquals(1L, segment.getChannelId());

		Assertions.assertEquals(
			2,
			_bqMembershipRepository.countBySegmentIdAndStatus(
				338511451975440187L, "ACTIVE"));
	}

	@BQSQLResource(resourcePath = "test_get_individuals_bq.sql")
	@SQLResource(resourcePath = "test_get_individuals.sql")
	@Test
	public void testGetIndividuals1() throws Exception {
		JSONObject individualsJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getIndividualDTOPageDTO(
				327968823603500666L, null, false, 0, 20, null),
			JSONObject.class);

		JSONObject embeddedJSONObject = individualsJSONObject.getJSONObject(
			"_embedded");

		System.out.println(embeddedJSONObject.toString());

		JSONArray individualsJSONArray = embeddedJSONObject.getJSONArray(
			"individuals");

		Assertions.assertEquals(1, individualsJSONArray.length());
	}

	@BQSQLResource(resourcePath = "test_get_individuals_bq.sql")
	@SQLResource(resourcePath = "test_get_individuals.sql")
	@Test
	public void testGetIndividuals2() throws Exception {
		JSONObject individualsJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getIndividualDTOPageDTO(
				327968823603500666L, null, false, 0, 100, null),
			JSONObject.class);

		JSONObject embeddedJSONObject = individualsJSONObject.getJSONObject(
			"_embedded");

		JSONArray individualsJSONArray = embeddedJSONObject.getJSONArray(
			"individuals");

		Assertions.assertEquals(1, individualsJSONArray.length());
	}

	@BQSQLResource(resourcePath = "test_get_individuals_bq.sql")
	@Disabled
	@SQLResource(resourcePath = "test_get_individuals.sql")
	@Test
	public void testGetIndividualsIncludeAnonymousUsers() throws Exception {
		JSONObject individualsJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getIndividualDTOPageDTO(
				327968823603500666L, null, true, 0, 20, null),
			JSONObject.class);

		JSONObject embeddedJSONObject = individualsJSONObject.getJSONObject(
			"_embedded");

		JSONArray individualsJSONArray = embeddedJSONObject.getJSONArray(
			"individuals");

		Assertions.assertEquals(1, individualsJSONArray.length());
	}

	@RepositoryResource(
		repositoryClass = BQMembershipChangeRepository.class,
		resourcePath = "osbasahfaroinfo/bq_membership_changes.json"
	)
	@Test
	public void testGetMembershipChanges() {

		// Include anonymous users

		JSONObject membershipChangesJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getBQMembershipChangeDTOPageDTO(
				327968823603500666L, null, null, 0, 20, null),
			JSONObject.class);

		JSONObject embeddedJSONObject =
			membershipChangesJSONObject.getJSONObject("_embedded");

		JSONArray membershipChangesJSONArray = embeddedJSONObject.getJSONArray(
			"membership-changes");

		Assertions.assertEquals(1, membershipChangesJSONArray.length());

		JSONObject membershipJSONObject =
			(JSONObject)membershipChangesJSONArray.get(0);

		Assertions.assertEquals(
			2, membershipJSONObject.getInt("individualsCount"));
		Assertions.assertEquals(
			1, membershipJSONObject.getInt("knownIndividualsCount"));

		// Does not include anonymous users

		membershipChangesJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getBQMembershipChangeDTOPageDTO(
				327968823603500677L, null, null, 0, 20, null),
			JSONObject.class);

		embeddedJSONObject = membershipChangesJSONObject.getJSONObject(
			"_embedded");

		membershipChangesJSONArray = embeddedJSONObject.getJSONArray(
			"membership-changes");

		membershipJSONObject = (JSONObject)membershipChangesJSONArray.get(0);

		Assertions.assertEquals(
			2, membershipJSONObject.getInt("individualsCount"));
		Assertions.assertEquals(
			0, membershipJSONObject.getInt("knownIndividualsCount"));
	}

	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_1.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testGetMemberships() throws Exception {

		// Include anonymous users

		JSONObject membershipsJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getBQMembershipDTOPageDTO(
				327968823603500666L, null, 0, 20, null),
			JSONObject.class);

		JSONObject embeddedJSONObject = membershipsJSONObject.getJSONObject(
			"_embedded");

		JSONArray membershipsJSONArray = embeddedJSONObject.getJSONArray(
			"memberships");

		Assertions.assertEquals(2, membershipsJSONArray.length());

		// Does not include anonymous users

		membershipsJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.getBQMembershipDTOPageDTO(
				327968823603500677L, null, 0, 20, null),
			JSONObject.class);

		embeddedJSONObject = membershipsJSONObject.getJSONObject("_embedded");

		membershipsJSONArray = embeddedJSONObject.getJSONArray("memberships");

		Assertions.assertEquals(
			2, membershipsJSONArray.length(), membershipsJSONArray.toString());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@Test
	public void testPostIndividualSegment1() throws Exception {
		Segment segment = FaroInfoTestUtil.buildDynamicSegment(1L, "");

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.postSegment(
				_objectMapper.convertValue(segment, SegmentDTO.class)),
			JSONObject.class);

		Assertions.assertEquals(
			"IN_PROGRESS", responseJSONObject.getString("state"));
	}

	@Test
	public void testPostIndividualSegment2() {
		Segment segment = FaroInfoTestUtil.buildStaticSegment();

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.postSegment(
				_objectMapper.convertValue(segment, SegmentDTO.class)),
			JSONObject.class);

		Assertions.assertEquals("READY", responseJSONObject.getString("state"));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_1.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testPreviewDisabledSegments() throws Exception {
		Assertions.assertEquals(
			1L,
			JSONUtil.getValue(
				_objectMapper.convertValue(
					_individualSegmentsRestController.
						getPreviewDisabledSegmentDTOPageDTO(
							351238757269547424L, null, 0, 10, null),
					JSONObject.class),
				"JSONObject/page", "Object/totalElements"));
		Assertions.assertEquals(
			1L,
			JSONUtil.getValue(
				_objectMapper.convertValue(
					_individualSegmentsRestController.
						getPreviewDisabledSegmentDTOPageDTO(
							351238757269547424L, "((status eq 'ACTIVE'))", 0,
							10, null),
					JSONObject.class),
				"JSONObject/page", "Object/totalElements"));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_2.json"
	)
	@Test
	public void testPutIndividualSegment1() {
		Segment segment = _segmentDog.addSegment(
			FaroInfoTestUtil.buildDynamicSegment(1L, ""));

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.putSegment(
				segment.getId(),
				new SegmentDTO(
					null,
					_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentId(
						segment.getId()),
					_segmentDog.getLastActivityDate(segment), segment)),
			JSONObject.class);

		Assertions.assertEquals(
			"IN_PROGRESS", responseJSONObject.getString("state"));
	}

	@Test
	public void testPutIndividualSegment2() throws Exception {
		Segment segment = _segmentDog.addSegment(
			FaroInfoTestUtil.buildStaticSegment());

		Long segmentId = segment.getId();

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_individualSegmentsRestController.putSegment(
				segmentId,
				new SegmentDTO(
					null,
					_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentId(
						segmentId),
					_segmentDog.getLastActivityDate(segment), segment)),
			JSONObject.class);

		Assertions.assertEquals("READY", responseJSONObject.getString("state"));
	}

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

	@Autowired
	private IndividualSegmentsRestController _individualSegmentsRestController;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SegmentDog _segmentDog;

	@Autowired
	private SegmentRepository _segmentRepository;

}