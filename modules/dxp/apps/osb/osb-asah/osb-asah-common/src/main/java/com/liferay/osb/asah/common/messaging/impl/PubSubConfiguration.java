/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging.impl;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;
import com.liferay.osb.asah.common.util.URLUtil;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.URI;

import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcellus Tavares
 */
@Configuration
public class PubSubConfiguration {

	@Bean(name = "managedChannelSupplier")
	@ConditionalOnGoogleApplicationCredentials
	public Supplier<ManagedChannel> computeEngineManagedChannelSupplier() {
		return () -> null;
	}

	@Bean(name = "managedChannelSupplier")
	@ConditionalOnGoogleApplicationCredentials(matchIfMissing = true)
	public Supplier<ManagedChannel> emulatorManagedChannelSupplier() {
		return () -> {
			String target = ServiceConstants.URL_PUBSUB_EMULATOR;

			try {
				URI uri = URLUtil.toURI(target);

				target = uri.getAuthority();
			}
			catch (Exception exception) {
				_log.error(exception, exception);
			}

			ManagedChannelBuilder managedChannelBuilder =
				ManagedChannelBuilder.forTarget(target);

			managedChannelBuilder.usePlaintext();

			return managedChannelBuilder.build();
		};
	}

	private static final Log _log = LogFactory.getLog(
		PubSubConfiguration.class);

}