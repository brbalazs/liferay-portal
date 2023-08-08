/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.common.model.MetricType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class VisitorHistogramDog {

	@Autowired
	public VisitorHistogramDog() {
	}

	public HistogramMetricBag getHistogramMetricBag(
		boolean includePrevious, MetricType metricType,
		SearchQueryContext searchQueryContext) {

		return new HistogramMetricBag();
	}

}