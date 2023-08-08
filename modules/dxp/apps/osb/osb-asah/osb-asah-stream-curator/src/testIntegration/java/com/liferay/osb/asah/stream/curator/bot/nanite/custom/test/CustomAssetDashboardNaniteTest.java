/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.custom.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.stream.curator.OSBAsahStreamCuratorSpringTestContext;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.stream.curator.bot.nanite.custom.CustomAssetDashboardNanite;
import com.liferay.osb.asah.stream.curator.bot.nanite.test.BaseNaniteTestCase;
import com.liferay.osb.asah.test.util.annotation.MessageBusChannel;
import com.liferay.osb.asah.test.util.messaging.MessageBusTestHelper;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;

import org.json.JSONArray;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class CustomAssetDashboardNaniteTest
	extends BaseNaniteTestCase
	implements OSBAsahStreamCuratorSpringTestContext {

	@BeforeEach
	public void setUp() {
		com.liferay.osb.asah.common.entity.Channel channel =
			new com.liferay.osb.asah.common.entity.Channel();

		channel.setId(1L);
		channel.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel);
	}

	@AfterEach
	public void tearDown() {
		_channelRepository.deleteById(1L);
	}

	@MessageBusChannel(
		channel = Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
		resourcePath = "analytics_events_custom_asset_channel.json"
	)
	@Test
	public void testAddDashboard() throws Exception {
		runNanite();

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_custom_asset_dashboards_info.json",
				this),
			_objectMapper.convertValue(
				_customAssetDashboardRepository.findAll(), JSONArray.class),
			false);
	}

	@MessageBusChannel(
		channel = Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
		resourcePath = "analytics_events_custom_asset_channel.json"
	)
	@Test
	public void testSkipTitleUpdate() throws Exception {
		runNanite();

		MessageBusTestHelper messageBusTestHelper = new MessageBusTestHelper(
			_messageBus);

		messageBusTestHelper.prepareMessageBusChannel(
			Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/analytics_events_custom_asset_channel_empty_title.json",
				this));

		runNanite();

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/expected_custom_asset_dashboards_info.json",
				this),
			_objectMapper.convertValue(
				_customAssetDashboardRepository.findAll(), JSONArray.class),
			false);
	}

	@Test
	public void testUpdateTitle() throws Exception {
		runNanite();

		MessageBusTestHelper messageBusTestHelper = new MessageBusTestHelper(
			_messageBus);

		messageBusTestHelper.prepareMessageBusChannel(
			Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/analytics_events_custom_asset_channel_update_1.json",
				this));

		runNanite();

		messageBusTestHelper.prepareMessageBusChannel(
			Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
			ResourceUtil.readResourceToJSONArray(
				"dependencies" +
					"/analytics_events_custom_asset_channel_update_2.json",
				this));

		runNanite();

		List<CustomAssetDashboard> customAssetDashboards = IterableUtils.toList(
			_customAssetDashboardRepository.findAll());

		Assertions.assertEquals(1, customAssetDashboards.size());

		CustomAssetDashboard customAssetDashboard = customAssetDashboards.get(
			0);

		Assertions.assertEquals(
			"Asset's Title Update 2", customAssetDashboard.getAssetTitle());
	}

	@Override
	protected Nanite getNanite() {
		return _customAssetDashboardNanite;
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private CustomAssetDashboardNanite _customAssetDashboardNanite;

	@Autowired
	private CustomAssetDashboardRepository _customAssetDashboardRepository;

	@Autowired
	private MessageBus _messageBus;

	@Autowired
	private ObjectMapper _objectMapper;

}