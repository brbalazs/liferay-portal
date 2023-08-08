/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alejo Ceballos
 */
public class BQMembershipChangeDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		List<String> channelNames = Arrays.asList(
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		Date date = DateUtil.newDayDate();

		channelNames.forEach(
			channelName -> {
				int daysAgoCreation = -32;

				Segment segment = _addSegment(
					_channelRepository.save(new Channel(channelName)),
					DateUtils.addDays(date, daysAgoCreation));

				for (int daysAgo = daysAgoCreation; daysAgo <= 0; daysAgo++) {
					BQMembershipChange bqMembershipChange =
						_addMembershipChange(
							DateUtils.addDays(date, daysAgo),
							33 + daysAgo +
								(channelNames.indexOf(channelName) * 33),
							segment);

					if (daysAgo == 0) {
						_bqMembershipChangeBySegmentId.put(
							segment.getId(), bqMembershipChange);
						_segments.add(segment);
					}
				}
			});
	}

	@Test
	public void testGetBQMembershipChanges() {
		Assertions.assertEquals(
			_bqMembershipChangeBySegmentId,
			_bqMembershipChangeDog.getLastBQMembershipChanges(_segments));
	}

	@Test
	public void testGetLastBQMembershipChangeBySegmentId() {
		List<Long> segmentIds = new ArrayList<>(
			_bqMembershipChangeBySegmentId.keySet());

		Long segmentId = segmentIds.get(0);

		BQMembershipChange bqMembershipChange =
			_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentId(
				segmentId);

		Assertions.assertEquals(
			_bqMembershipChangeBySegmentId.get(segmentId), bqMembershipChange);
	}

	@Test
	public void testGetLastBQMembershipChangeBySegmentIds1() {
		List<Long> segmentIds = new ArrayList<>(
			_bqMembershipChangeBySegmentId.keySet());

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentIds(
				segmentIds);

		Assertions.assertEquals(
			2, bqMembershipChanges.size(), bqMembershipChanges.toString());
		Assertions.assertNotEquals(
			bqMembershipChanges.get(0), bqMembershipChanges.get(1));

		for (BQMembershipChange bqMembershipChange : bqMembershipChanges) {
			Assertions.assertEquals(
				_bqMembershipChangeBySegmentId.get(
					bqMembershipChange.getSegmentId()),
				bqMembershipChange);
		}
	}

	@Test
	public void testGetLastBQMembershipChangeBySegmentIds2() {
		Date date = DateUtil.newDayDate();

		Segment segment = _addSegment(
			_channelRepository.save(new Channel(RandomTestUtil.randomString())),
			DateUtils.addDays(date, -1));

		segment.setIncludeAnonymousUsers(true);

		segment = _segmentRepository.save(segment);

		BQMembershipChange bqMembershipChange = _addMembershipChange(
			DateUtils.addDays(date, -1), 1, segment);

		_bqMembershipChangeRepository.insert(bqMembershipChange);

		List<BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentIds(
				Arrays.asList(segment.getId()));

		Assertions.assertEquals(
			1, bqMembershipChanges.size(), bqMembershipChanges.toString());
	}

	private BQMembershipChange _addMembershipChange(
		Date createDate, int index, Segment segment) {

		BQMembershipChange bqMembershipChange = new BQMembershipChange();

		bqMembershipChange.setCreateDate(createDate);
		bqMembershipChange.setIdentitiesCount((long)index);
		bqMembershipChange.setIndividualsCount((long)index);
		bqMembershipChange.setSegmentId(segment.getId());

		return _bqMembershipChangeRepository.insert(bqMembershipChange);
	}

	private Segment _addSegment(Channel channel, Date createDate) {
		Segment segment = new Segment();

		segment.setChannelId(channel.getId());

		segment.setCreateDate(createDate);
		segment.setFilter(
			String.format("(channelId eq '%d')", channel.getId()));
		segment.setName(
			String.format("Segment of channel %s", channel.getName()));
		segment.setReferencedDataSourceIds(SetUtil.of(5L, 6L));
		segment.setReferencedFieldMappingFieldNames(SetUtil.of("7", "8"));
		segment.setState("READY");
		segment.setStatus("STARTED");
		segment.setType(Segment.Type.DYNAMIC);

		return _segmentRepository.save(segment);
	}

	private final Map<Long, BQMembershipChange> _bqMembershipChangeBySegmentId =
		new HashMap<>();

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipChangeRepository _bqMembershipChangeRepository;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private SegmentRepository _segmentRepository;

	private final List<Segment> _segments = new ArrayList<>();

}