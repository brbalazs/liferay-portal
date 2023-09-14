/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.repository.AssetRepository;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.impl.DSL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author André Miranda
 */
public class ChannelDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testAddChannelWithDuplicateName() {
		Channel channel = _channelDog.addChannel("channel1");

		Assertions.assertEquals("channel1 (1)", channel.getName());
	}

	@BQSQLResource(resourcePath = "test_bq_clear_channels.sql")
	@SQLResource(resourcePath = "test_clear_channels.sql")
	@Test
	public void testClearChannels() throws Exception {
		_channelDog.clearChannels(SetUtil.of(1L, 2L), true, "0", "test");

		_assertClearChannels(1L);

		Assertions.assertEquals(
			0,
			_bqEventRepository.countTotalBQEvents(
				1L, null, null, null, null, "UTC"));

		List<BQSession> bqSessions = _bqSessionRepository.findAllById(
			Collections.singleton("366909399944213421"));

		Assertions.assertEquals(0, bqSessions.size());

		Assertions.assertEquals(1, _customAssetDashboardRepository.count());
		Assertions.assertEquals(1, _experimentRepository.count());
		Assertions.assertEquals(1, _segmentRepository.count());

		Assertions.assertNotNull(_channelDog.fetchChannel(1L));
	}

	@BQSQLResource(resourcePath = "test_bq_delete_channels.sql")
	@SQLResource(resourcePath = "test_delete_channels.sql")
	@Test
	public void testDeleteChannels() throws Exception {
		_channelDog.deleteChannels(SetUtil.of(1L), "0", "test");

		_assertClearChannels(1L);

		Assertions.assertEquals(
			0,
			_bqEventRepository.countTotalBQEvents(
				1L, null, null, null, null, "UTC"));

		List<BQSession> bqSessions = _bqSessionRepository.findAllById(
			Collections.singleton("366909399944213421"));

		Assertions.assertEquals(0, bqSessions.size());

		Assertions.assertEquals(0, _customAssetDashboardRepository.count());
		Assertions.assertEquals(0, _experimentRepository.count());
		Assertions.assertEquals(0, _segmentRepository.count());

		Assertions.assertNull(_channelDog.fetchChannel(1L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetChannelNamesByGroupIds() {
		Map<Long, String> channelNames = _channelDog.getChannelNamesByGroupIds(
			405201047787757795L, SetUtil.of(123L, 456L, 789L));

		Assertions.assertEquals(
			2, channelNames.size(), channelNames.toString());
		Assertions.assertEquals("channel1", channelNames.get(123L));
		Assertions.assertEquals("channel2", channelNames.get(456L));
		Assertions.assertNull(channelNames.get(789L));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetChannelPage() {
		Page<Channel> channelPage = _channelDog.getChannelPage("", 0, 20, null);

		Assertions.assertEquals(3, channelPage.getTotalElements());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetChannelPageByName() {
		Page<Channel> channelPage = _channelDog.getChannelPage(
			"channel2", 0, 20, null);

		Assertions.assertEquals(1, channelPage.getTotalElements());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetChannelPageSort() {
		Page<Channel> channelPage = _channelDog.getChannelPage(
			"", 0, 20, new String[] {"name", "desc"});

		Assertions.assertEquals(3, channelPage.getTotalElements());

		List<Channel> channels = channelPage.getContent();

		Channel channel = channels.get(0);

		Assertions.assertEquals("channel3", channel.getName());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testPatchChannelAddGroups() {
		Long dataSourceId = RandomTestUtil.randomNumber();

		_channelDog.patchChannel(
			1L, dataSourceId, SetUtil.of(456L, 789L), null);

		Channel channel = _channelDog.getChannel(1L);

		Set<ChannelDataSource> channelChannelDataSources =
			channel.getChannelDataSources();

		Stream<ChannelDataSource> stream = channelChannelDataSources.stream();

		Map<Long, List<ChannelDataSource>> channelDataSourcesByDataSourceId =
			stream.collect(
				Collectors.groupingBy(ChannelDataSource::getDataSourceId));

		List<ChannelDataSource> channelDataSources =
			channelDataSourcesByDataSourceId.get(405201047787757795L);

		ChannelDataSource channelDataSource = channelDataSources.get(0);

		Assertions.assertEquals(
			SetUtil.of(123L), channelDataSource.getGroupIds());

		channelDataSources = channelDataSourcesByDataSourceId.get(dataSourceId);

		channelDataSource = channelDataSources.get(0);

		Assertions.assertEquals(
			SetUtil.of(456L, 789L), channelDataSource.getGroupIds());

		channelDataSources = channelDataSourcesByDataSourceId.get(
			402135416847684684L);

		channelDataSource = channelDataSources.get(0);

		Assertions.assertEquals(
			SetUtil.of(321L), channelDataSource.getGroupIds());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testPatchChannelName() {
		String name = RandomTestUtil.randomString();

		_channelDog.patchChannel(1L, null, null, name);

		Channel channel = _channelDog.getChannel(1L);

		Assertions.assertEquals(name, channel.getName());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testPatchChannelReplaceGroups() {
		_channelDog.patchChannel(
			1L, 405201047787757795L, SetUtil.of(456L, 789L), null);

		Channel channel = _channelDog.getChannel(1L);

		ChannelDataSource channelDataSource1 = _findFirstChannelDataSource(
			405201047787757795L, channel.getChannelDataSources());

		Assertions.assertEquals(
			SetUtil.of(456L, 789L), channelDataSource1.getGroupIds());

		ChannelDataSource channelDataSource2 = _findFirstChannelDataSource(
			402135416847684684L, channel.getChannelDataSources());

		Assertions.assertEquals(
			SetUtil.of(321L), channelDataSource2.getGroupIds());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testPatchChannelWithDuplicateName() {
		Channel channel = _channelDog.patchChannel(2L, null, null, "channel1");

		Assertions.assertEquals("channel1 (1)", channel.getName());
	}

	private void _assertClearChannels(long channelId) {
		String[] tableNames = {
			"BlogDaily", "DocumentLibraryDaily", "FormDaily",
			"BQIdentityInterestPage", "BQIdentityInterestScore", "JournalDaily",
			"PageDaily", "BQSessionInterestScore"
		};

		for (String tableName : tableNames) {
			Assertions.assertEquals(
				0,
				_queryExecutor.queryForLong(
					DSL.selectCount(
					).from(
						tableName
					).where(
						DSL.field(
							"channelId"
						).eq(
							channelId
						)
					)));
		}
	}

	private ChannelDataSource _findFirstChannelDataSource(
		Long dataSourceId, Set<ChannelDataSource> channelDataSources) {

		for (ChannelDataSource channelDataSource : channelDataSources) {
			if (Objects.equals(
					channelDataSource.getDataSourceId(), dataSourceId)) {

				return channelDataSource;
			}
		}

		return null;
	}

	private String[] _getChannelAssetChannelIds(Long assetId) {
		Optional<Asset> assetOptional = _assetRepository.findById(assetId);

		Asset asset = assetOptional.get();

		Set<Long> channelIds = asset.getChannelIds();

		Stream<Long> stream = channelIds.stream();

		return stream.map(
			String::valueOf
		).toArray(
			String[]::new
		);
	}

	@Autowired
	private AssetRepository _assetRepository;

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private BQSessionRepository _bqSessionRepository;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private CustomAssetDashboardRepository _customAssetDashboardRepository;

	@Autowired
	private ExperimentRepository _experimentRepository;

	@Autowired
	private QueryExecutor _queryExecutor;

	@Autowired
	private SegmentRepository _segmentRepository;

}