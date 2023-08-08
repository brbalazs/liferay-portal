/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;

/**
 * @author Marcellus Tavares
 */
public class PrometheusUtil {

	public static Counter counter(
		String name, String help, String... labelNames) {

		Counter.Builder builder = Counter.build();

		builder.help(help);
		builder.labelNames(labelNames);
		builder.name(name);

		return builder.register();
	}

	public static Gauge gauge(String name, String help, String... labelNames) {
		Gauge.Builder builder = Gauge.build();

		builder.help(help);
		builder.labelNames(labelNames);
		builder.name(name);

		return builder.register();
	}

	public static Histogram histogram(
		String name, String help, String... labelNames) {

		Histogram.Builder builder = Histogram.build();

		builder.help(help);
		builder.labelNames(labelNames);
		builder.name(name);

		return builder.register();
	}

}