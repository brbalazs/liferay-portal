/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.messaging;

import com.liferay.osb.asah.common.messaging.Channel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author Riccardo Ferrari
 */
@Component
public class DXPEntitiesChannels {

	public DXPEntitiesChannels() {
		_channels.put(
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Order",
			Channel.DXP_ENTITIES_ORDER);
		_channels.put(
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Product",
			Channel.DXP_ENTITIES_PRODUCT);
	}

	public Channel getChannel(String resourceName) {
		return _channels.getOrDefault(resourceName, _DEFAULT_CHANNEL);
	}

	private static final Channel _DEFAULT_CHANNEL =
		Channel.DXP_ENTITIES_DEFAULT;

	private static final Map<String, Channel> _channels = new HashMap<>();

}