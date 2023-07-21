/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.internal.increment;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Preston Crary
 */
public class BufferedIncrementProcessorUtil {

	public static BufferedIncrementProcessor getBufferedIncrementProcessor(
		String configuration) {

		Map.Entry<BufferedIncrementConfiguration, BufferedIncrementProcessor>
			entry = _bufferedIncrementProcessors.computeIfAbsent(
				configuration,
				key -> {
					BufferedIncrementConfiguration
						bufferedIncrementConfiguration =
							new BufferedIncrementConfiguration(key);

					BufferedIncrementProcessor bufferedIncrementProcessor =
						null;

					if (bufferedIncrementConfiguration.isEnabled()) {
						bufferedIncrementProcessor =
							new BufferedIncrementProcessor(
								bufferedIncrementConfiguration, key);
					}

					return new AbstractMap.SimpleImmutableEntry<>(
						bufferedIncrementConfiguration,
						bufferedIncrementProcessor);
				});

		return entry.getValue();
	}

	public void destroy() {
		for (Map.Entry<?, BufferedIncrementProcessor> entry :
				_bufferedIncrementProcessors.values()) {

			BufferedIncrementProcessor bufferedIncrementProcessor =
				entry.getValue();

			if (bufferedIncrementProcessor != null) {
				bufferedIncrementProcessor.destroy();
			}
		}
	}

	private static final Map
		<String,
		 Map.Entry<BufferedIncrementConfiguration, BufferedIncrementProcessor>>
			_bufferedIncrementProcessors = new ConcurrentHashMap<>();

}