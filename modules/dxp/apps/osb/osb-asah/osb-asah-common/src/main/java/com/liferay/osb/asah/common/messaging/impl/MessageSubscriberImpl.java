/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.pubsub.v1.AcknowledgeRequest;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.PullResponse;
import com.google.pubsub.v1.ReceivedMessage;
import com.google.pubsub.v1.Subscription;

import com.liferay.osb.asah.common.function.UnsafeFunction;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcellus Tavares
 */
public class MessageSubscriberImpl implements MessageSubscriber {

	public MessageSubscriberImpl(
		PubSubClientFactory pubSubClientFactory, Subscription subscription) {

		_pubSubClientFactory = pubSubClientFactory;
		_subscription = subscription;
	}

	@Override
	public <T> List<Message<T>> pullMessages(
		int maxMessages,
		UnsafeFunction<String, T, Exception> modelMapperUnsafeFunction) {

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"Pulling %s messages from %s", maxMessages,
					_subscription.getName()));
		}

		List<Message<T>> messages = new ArrayList<>();

		try (PubSubClient<SubscriberStub> subscriberStubPubSubClient =
				_pubSubClientFactory.createSubscriberStub()) {

			SubscriberStub subscriberStub = subscriberStubPubSubClient.get();

			UnaryCallable<PullRequest, PullResponse> pullRequestUnaryCallable =
				subscriberStub.pullCallable();

			PullResponse pullResponse = pullRequestUnaryCallable.call(
				_buildPullRequest(maxMessages));

			for (ReceivedMessage receivedMessage :
					pullResponse.getReceivedMessagesList()) {

				PubsubMessage pubsubMessage = receivedMessage.getMessage();

				ByteString byteString = pubsubMessage.getData();

				messages.add(
					new Message<>(
						receivedMessage.getAckId(),
						pubsubMessage.getAttributesMap(),
						pubsubMessage.getMessageId(),
						modelMapperUnsafeFunction.apply(
							byteString.toStringUtf8())));
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Received messages: " + messages);
		}

		return messages;
	}

	@Override
	public <T> void registerException(Exception exception, Message<T> message) {
		StackTraceElement[] stackTraceElements = _messageIds.getIfPresent(
			_subscription.getName() + "#" + message.getId());

		if ((stackTraceElements == null) ||
			!Arrays.equals(stackTraceElements, exception.getStackTrace())) {

			if (_log.isInfoEnabled()) {
				_log.info("Retrying message " + message.getObject());
			}

			_messageIds.put(
				_subscription.getName() + "#" + message.getId(),
				exception.getStackTrace());
		}
		else {
			_log.error("Discarding message " + message.getObject(), exception);

			sendAckIds(Collections.singletonList(message.getAckId()));

			_messageIds.invalidate(
				_subscription.getName() + "#" + message.getId());
		}
	}

	@Override
	public void sendAckIds(List<String> ackIds) {
		if (ackIds.isEmpty()) {
			return;
		}

		try (PubSubClient<SubscriberStub> subscriberStubPubSubClient =
				_pubSubClientFactory.createSubscriberStub()) {

			SubscriberStub subscriberStub = subscriberStubPubSubClient.get();

			UnaryCallable<AcknowledgeRequest, Empty>
				acknowledgeRequestUnaryCallable =
					subscriberStub.acknowledgeCallable();

			acknowledgeRequestUnaryCallable.call(
				_buildAcknowledgeRequest(ackIds));

			if (_log.isDebugEnabled()) {
				_log.debug("Successfully sent ack IDs " + ackIds);
			}
		}
		catch (Exception exception) {
			_log.error("Unable to send ack IDs " + ackIds, exception);
		}
	}

	private AcknowledgeRequest _buildAcknowledgeRequest(
		Iterable<String> ackIds) {

		AcknowledgeRequest.Builder builder = AcknowledgeRequest.newBuilder();

		builder.addAllAckIds(ackIds);
		builder.setSubscription(_subscription.getName());

		return builder.build();
	}

	private PullRequest _buildPullRequest(int maxMessages) {
		PullRequest.Builder builder = PullRequest.newBuilder();

		builder.setMaxMessages(maxMessages);
		builder.setReturnImmediately(true);
		builder.setSubscription(_subscription.getName());

		return builder.build();
	}

	private static final Log _log = LogFactory.getLog(
		MessageSubscriberImpl.class);

	private static final Cache<String, StackTraceElement[]> _messageIds =
		Caffeine.newBuilder(
		).expireAfterAccess(
			7, TimeUnit.DAYS
		).maximumSize(
			100000
		).build();

	private final PubSubClientFactory _pubSubClientFactory;
	private final Subscription _subscription;

}