/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Thiago Buarque
 */
public class CohortMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CohortMetric)) {
			return false;
		}

		CohortMetric cohortMetric = (CohortMetric)obj;

		if (Objects.equals(
				_anonymousCohortHeatMapMetrics,
				cohortMetric._anonymousCohortHeatMapMetrics) &&
			Objects.equals(
				_knownCohortHeatMapMetrics,
				cohortMetric._knownCohortHeatMapMetrics) &&
			Objects.equals(
				_visitorsCohortHeatMapMetrics,
				cohortMetric._visitorsCohortHeatMapMetrics)) {

			return true;
		}

		return false;
	}

	public List<CohortHeatMapMetric> getAnonymousCohortHeatMapMetrics() {
		return _anonymousCohortHeatMapMetrics;
	}

	public List<CohortHeatMapMetric> getKnownCohortHeatMapMetrics() {
		return _knownCohortHeatMapMetrics;
	}

	public List<CohortHeatMapMetric> getVisitorsCohortHeatMapMetrics() {
		return _visitorsCohortHeatMapMetrics;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_anonymousCohortHeatMapMetrics, _knownCohortHeatMapMetrics,
			_visitorsCohortHeatMapMetrics);
	}

	public void setAnonymousCohortHeatMapMetrics(
		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics) {

		_anonymousCohortHeatMapMetrics = anonymousCohortHeatMapMetrics;
	}

	public void setKnownCohortHeatMapMetrics(
		List<CohortHeatMapMetric> knownCohortHeatMapMetrics) {

		_knownCohortHeatMapMetrics = knownCohortHeatMapMetrics;
	}

	public void setVisitorsCohortHeatMapMetrics(
		List<CohortHeatMapMetric> visitorsCohortHeatMapMetrics) {

		_visitorsCohortHeatMapMetrics = visitorsCohortHeatMapMetrics;
	}

	private List<CohortHeatMapMetric> _anonymousCohortHeatMapMetrics =
		new ArrayList<>();
	private List<CohortHeatMapMetric> _knownCohortHeatMapMetrics =
		new ArrayList<>();
	private List<CohortHeatMapMetric> _visitorsCohortHeatMapMetrics =
		new ArrayList<>();

}