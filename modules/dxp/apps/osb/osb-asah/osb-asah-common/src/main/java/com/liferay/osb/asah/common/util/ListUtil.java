/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marcellus Tavares
 */
public class ListUtil {

	public static <T, R> List<R> map(
		Collection<? extends T> collection,
		Function<? super T, ? extends R> mapperFunction) {

		if (collection == null) {
			return Collections.emptyList();
		}

		Stream<? extends T> stream = collection.stream();

		return stream.map(
			mapperFunction
		).collect(
			Collectors.toList()
		);
	}

}