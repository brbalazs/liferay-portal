/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import org.springframework.data.domain.Sort;

/**
 * @author Marcellus Tavares
 */
public class SortUtil {

	public static Sort getSort(Sort defaultSort, String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return defaultSort;
		}

		List<Sort.Order> orders = new ArrayList<>();

		for (int i = 0; i < (sorts.length - 1); i = i + 2) {
			String sort = sorts[i];

			if (Objects.equals(sorts[i + 1], "asc")) {
				orders.add(Sort.Order.asc(sort));
			}
			else {
				orders.add(Sort.Order.desc(sort));
			}
		}

		return Sort.by(orders);
	}

	public static Sort getSort(String[] sorts) {
		return getSort(Sort.by(Sort.Order.desc("id")), sorts);
	}

}