/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.wedeploy.data;

/**
 * @author Marcellus Tavares
 */
public enum WeDeployDataService {

	OSB_ASAH_CEREBRO_INFO("osbasahcerebroinfo"),
	OSB_ASAH_DXP_RAW("osbasahdxpraw"), OSB_ASAH_FARO_INFO("osbasahfaroinfo");

	@Override
	public String toString() {
		return _serviceName;
	}

	private WeDeployDataService(String serviceName) {
		_serviceName = serviceName;
	}

	private final String _serviceName;

}