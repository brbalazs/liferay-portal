/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.registry;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Shuyang Zhou
 */
public class ServiceRankingUtil {

	public static int compare(
		ServiceReference<?> serviceReference1,
		ServiceReference<?> serviceReference2) {

		int value = Integer.compare(
			_getServiceRanking(serviceReference1),
			_getServiceRanking(serviceReference2));

		if (value != 0) {
			return value;
		}

		return -Long.compare(
			(Long)serviceReference1.getProperty("service.id"),
			(Long)serviceReference2.getProperty("service.id"));
	}

	public static <S, T> Optional<Map.Entry<ServiceReference<S>, T>>
		getHighestRankingEntry(Map<ServiceReference<S>, T> services) {

		Set<Map.Entry<ServiceReference<S>, T>> entrySet = services.entrySet();

		Stream<Map.Entry<ServiceReference<S>, T>> stream = entrySet.stream();

		return stream.max(
			Comparator.comparing(
				Map.Entry::getKey, ServiceRankingUtil::compare));
	}

	private static int _getServiceRanking(
		ServiceReference<?> serviceReference) {

		Object serviceRanking = serviceReference.getProperty("service.ranking");

		if (serviceRanking instanceof Integer) {
			return (Integer)serviceRanking;
		}

		if (serviceRanking instanceof String) {
			try {
				return Integer.parseInt((String)serviceRanking);
			}
			catch (NumberFormatException nfe) {
			}
		}

		return 0;
	}

}