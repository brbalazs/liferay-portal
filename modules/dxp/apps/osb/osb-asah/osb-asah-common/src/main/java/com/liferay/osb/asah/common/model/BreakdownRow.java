/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcos Martins
 */
public class BreakdownRow {

	public BreakdownRow() {
	}

	public BreakdownRow(Map<String, Object> source) {
		for (Map.Entry<String, Object> entry : source.entrySet()) {
			_breakdownColumns.add(
				new BreakdownColumn(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BreakdownRow)) {
			return false;
		}

		BreakdownRow breakdownRow = (BreakdownRow)obj;

		if (Objects.equals(_breakdownColumns, breakdownRow._breakdownColumns)) {
			return true;
		}

		return false;
	}

	public BreakdownColumn getBreakdownColumn(int columnIndex) {
		if (columnIndex < getBreakdownColumnsCount()) {
			return _breakdownColumns.get(columnIndex);
		}

		return null;
	}

	public List<BreakdownColumn> getBreakdownColumns() {
		return _breakdownColumns;
	}

	public int getBreakdownColumnsCount() {
		return _breakdownColumns.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(_breakdownColumns);
	}

	@Override
	public String toString() {
		return _breakdownColumns.toString();
	}

	public static class BreakdownColumn {

		public BreakdownColumn() {
		}

		public BreakdownColumn(String name, Object value) {
			_name = name;
			_value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof BreakdownColumn)) {
				return false;
			}

			BreakdownColumn breakdownColumn = (BreakdownColumn)obj;

			if (Objects.equals(_name, breakdownColumn._name) &&
				Objects.equals(_value, breakdownColumn._value)) {

				return true;
			}

			return false;
		}

		public String getName() {
			return _name;
		}

		public Object getValue() {
			return _value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_name, _value);
		}

		@Override
		public String toString() {
			return "{name: " + _name + ", value: " + _value + "}";
		}

		private String _name;
		private Object _value;

	}

	private final List<BreakdownColumn> _breakdownColumns = new ArrayList<>();

}