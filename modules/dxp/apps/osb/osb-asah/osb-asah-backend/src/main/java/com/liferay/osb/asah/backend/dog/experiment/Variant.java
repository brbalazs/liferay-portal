/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward Kwok-Yu Wong
 */
public class Variant<T> {

	public Variant(boolean control, String dxpVariantId, Double trafficSplit) {
		_control = control;
		_dxpVariantId = dxpVariantId;
		_trafficSplit = trafficSplit;
	}

	public void addExperimentDataPoint(
		ExperimentDataPoint<T> experimentDataPoint) {

		_experimentDataPoints.add(experimentDataPoint);
	}

	public String getDXPVariantId() {
		return _dxpVariantId;
	}

	public long getEstimatedSampleSize() {
		return _estimatedSampleSize;
	}

	public double getEstimatedTrafficRate() {
		return _estimatedTrafficRate;
	}

	public List<ExperimentDataPoint<T>> getExperimentDataPoints() {
		return _experimentDataPoints;
	}

	@JsonIgnore
	public long getFailures() {
		return _trials - _successes;
	}

	public long getSuccesses() {
		return _successes;
	}

	public double getSuccessRate() {
		return _successRate;
	}

	public double getTrafficSplit() {
		return _trafficSplit;
	}

	public long getTrials() {
		return _trials;
	}

	public boolean isControl() {
		return _control;
	}

	public void setEstimatedSampleSize(long estimatedSampleSize) {
		_estimatedSampleSize = estimatedSampleSize;
	}

	public void setEstimatedTrafficRate(double estimatedTrafficRate) {
		_estimatedTrafficRate = estimatedTrafficRate;
	}

	public void setExperimentDataPoints(
		List<ExperimentDataPoint<T>> experimentDataPoints) {

		_experimentDataPoints = experimentDataPoints;
	}

	public void setSuccesses(long successes) {
		_successes = successes;
	}

	public void setSuccessRate(double successRate) {
		_successRate = successRate;
	}

	public void setTrials(long trials) {
		_trials = trials;
	}

	private final boolean _control;
	private final String _dxpVariantId;
	private long _estimatedSampleSize;
	private double _estimatedTrafficRate;
	private List<ExperimentDataPoint<T>> _experimentDataPoints =
		new ArrayList<>();
	private long _successes;
	private double _successRate;
	private final double _trafficSplit;
	private long _trials;

}