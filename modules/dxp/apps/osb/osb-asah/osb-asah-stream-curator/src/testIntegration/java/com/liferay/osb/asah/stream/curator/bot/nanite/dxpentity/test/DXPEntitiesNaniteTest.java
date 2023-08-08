/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.dxpentity.test;

import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.stream.curator.OSBAsahStreamCuratorSpringTestContext;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.stream.curator.bot.nanite.dxpentity.DXPEntitiesNanite;
import com.liferay.osb.asah.stream.curator.bot.nanite.test.BaseNaniteTestCase;
import com.liferay.osb.asah.test.util.annotation.MessageBusChannel;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class DXPEntitiesNaniteTest
	extends BaseNaniteTestCase
	implements OSBAsahStreamCuratorSpringTestContext {

	@MessageBusChannel(
		channel = Channel.DXP_ENTITIES_MESSAGE,
		resourcePath = "dxp_entities_message_2.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "data_sources.json"
	)
	@Test
	public void testProcessQueuedMessagesAddEntities() {
		runNanite();

		DXPEntity dxpEntity = _dxpEntityDog.fetchByFieldsAndType(
			Collections.singletonMap(
				"fields.emailAddress", "scott.lang@test.com"),
			DXPEntity.Type.USER);

		Assertions.assertNotNull(dxpEntity);
	}

	@MessageBusChannel(
		channel = Channel.DXP_ENTITIES_MESSAGE,
		resourcePath = "dxp_entities_message_1.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "data_sources.json"
	)
	@Test
	public void testProcessQueuedMessagesRetainsOrder() throws Exception {
		List<Message<JSONArray>> messages = _messageSubscriber.pullMessages(
			50, JSONArray::new);

		_messageSubscriber.sendAckIds(
			ListUtil.map(messages, Message::getAckId));

		Assertions.assertNotEquals(0, messages.size());

		JSONArray jsonArray = new JSONArray();

		Stream<Message<JSONArray>> stream = messages.stream();

		stream.collect(
			Collectors.groupingBy(
				message -> {
					Map<String, String> attributesMap = message.getAttributes();

					return attributesMap.get("projectId");
				},
				LinkedHashMap::new, Collectors.toList())
		).forEach(
			(id, message) -> _populateJSONArray(jsonArray, id, message)
		);

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONArray(
				"dependencies/dxp_entities_message_1.json", this),
			jsonArray, true);
	}

	@Override
	protected Nanite getNanite() {
		return _dxpEntitiesNanite;
	}

	private void _populateJSONArray(
		JSONArray jsonArray, String projectId,
		List<Message<JSONArray>> messages) {

		for (Message<JSONArray> message : messages) {
			JSONArray messageJSONArray = message.getObject();

			for (int i = 0; i < messageJSONArray.length(); i++) {
				jsonArray.put(messageJSONArray.getJSONObject(i));
			}
		}
	}

	@Autowired
	private DXPEntitiesNanite _dxpEntitiesNanite;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@MessageSubscriber.Autowired(channel = Channel.DXP_ENTITIES_MESSAGE)
	private MessageSubscriber _messageSubscriber;

}