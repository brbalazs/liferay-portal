/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.test;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.batch.curator.bot.nanite.UpdateMembershipsNanite;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Rachael Koestartyo
 */
@Import(JDBCTestConfiguration.class)
public class UpdateMembershipsNaniteTest
	implements OSBAsahBatchCuratorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_segmentRepository.deleteAll();

		_channelRepository.deleteAll();
	}

	@BQSQLResource
	@Test
	public void testRun() {
		ProjectIdThreadLocal.setProjectId("test");

		Segment segment = new Segment();

		segment.setAuthorName("Test Test");

		Channel channel1 = _addChannel(
			RandomTestUtil.randomNumber(), "Test Channel");

		segment.setChannelId(channel1.getId());

		segment.setCreateDate(DateUtil.addDays(new Date(), -5));
		segment.setFilter(
			String.format("(channelId eq '%s')", channel1.getId()));
		segment.setIsNew(Boolean.TRUE);
		segment.setName("Segment 1");
		segment.setState("IN_PROGRESS");
		segment.setStatus("ACTIVE");
		segment.setType(Segment.Type.DYNAMIC);

		_segmentRepository.save(segment);

		_updateMembershipsNanite.run(null);

		Optional<Segment> segmentOptional =
			_segmentRepository.findByNameAndStatus("Segment 1", "ACTIVE");

		segment = segmentOptional.get();

		Assertions.assertEquals("READY", segment.getState());
	}

	private Channel _addChannel(long id, String name) {
		Channel channel = new Channel(name);

		channel.setId(id);
		channel.setIsNew(Boolean.TRUE);

		return _channelRepository.save(channel);
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private SegmentRepository _segmentRepository;

	@Autowired
	private UpdateMembershipsNanite _updateMembershipsNanite;

}