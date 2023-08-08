/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.model.HeatMapMetric;
import com.liferay.osb.asah.common.model.TimeRange;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class SiteVisitorHeatMapDog {

	public List<HeatMapMetric> getHeatMapMetrics(
		String assetId, String channelId, TimeRange timeRange) {

		return Collections.emptyList();
	}

}