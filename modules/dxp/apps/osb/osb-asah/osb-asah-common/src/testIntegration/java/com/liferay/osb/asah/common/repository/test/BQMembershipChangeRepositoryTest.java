/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang.time.DateUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class BQMembershipChangeRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		_newDayDate = DateUtil.newDayDate();

		_segments = Arrays.asList(_addSegment(), _addSegment(), _addSegment());
		_tomorrow = DateUtils.addDays(_newDayDate, 1);
		_yesterday = DateUtils.addDays(_newDayDate, -1);

		List<BQMembershipChange> bqMembershipsChanges = new LinkedList<>();

		for (Segment segment : _segments) {
			bqMembershipsChanges.add(
				_createBQMembershipChange(_yesterday, 1, segment));
			bqMembershipsChanges.add(
				_createBQMembershipChange(
					DateUtil.newEndOfDayDate(_newDayDate), 3, segment));
			bqMembershipsChanges.add(
				_createBQMembershipChange(_tomorrow, 5, segment));
		}

		bqMembershipsChanges.forEach(_bqMembershipChangeRepository::insert);
	}

	@AfterEach
	public void tearDown() {
		List<Long> segmentIds = new ArrayList<>();

		for (Segment segment : _segmentRepository.findAll()) {
			segmentIds.add(segment.getId());
		}

		_bqMembershipChangeRepository.deleteBySegmentIdIn(segmentIds);

		_segmentRepository.deleteAll();
	}

	@BQSQLResource(resourcePath = "test_add_membership_change_bq.sql")
	@SQLResource(resourcePath = "test_add_membership_change.sql")
	@Test
	public void testAddMembershipChange() {
		_bqMembershipChangeRepository.addBQMembershipChange(
			_bqMembershipRepository.getMembershipCountSnapshot(
				1L, 1029384756L));

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeRepository.
				findLastBQMembershipChangeBySegmentIds(
					Collections.singletonList(1029384756L));

		Assertions.assertEquals(1, bqMembershipChanges.size());

		BQMembershipChange bqMembershipChange = bqMembershipChanges.get(0);

		Assertions.assertEquals(5, bqMembershipChange.getIdentitiesCount());
		Assertions.assertEquals(3, bqMembershipChange.getIndividualsCount());
		Assertions.assertEquals(1029384756L, bqMembershipChange.getSegmentId());
	}

	@Test
	public void testCountMembershipChanges() {
		Segment segment = _segments.get(0);

		Assertions.assertEquals(
			3,
			_bqMembershipChangeRepository.countBQMembershipChanges(
				FilterHelper.EMPTY, segment.getId()));
	}

	@Test
	public void testDeleteBySegmentIdIn() {
		List<Segment> segments = _segments.subList(0, 2);

		Stream<Segment> segmentsStream = segments.stream();

		List<Long> segmentIds = segmentsStream.map(
			Segment::getId
		).collect(
			Collectors.toList()
		);

		_bqMembershipChangeRepository.deleteBySegmentIdIn(segmentIds);

		List<BQMembershipChange> bqMembershipChanges = IterableUtils.toList(
			_bqMembershipChangeRepository.findAll());

		Stream<BQMembershipChange> bqMembershipChangesStream =
			bqMembershipChanges.stream();

		Assertions.assertFalse(
			bqMembershipChangesStream.anyMatch(
				membershipChange -> segmentIds.contains(
					membershipChange.getSegmentId())));
	}

	@Test
	public void testFindLastBQMembershipChangeBySegmentIds() {
		Stream<Segment> segmentsStream = _segments.stream();

		List<Long> segmentIds = segmentsStream.map(
			Segment::getId
		).collect(
			Collectors.toList()
		);

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeRepository.
				findLastBQMembershipChangeBySegmentIds(segmentIds);

		Assertions.assertEquals(
			3, bqMembershipChanges.size(), bqMembershipChanges.toString());

		Stream<BQMembershipChange> bqMembershipChangesStream =
			bqMembershipChanges.stream();

		Assertions.assertEquals(
			segmentIds,
			bqMembershipChangesStream.map(
				BQMembershipChange::getSegmentId
			).collect(
				Collectors.toList()
			));

		for (BQMembershipChange actualBQMembershipChanges :
				bqMembershipChanges) {

			Assertions.assertEquals(
				7L, actualBQMembershipChanges.getIdentitiesCount());
		}
	}

	@Test
	public void testFindSegmentIdByFilterString() {
		List<Long> segmentIds =
			_bqMembershipChangeRepository.findSegmentIdByFilterString(
				"identitiesCount eq 1");

		Assertions.assertEquals(0, segmentIds.size());

		segmentIds = _bqMembershipChangeRepository.findSegmentIdByFilterString(
			"identitiesCount eq 7");

		Assertions.assertEquals(3, segmentIds.size());

		segmentIds = _bqMembershipChangeRepository.findSegmentIdByFilterString(
			"individualsCount ge 3");

		Assertions.assertEquals(3, segmentIds.size());
	}

	@Test
	public void testInitializeBQMembershipChanges() {
		_bqMembershipChangeRepository.initializeBQMembershipChanges(
			123L, ZoneId.of("UTC"));

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeRepository.findBySegmentId(123L);

		Assertions.assertEquals(31, bqMembershipChanges.size());

		LocalDateTime localDateTime = DateUtil.newDayLocalDateTime(
			ZoneId.of("UTC"));

		for (int i = 0; i < bqMembershipChanges.size(); i++) {
			BQMembershipChange bqMembershipChange = bqMembershipChanges.get(i);

			Assertions.assertEquals(0, bqMembershipChange.getIdentitiesCount());
			Assertions.assertEquals(
				0, bqMembershipChange.getIndividualsCount());
			Assertions.assertEquals(123L, bqMembershipChange.getSegmentId());

			LocalDateTime expectedLocalDateTime = localDateTime.minusDays(i);

			Assertions.assertEquals(
				Date.from(expectedLocalDateTime.toInstant(ZoneOffset.UTC)),
				bqMembershipChange.getCreateDate());
		}
	}

	@Test
	public void testSearchMembershipChanges() {
		Segment segment = _segments.get(0);

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeRepository.searchBQMembershipChanges(
				FilterHelper.EMPTY, segment.getId(), PageRequest.of(0, 1));

		Assertions.assertEquals(
			1, bqMembershipChanges.size(), bqMembershipChanges.toString());

		bqMembershipChanges =
			_bqMembershipChangeRepository.searchBQMembershipChanges(
				FilterHelper.EMPTY, segment.getId(), PageRequest.of(0, 10));

		Stream<BQMembershipChange> stream = bqMembershipChanges.stream();

		Map<Long, List<BQMembershipChange>> bqMembershipChangesMap =
			stream.collect(
				Collectors.groupingBy(BQMembershipChange::getSegmentId));

		Assertions.assertEquals(
			1, bqMembershipChangesMap.size(),
			bqMembershipChangesMap.toString());
	}

	private Segment _addSegment() {
		Segment segment = new Segment();

		Channel channel = _channelRepository.save(new Channel("Channel"));

		Long channelId = channel.getId();

		segment.setChannelId(channelId);

		segment.setCreateDate(_newDayDate);
		segment.setFilter(String.format("(channelId eq '%d')", channelId));
		segment.setName(String.format("Segment %d", channelId));
		segment.setReferencedDataSourceIds(SetUtil.of(5L, 6L));
		segment.setReferencedFieldMappingFieldNames(SetUtil.of("7", "8"));
		segment.setState("READY");
		segment.setStatus("STARTED");
		segment.setType(Segment.Type.DYNAMIC);

		return _segmentRepository.save(segment);
	}

	private BQMembershipChange _createBQMembershipChange(
		Date createDate, long knownIdentitiesCount, Segment segment) {

		BQMembershipChange bqMembershipChange = new BQMembershipChange();

		bqMembershipChange.setCreateDate(createDate);
		bqMembershipChange.setIdentitiesCount(knownIdentitiesCount + 2);
		bqMembershipChange.setIndividualsCount(knownIdentitiesCount);
		bqMembershipChange.setSegmentId(segment.getId());

		return bqMembershipChange;
	}

	@Autowired
	private BQMembershipChangeRepository _bqMembershipChangeRepository;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

	@Autowired
	private ChannelRepository _channelRepository;

	private Date _newDayDate;

	@Autowired
	private SegmentRepository _segmentRepository;

	private List<Segment> _segments;
	private Date _tomorrow;
	private Date _yesterday;

}