/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Matthew Kong
 */
public class StreamUtil {

	public static <T, R> List<R> toList(List<T> values, Function<T, R> mapper) {
		Stream<T> stream = values.stream();

		return stream.map(
			mapper
		).collect(
			Collectors.toList()
		);
	}

	public static <T, R> List<R> toList(
		List<T> values, Predicate<T> filter, Function<T, R> mapper) {

		Stream<T> stream = values.stream();

		return stream.filter(
			filter
		).map(
			mapper
		).collect(
			Collectors.toList()
		);
	}

	public static <T, R> Map<String, R> toMap(
		List<T> values, Function<T, String> keyMapper,
		Function<T, R> valueMapper) {

		Stream<T> stream = values.stream();

		return stream.collect(
			Collectors.toMap(keyMapper, valueMapper, (key1, key2) -> key1));
	}

	public static <T, R> Map<String, R> toMap(
		List<T> values, Predicate<T> filter, Function<T, String> keyMapper,
		Function<T, R> valueMapper) {

		Stream<T> stream = values.stream();

		return stream.filter(
			filter
		).collect(
			Collectors.toMap(keyMapper, valueMapper, (key1, key2) -> key1)
		);
	}

}