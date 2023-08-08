/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite;

import com.liferay.osb.asah.common.entity.BQIdentity;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class IdentityNanite {

	public void run() throws Exception {
		while (true) {
			List<Message<String>> messages = _messageSubscriber.pullMessages(
				100, String::valueOf);

			if (messages.isEmpty()) {
				break;
			}

			Stream<Message<String>> stream = messages.stream();

			stream.forEach(this::_processMessage);

			_acknowledgeMessages(messages);
		}
	}

	private void _acknowledgeMessages(List<Message<String>> messages) {
		Stream<Message<String>> stream = messages.stream();

		_messageSubscriber.sendAckIds(
			stream.map(
				Message::getAckId
			).collect(
				Collectors.toList()
			));
	}

	private void _processMessage(Message<String> message) {
		JSONObject jsonObject = new JSONObject(message.getObject());

		ProjectIdThreadLocal.forProject(
			jsonObject.getString("projectId"),
			() -> _saveBQIdentity(jsonObject));
	}

	private BQIdentity _saveBQIdentity(JSONObject jsonObject) {
		String userId = jsonObject.getString("userId");

		BQIdentity bqIdentity = new BQIdentity();

		bqIdentity.setCreateDate(new Date());
		bqIdentity.setId(userId);

		String individualId = jsonObject.getString("individualId");

		if (StringUtils.isBlank(individualId) ||
			Objects.equals(individualId, _EMPTY_EMAIL_ADDRESS_HASHED)) {

			individualId = null;
		}

		bqIdentity.setIndividualId(individualId);

		return _bqIdentityRepository.insert(bqIdentity);
	}

	private static final String _EMPTY_EMAIL_ADDRESS_HASHED =
		"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

	@Autowired
	private BQIdentityRepository _bqIdentityRepository;

	@MessageSubscriber.Autowired(channel = Channel.IDENTITY_MESSAGE)
	private MessageSubscriber _messageSubscriber;

}