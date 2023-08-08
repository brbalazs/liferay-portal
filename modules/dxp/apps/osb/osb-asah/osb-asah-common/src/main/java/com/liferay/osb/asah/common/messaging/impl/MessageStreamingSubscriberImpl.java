/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.google.api.core.ApiService;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.Subscription;

import com.liferay.osb.asah.common.messaging.MessageStreamingSubscriber;

/**
 * @author Robson Pastor
 */
public class MessageStreamingSubscriberImpl
	implements MessageStreamingSubscriber {

	public MessageStreamingSubscriberImpl(
		PubSubClientFactory pubSubClientFactory, Subscription subscription) {

		_pubSubClientFactory = pubSubClientFactory;
		_subscription = subscription;
	}

	@Override
	public void subscribe(
		long maxOutstandingMessages, MessageReceiver messageReceiver) {

		Subscriber subscriber = _pubSubClientFactory.createSubscriber(
			maxOutstandingMessages, messageReceiver, _subscription.getName());

		ApiService apiService = subscriber.startAsync();

		apiService.awaitRunning();

		subscriber.awaitTerminated();
	}

	private final PubSubClientFactory _pubSubClientFactory;
	private final Subscription _subscription;

}