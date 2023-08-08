/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging;

import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public interface MessageBus {

	public void registerMessageListener(
		Channel channel, MessageListener messageListener);

	public MessageStreamingSubscriber registerMessageStreamingSubscriber(
		Channel channel, String messageSubscriberName);

	public MessageSubscriber registerMessageSubscriber(
		Channel channel, String messageSubscriberName);

	public void sendMessage(Channel channel, String message);

	public void sendMessage(
		Channel channel, String message, Map<String, String> messageAttributes);

	public void unregisterMessageListener(MessageListener messageListener);

}