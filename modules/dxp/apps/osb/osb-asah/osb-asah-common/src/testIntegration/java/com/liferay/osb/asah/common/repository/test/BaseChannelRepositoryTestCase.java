/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
public abstract class BaseChannelRepositoryTestCase
	extends BaseRepositoryTestCase<Channel, Long> {

	@BeforeEach
	public void setUp() {
		setUpRepository(new Channel("name"));

		_channel = entityModels.get(0);
	}

	@AfterEach
	public void tearDown() {
		_channelRepository.deleteAll();
	}

	@Test
	public void testCountByNameContainingIgnoreCase() {
		Assertions.assertEquals(
			1, _channelRepository.countByNameContainingIgnoreCase("NAME"));
	}

	@Test
	public void testCountByNameContainingIgnoreCaseAndStateNot() {
		Assertions.assertEquals(
			1, _channelRepository.countByNameContainingIgnoreCase("NAME"),
			"READY");
	}

	@Test
	public void testDeleteByIdIn() {
		_channelRepository.deleteByIdIn(
			Collections.singleton(_channel.getId()));

		Assertions.assertEquals(0, _channelRepository.count());
	}

	@Test
	public void testExistsByIdNotAndName() {
		Channel channel = _channelRepository.save(new Channel("channel"));

		Assertions.assertTrue(
			_channelRepository.existsByIdNotAndName(channel.getId(), "name"));

		Assertions.assertFalse(
			_channelRepository.existsByIdNotAndName(_channel.getId(), "name"));
	}

	@Test
	public void testExistsByName() {
		Assertions.assertTrue(_channelRepository.existsByName("name"));
	}

	@Test
	public void testFindByDataSourceId() {
		ChannelDataSource channelDataSource = new ChannelDataSource(
			Collections.emptySet(), 123L, Collections.emptySet());

		_channel.addChannelDataSource(channelDataSource);

		_channelRepository.save(_channel);

		List<Channel> channels = _channelRepository.findByDataSourceId(123L);

		Assertions.assertEquals(1, channels.size(), channels.toString());

		Channel channel = channels.get(0);

		Set<ChannelDataSource> channelDataSources =
			channel.getChannelDataSources();

		Assertions.assertEquals(
			1, channelDataSources.size(), channelDataSources.toString());
		Assertions.assertTrue(channelDataSources.contains(channelDataSource));
	}

	@Test
	public void testFindByDataSourceIdAndGroupIds() {
		ChannelDataSource channelDataSource = new ChannelDataSource(
			Collections.emptySet(), 123L, SetUtil.of(456L, 789L));

		_channel.addChannelDataSource(channelDataSource);

		_channelRepository.save(_channel);

		List<Channel> channels =
			_channelRepository.findByDataSourceIdAndGroupIds(
				123L, SetUtil.of(456L, 789L));

		Assertions.assertEquals(1, channels.size(), channels.toString());

		Channel channel = channels.get(0);

		Set<ChannelDataSource> channelDataSources =
			channel.getChannelDataSources();

		Assertions.assertEquals(
			1, channelDataSources.size(), channelDataSources.toString());
		Assertions.assertTrue(channelDataSources.contains(channelDataSource));
	}

	@Test
	public void testFindByNameContainingIgnoreCase() {
		List<Channel> channels =
			_channelRepository.findByNameContainingIgnoreCase(
				"NAME", PageRequest.of(0, 1));

		Assertions.assertEquals(1, channels.size(), channels.toString());
		Assertions.assertEquals(_channel, channels.get(0));
	}

	@Test
	public void testFindByNameContainingIgnoreCaseAndStateNot() {
		Iterable<Channel> channels = _channelRepository.findAll();

		_channelRepository.saveAll(channels);

		List<Channel> channelsNotReady =
			_channelRepository.findByNameContainingIgnoreCaseAndStateNot(
				"NAME", PageRequest.of(0, 1), "READY");

		Assertions.assertEquals(0, channelsNotReady.size());
	}

	@Override
	protected PagingAndSortingRepository<Channel, Long>
		getPagingAndSortingRepository() {

		return _channelRepository;
	}

	private Channel _channel;

	@Autowired
	private ChannelRepository _channelRepository;

}