/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Rachael Koestartyo
 */
public class CohortHeatMapMetric extends HeatMapMetric {

	public CohortHeatMapMetric(
		String colDimension, Metric metric, Double retention,
		String rowDimension, String rowKey) {

		super(colDimension, metric, rowDimension);

		_retention = retention;
		_rowKey = rowKey;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj) || !(obj instanceof CohortHeatMapMetric)) {
			return false;
		}

		CohortHeatMapMetric cohortHeatMapMetric = (CohortHeatMapMetric)obj;

		if (super.equalsMetric(cohortHeatMapMetric) &&
			Objects.equals(_retention, cohortHeatMapMetric._retention) &&
			Objects.equals(_rowKey, cohortHeatMapMetric._rowKey)) {

			return true;
		}

		return false;
	}

	public Double getRetention() {
		return _retention;
	}

	public String getRowKey() {
		return _rowKey;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Objects.hash(_retention, _rowKey);
	}

	public void setRetention(Double retention) {
		_retention = retention;
	}

	public void setRowKey(String rowKey) {
		_rowKey = rowKey;
	}

	private Double _retention;
	private String _rowKey;

}