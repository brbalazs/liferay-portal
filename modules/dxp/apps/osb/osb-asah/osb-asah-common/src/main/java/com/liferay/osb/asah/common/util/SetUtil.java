/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author André Miranda
 */
public class SetUtil {

	public static <T, R> Set<R> map(
		Collection<? extends T> collection,
		Function<? super T, ? extends R> mapperFunction) {

		if (collection == null) {
			return Collections.emptySet();
		}

		Stream<? extends T> stream = collection.stream();

		return stream.map(
			mapperFunction
		).collect(
			Collectors.toCollection(LinkedHashSet::new)
		);
	}

	public static <T> Set<T> of(T... elements) {
		return new LinkedHashSet<>(Arrays.asList(elements));
	}

}