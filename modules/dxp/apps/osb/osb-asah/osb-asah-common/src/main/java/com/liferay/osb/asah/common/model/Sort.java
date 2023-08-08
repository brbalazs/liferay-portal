/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.findbugs.SuppressFBWarnings;

import java.util.Collections;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
@SuppressFBWarnings(
	{"EQ_DOESNT_OVERRIDE_EQUALS", "NM_SAME_SIMPLE_NAME_AS_SUPERCLASS"}
)
public class Sort extends org.springframework.data.domain.Sort {

	public static Sort asc(String column) {
		return new Sort(column, "asc");
	}

	public static Sort desc(String column) {
		return new Sort(column, "desc");
	}

	public static Sort of(Map<String, String> sort) {
		return new Sort(sort.get("column"), sort.get("type"));
	}

	public Sort() {
		super(Collections.emptyList());
	}

	public Sort(String column, String type) {
		super(
			Collections.singletonList(
				new Order(Direction.valueOf(type.toUpperCase()), column)));

		_column = column;
		_type = type.toUpperCase();
	}

	public String getColumn() {
		return _column;
	}

	public String getType() {
		return _type;
	}

	private String _column;
	private String _type;

}