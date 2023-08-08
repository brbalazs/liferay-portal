/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.ProjectTopicName;

import io.grpc.ManagedChannel;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class PubSubClientFactory {

	public Publisher createPublisher(
			boolean enableOrdering, ProjectTopicName projectTopicName)
		throws Exception {

		Publisher.Builder builder = Publisher.newBuilder(projectTopicName);

		builder.setEnableMessageOrdering(enableOrdering);

		ManagedChannel managedChannel = _managedChannelSupplier.get();

		_setClientSettingsBuilderProviders(
			builder::setCredentialsProvider, builder::setChannelProvider,
			managedChannel);

		return builder.build();
	}

	public Subscriber createSubscriber(
		long maxOutstandingMessages, MessageReceiver messageReceiver,
		String projectSubscriptionName) {

		FlowControlSettings flowControlSettings =
			FlowControlSettings.newBuilder(
			).setMaxOutstandingElementCount(
				maxOutstandingMessages
			).build();

		return _createSubscriber(
			flowControlSettings, messageReceiver, projectSubscriptionName);
	}

	public Subscriber createSubscriber(
		MessageReceiver messageReceiver, String projectSubscriptionName) {

		return _createSubscriber(
			null, messageReceiver, projectSubscriptionName);
	}

	public PubSubClient<SubscriberStub> createSubscriberStub()
		throws Exception {

		SubscriberStubSettings.Builder builder =
			SubscriberStubSettings.newBuilder();

		ManagedChannel managedChannel = _managedChannelSupplier.get();

		_setClientSettingsBuilderProviders(
			builder::setCredentialsProvider,
			builder::setTransportChannelProvider, managedChannel);

		return new PubSubClient(
			GrpcSubscriberStub.create(builder.build()), managedChannel);
	}

	public PubSubClient<SubscriptionAdminClient> createSubscriptionAdminClient()
		throws Exception {

		SubscriptionAdminSettings.Builder builder =
			SubscriptionAdminSettings.newBuilder();

		ManagedChannel managedChannel = _managedChannelSupplier.get();

		_setClientSettingsBuilderProviders(
			builder::setCredentialsProvider,
			builder::setTransportChannelProvider, managedChannel);

		return new PubSubClient(
			SubscriptionAdminClient.create(builder.build()), managedChannel);
	}

	public PubSubClient<TopicAdminClient> createTopicAdminClient()
		throws Exception {

		TopicAdminSettings.Builder builder = TopicAdminSettings.newBuilder();

		ManagedChannel managedChannel = _managedChannelSupplier.get();

		_setClientSettingsBuilderProviders(
			builder::setCredentialsProvider,
			builder::setTransportChannelProvider, managedChannel);

		return new PubSubClient(
			TopicAdminClient.create(builder.build()), managedChannel);
	}

	private Subscriber _createSubscriber(
		@Nullable FlowControlSettings flowControlSettings,
		MessageReceiver messageReceiver, String projectSubscriptionName) {

		Subscriber.Builder builder = Subscriber.newBuilder(
			projectSubscriptionName, messageReceiver);

		if (flowControlSettings != null) {
			builder.setFlowControlSettings(flowControlSettings);
		}

		ManagedChannel managedChannel = _managedChannelSupplier.get();

		_setClientSettingsBuilderProviders(
			builder::setCredentialsProvider, builder::setChannelProvider,
			managedChannel);

		return builder.build();
	}

	private void _setClientSettingsBuilderProviders(
		Function<CredentialsProvider, ?> credentialsProviderSetterFunction,
		Function<TransportChannelProvider, ?>
			transportChannelProviderSetterFunction,
		ManagedChannel managedChannel) {

		if (managedChannel == null) {
			return;
		}

		credentialsProviderSetterFunction.apply(NoCredentialsProvider.create());
		transportChannelProviderSetterFunction.apply(
			FixedTransportChannelProvider.create(
				GrpcTransportChannel.create(managedChannel)));
	}

	@Autowired
	@Qualifier("managedChannelSupplier")
	private Supplier<ManagedChannel> _managedChannelSupplier;

}