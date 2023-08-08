/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcellus Tavares
 */
public class PubSubClient<T extends AutoCloseable> implements AutoCloseable {

	public PubSubClient(T client, ManagedChannel managedChannel) {
		_client = client;
		_managedChannel = managedChannel;
	}

	@Override
	public void close() throws Exception {
		_client.close();

		if (_managedChannel != null) {
			_managedChannel.shutdown();

			try {
				if (!_managedChannel.awaitTermination(1, TimeUnit.MINUTES)) {
					_managedChannel.shutdownNow();
				}
			}
			catch (InterruptedException interruptedException) {
				_log.error(
					"Interrupted while waiting for termination of managed " +
						"channel",
					interruptedException);
			}
		}
	}

	public T get() {
		return _client;
	}

	private static final Log _log = LogFactory.getLog(PubSubClient.class);

	private final T _client;
	private final ManagedChannel _managedChannel;

}