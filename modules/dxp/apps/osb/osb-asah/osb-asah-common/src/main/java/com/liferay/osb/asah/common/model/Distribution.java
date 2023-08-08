/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rachael Koestartyo
 */
public class Distribution {

	public Distribution() {
	}

	public Distribution(Integer count, List<Object> values) {
		_count = count;
		_values = values;
	}

	public Distribution(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Distribution)) {
			return false;
		}

		Distribution distribution = (Distribution)obj;

		if (Objects.equals(_count, distribution._count) &&
			Objects.equals(_values, distribution._values)) {

			return true;
		}

		return false;
	}

	public Integer getCount() {
		return _count;
	}

	public List<Object> getValues() {
		return _values;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_count, _values);
	}

	public void setCount(Integer count) {
		_count = count;
	}

	public void setValues(List<Object> values) {
		_values = values;
	}

	private Integer _count;
	private List<Object> _values;

}