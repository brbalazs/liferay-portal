/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

/**
 * @author Edward Kwok-Yu Wong
 */
public class ExperimentDataPoint<T> {

	public ExperimentDataPoint(long trials, T value) {
		_trials = trials;
		_value = value;
	}

	public long getTrials() {
		return _trials;
	}

	public T getValue() {
		return _value;
	}

	public void setTrials(long trials) {
		_trials = trials;
	}

	public void setValue(T value) {
		_value = value;
	}

	private long _trials;
	private T _value;

}