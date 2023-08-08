/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class ExperimentMetricDog {

	public ExperimentMetric calculateExperimentMetric(Experiment experiment) {
		ExperimentMetricCalculator experimentMetricCalculator =
			_getExperimentMetricCalculator(experiment);

		return experimentMetricCalculator.calculateExperimentMetric(experiment);
	}

	public Long estimateDaysDuration(
		List<DXPVariantSettings> dxpVariantsSettings, Experiment experiment) {

		ExperimentMetricCalculator experimentMetricCalculator =
			_getExperimentMetricCalculator(experiment);

		return experimentMetricCalculator.estimateDaysDuration(
			dxpVariantsSettings, experiment);
	}

	private ExperimentMetricCalculator _getExperimentMetricCalculator(
		Experiment experiment) {

		Goal goal = experiment.getGoal();

		if (goal == null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Experiment is missing goal metric");
		}

		return _dichotomousDataExperimentMetricCalculator;
	}

	@Autowired
	private DichotomousDataExperimentMetricCalculator
		_dichotomousDataExperimentMetricCalculator;

}