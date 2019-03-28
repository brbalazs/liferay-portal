/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.dashboard.web.internal.servlet.taglib.model;

import com.liferay.frontend.taglib.chart.model.predictive.PredictiveChartConfig;

import java.util.Map;

/**
 * @author Andrea Di Giorgi
 */
public class CommerceDashboardPredictiveChartConfig
	extends PredictiveChartConfig {

	public Map<String, String> getColors() {
		return get("colors", Map.class);
	}

	public Map<String, Object> getLegend() {
		return get("legend", Map.class);
	}

	public void setColors(Map<String, String> colors) {
		set("colors", colors);
	}

	public void setLegend(Map<String, ?> legend) {
		set("legend", legend);
	}

}