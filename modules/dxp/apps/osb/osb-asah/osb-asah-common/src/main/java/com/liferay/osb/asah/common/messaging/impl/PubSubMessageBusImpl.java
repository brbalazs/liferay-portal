/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.messaging.MessageListener;
import com.liferay.osb.asah.common.messaging.MessageStreamingSubscriber;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class PubSubMessageBusImpl implements MessageBus {

	public PubsubMessage createPubsubMessage(String message) {
		return createPubsubMessage(message, null, null);
	}

	public PubsubMessage createPubsubMessage(
		String message, Map<String, String> messageAttributes,
		String orderingKey) {

		PubsubMessage.Builder builder = PubsubMessage.newBuilder();

		if ((messageAttributes != null) && !messageAttributes.isEmpty()) {
			builder.putAllAttributes(messageAttributes);
		}

		builder.setData(ByteString.copyFromUtf8(message));

		if (StringUtils.isNotEmpty(orderingKey)) {
			builder.setOrderingKey(orderingKey);
		}

		return builder.build();
	}

	public ProjectName getProjectName() {
		return ProjectName.of(_gcloudProjectId);
	}

	public ProjectTopicName getProjectTopicName(Channel channel) {
		return ProjectTopicName.of(_gcloudProjectId, _getTopicId(channel));
	}

	public PubSubClientFactory getPubSubClientFactory() {
		return _pubSubClientFactory;
	}

	@Override
	public void registerMessageListener(
		Channel channel, MessageListener messageListener) {

		try {
			Subscriber subscriber = _createSubscriber(channel, messageListener);

			_messageListeners.put(messageListener, subscriber);

			subscriber.startAsync();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Override
	public MessageStreamingSubscriber registerMessageStreamingSubscriber(
		Channel channel, String messageSubscriberName) {

		try {
			Subscription subscription = _getOrCreateSubscription(
				channel,
				_getProjectSubscriptionName(channel, messageSubscriberName));

			return new MessageStreamingSubscriberImpl(
				_pubSubClientFactory, subscription);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new IllegalStateException(exception);
		}
	}

	@Override
	public MessageSubscriber registerMessageSubscriber(
		Channel channel, String messageSubscriberName) {

		try {
			Subscription subscription = _getOrCreateSubscription(
				channel,
				_getProjectSubscriptionName(channel, messageSubscriberName));

			return new MessageSubscriberImpl(
				_pubSubClientFactory, subscription);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new IllegalStateException(exception);
		}
	}

	@Override
	public void sendMessage(Channel channel, String message) {
		sendMessage(channel, message, Collections.emptyMap());
	}

	@Override
	public void sendMessage(
		Channel channel, String message,
		Map<String, String> messageAttributes) {

		if (StringUtils.isBlank(message)) {
			throw new IllegalArgumentException("Message is blank");
		}

		try {
			Publisher publisher = _getOrCreatePublisher(channel);

			if (channel == Channel.DXP_ENTITIES_MESSAGE) {
				publisher.publish(
					createPubsubMessage(
						message, messageAttributes,
						Channel.DXP_ENTITIES_MESSAGE.name() + "_" +
							ProjectIdThreadLocal.getProjectId()));
			}
			else {
				publisher.publish(
					createPubsubMessage(message, messageAttributes, null));
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Override
	public void unregisterMessageListener(MessageListener messageListener) {
		Subscriber subscriber = _messageListeners.get(messageListener);

		if (subscriber == null) {
			throw new IllegalArgumentException(
				"Unable to unregister message listener " + messageListener);
		}

		subscriber.stopAsync();
	}

	private MessageReceiver _createMessageReceiver(
		MessageListener messageListener) {

		return (pubsubMessage, ackReplyConsumer) -> {
			ByteString byteString = pubsubMessage.getData();

			if (_log.isDebugEnabled()) {
				_log.debug("Received message: " + byteString.toStringUtf8());
			}

			messageListener.onMessage(byteString.toStringUtf8());

			ackReplyConsumer.ack();
		};
	}

	private Subscriber _createSubscriber(
			Channel channel, MessageListener messageListener)
		throws Exception {

		Subscription subscription = _getOrCreateSubscription(
			channel,
			_getProjectSubscriptionName(channel, messageListener.getClass()));

		return _pubSubClientFactory.createSubscriber(
			_createMessageReceiver(messageListener), subscription.getName());
	}

	private void _createTopic(
		ProjectTopicName projectTopicName, TopicAdminClient topicAdminClient) {

		try {
			topicAdminClient.createTopic(projectTopicName);

			if (_log.isInfoEnabled()) {
				_log.info("Created topic " + projectTopicName.toString());
			}
		}
		catch (AlreadyExistsException alreadyExistsException) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Topic already exists " + projectTopicName.toString());
			}
		}
	}

	private void _createTopics() throws Exception {
		try (PubSubClient<TopicAdminClient> pubSubClient =
				_pubSubClientFactory.createTopicAdminClient()) {

			TopicAdminClient topicAdminClient = pubSubClient.get();

			Set<String> topicNames = _fetchTopicNames(topicAdminClient);

			for (Channel channel : Channel.values()) {
				ProjectTopicName projectTopicName = getProjectTopicName(
					channel);

				if (!topicNames.contains(projectTopicName.toString())) {
					_createTopic(projectTopicName, topicAdminClient);
				}
			}
		}
	}

	@PreDestroy
	private void _destroy() {
		BoundedExecutor boundedExecutor = BoundedExecutor.newBoundedExecutor(
			10, 5);

		for (Subscriber subscriber : _messageListeners.values()) {
			boundedExecutor.runAsync(
				() -> {
					subscriber.stopAsync();

					try {
						subscriber.awaitTerminated(1, TimeUnit.MINUTES);
					}
					catch (TimeoutException timeoutException) {
						_log.error(
							"Timeout while waiting for termination of " +
								"subscriber",
							timeoutException);
					}
				});
		}

		for (Publisher publisher : _channels.values()) {
			boundedExecutor.runAsync(
				() -> {
					publisher.shutdown();

					try {
						publisher.awaitTermination(1, TimeUnit.MINUTES);
					}
					catch (InterruptedException interruptedException) {
						_log.error(
							"Interrupted while waiting for termination of " +
								"publisher",
							interruptedException);
					}
				});
		}

		boundedExecutor.awaitPendingTasks();

		boundedExecutor.shutdown();
	}

	private Set<String> _fetchTopicNames(TopicAdminClient topicAdminClient) {
		Set<String> topicNames = new HashSet<>();

		TopicAdminClient.ListTopicsPagedResponse listTopicsPagedResponse =
			topicAdminClient.listTopics(getProjectName());

		for (Topic topic : listTopicsPagedResponse.iterateAll()) {
			topicNames.add(topic.getName());
		}

		return topicNames;
	}

	private Publisher _getOrCreatePublisher(Channel channel) throws Exception {
		return _channels.computeIfAbsent(
			channel,
			key -> {
				try {
					return _pubSubClientFactory.createPublisher(
						key.isOrderingEnabled(), getProjectTopicName(key));
				}
				catch (Exception exception) {
					throw new IllegalStateException(exception);
				}
			});
	}

	private Subscription _getOrCreateSubscription(
			Channel channel, ProjectSubscriptionName projectSubscriptionName)
		throws Exception {

		PubSubClient<SubscriptionAdminClient> pubSubClient =
			_pubSubClientFactory.createSubscriptionAdminClient();

		SubscriptionAdminClient subscriptionAdminClient = pubSubClient.get();

		try {
			return subscriptionAdminClient.getSubscription(
				projectSubscriptionName);
		}
		catch (NotFoundException notFoundException) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Creating subscription " +
						projectSubscriptionName.toString());
			}

			ProjectTopicName projectTopicName = getProjectTopicName(channel);

			Subscription.Builder builder = Subscription.newBuilder();

			builder.setAckDeadlineSeconds(600);
			builder.setName(projectSubscriptionName.toString());
			builder.setPushConfig(PushConfig.getDefaultInstance());
			builder.setTopic(projectTopicName.toString());

			if (channel == Channel.DXP_ENTITIES_MESSAGE) {
				builder.setEnableMessageOrdering(true);
			}

			return subscriptionAdminClient.createSubscription(builder.build());
		}
		finally {
			pubSubClient.close();
		}
	}

	private ProjectSubscriptionName _getProjectSubscriptionName(
		Channel channel, Class<?> clazz) {

		return _getProjectSubscriptionName(channel, clazz.getName());
	}

	private ProjectSubscriptionName _getProjectSubscriptionName(
		Channel channel, String name) {

		return ProjectSubscriptionName.of(
			_gcloudProjectId,
			_getTopicId(channel) + "_" + StringUtils.replace(name, "$", "_"));
	}

	private String _getTopicId(Channel channel) {
		return StringUtils.lowerCase(
			String.format(
				"%s_%s", ServiceConstants.LCP_PROJECT_ID, channel.toString()));
	}

	@PostConstruct
	private void _init() throws Exception {
		_createTopics();
	}

	private static final Log _log = LogFactory.getLog(
		PubSubMessageBusImpl.class);

	private final Map<Channel, Publisher> _channels = new ConcurrentHashMap<>();

	@Value("${osb.asah.gcloud.project.id:liferaycloud-customer-ac}")
	private String _gcloudProjectId;

	private final Map<MessageListener, Subscriber> _messageListeners =
		new ConcurrentHashMap<>();

	@Autowired
	private PubSubClientFactory _pubSubClientFactory;

}