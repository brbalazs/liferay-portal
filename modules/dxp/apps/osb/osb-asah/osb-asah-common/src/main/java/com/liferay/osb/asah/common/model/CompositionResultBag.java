/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.List;
import java.util.Objects;

/**
 * @author Matthew Kong
 */
public class CompositionResultBag extends ResultBag<Composition> {

	public CompositionResultBag() {
	}

	public CompositionResultBag(
		List<Composition> results, long total, long totalCount) {

		if (!results.isEmpty()) {
			Composition composition = results.get(0);

			_maxCount = composition.getCount();
		}

		this.results = results;
		this.total = total;
		_totalCount = totalCount;
	}

	public CompositionResultBag(
		long maxCount, List<Composition> results, long total, long totalCount) {

		_maxCount = maxCount;
		this.results = results;
		this.total = total;
		_totalCount = totalCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		Class<?> clazz = getClass();

		if ((obj == null) || !clazz.isAssignableFrom(obj.getClass())) {
			return false;
		}

		CompositionResultBag compositionResultBag = (CompositionResultBag)obj;

		if (Objects.equals(results, compositionResultBag.results) &&
			Objects.equals(total, compositionResultBag.total) &&
			Objects.equals(_totalCount, compositionResultBag._totalCount)) {

			return true;
		}

		return false;
	}

	public long getMaxCount() {
		return _maxCount;
	}

	public long getTotalCount() {
		return _totalCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(results, total, _totalCount);
	}

	public void setMaxCount(long maxCount) {
		_maxCount = maxCount;
	}

	public void setTotalCount(long totalCount) {
		_totalCount = totalCount;
	}

	private long _maxCount;
	private long _totalCount;

}