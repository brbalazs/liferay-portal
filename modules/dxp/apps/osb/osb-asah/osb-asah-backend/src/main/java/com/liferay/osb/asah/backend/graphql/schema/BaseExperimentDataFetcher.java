/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.model.ExperimentSettings;
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class BaseExperimentDataFetcher {

	protected ExperimentSettings createExperimentSettings(
		Map<String, Object> experimentSettingsMap) {

		if (experimentSettingsMap == null) {
			return null;
		}

		ExperimentSettings experimentSettings = new ExperimentSettings();

		experimentSettings.setConfidenceLevel(
			(Double)experimentSettingsMap.get("confidenceLevel"));
		experimentSettings.setDXPVariantsSettings(
			_createDXPVariantSettings(
				(List<Map<String, Object>>)experimentSettingsMap.get(
					"dxpVariantsSettings")));

		return experimentSettings;
	}

	private List<DXPVariantSettings> _createDXPVariantSettings(
		List<Map<String, Object>> dxpVariantsSettingsMap) {

		return ListUtil.map(
			dxpVariantsSettingsMap,
			dxpVariantSettingsMap -> new DXPVariantSettings(
				(boolean)dxpVariantSettingsMap.get("control"),
				(String)dxpVariantSettingsMap.get("dxpVariantId"),
				(Double)dxpVariantSettingsMap.get("trafficSplit")));
	}

}