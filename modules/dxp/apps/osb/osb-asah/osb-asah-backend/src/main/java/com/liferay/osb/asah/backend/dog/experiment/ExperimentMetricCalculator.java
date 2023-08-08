/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.model.DXPVariantSettings;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public interface ExperimentMetricCalculator {

	public ExperimentMetric calculateExperimentMetric(Experiment experiment);

	public Long estimateDaysDuration(
		List<DXPVariantSettings> dxpVariantsSettings, Experiment experiment);

}