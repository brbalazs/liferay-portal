/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class HeatMapMetric extends Metric {

	public HeatMapMetric(
		String colDimension, Metric metric, String rowDimension) {

		super(metric.getMetricType());

		_colDimension = colDimension;
		_rowDimension = rowDimension;

		setValue(metric.getValue());
		setValueKey(metric.getValueKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj) || !(obj instanceof HeatMapMetric)) {
			return false;
		}

		HeatMapMetric heatMapMetric = (HeatMapMetric)obj;

		if (super.equalsMetric(heatMapMetric) &&
			Objects.equals(_colDimension, heatMapMetric._colDimension) &&
			Objects.equals(_rowDimension, heatMapMetric._rowDimension)) {

			return true;
		}

		return false;
	}

	public String getColDimension() {
		return _colDimension;
	}

	public String getRowDimension() {
		return _rowDimension;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Objects.hash(_colDimension, _rowDimension);
	}

	private final String _colDimension;
	private final String _rowDimension;

}