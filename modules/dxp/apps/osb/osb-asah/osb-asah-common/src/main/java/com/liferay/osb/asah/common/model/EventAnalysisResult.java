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
public class EventAnalysisResult {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventAnalysisResult)) {
			return false;
		}

		EventAnalysisResult eventAnalysisResult = (EventAnalysisResult)obj;

		if (Objects.equals(
				_breakdownItems, eventAnalysisResult._breakdownItems) &&
			Objects.equals(_count, eventAnalysisResult._count) &&
			Objects.equals(_page, eventAnalysisResult._page) &&
			Objects.equals(
				_previousValue, eventAnalysisResult._previousValue) &&
			Objects.equals(_value, eventAnalysisResult._value)) {

			return true;
		}

		return false;
	}

	public List<BreakdownItem> getBreakdownItems() {
		return _breakdownItems;
	}

	public long getCount() {
		return _count;
	}

	public int getPage() {
		return _page;
	}

	public Number getPreviousValue() {
		return _previousValue;
	}

	public Number getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_breakdownItems, _count, _page, _previousValue, _value);
	}

	public void setBreakdownItems(List<BreakdownItem> breakdownItems) {
		_breakdownItems = breakdownItems;
	}

	public void setCount(long count) {
		_count = count;
	}

	public void setPage(int page) {
		_page = page;
	}

	public void setPreviousValue(Number previousValue) {
		_previousValue = previousValue;
	}

	public void setValue(Number value) {
		_value = value;
	}

	private List<BreakdownItem> _breakdownItems;
	private long _count;
	private int _page;
	private Number _previousValue;
	private Number _value;

}