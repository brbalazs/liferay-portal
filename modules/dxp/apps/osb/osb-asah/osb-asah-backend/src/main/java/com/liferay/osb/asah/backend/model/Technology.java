/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.constants.DataConstants;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Adolfo Pérez
 */
public class Technology {

	public static Technology any() {
		return new Technology(
			DataConstants.ANY, DataConstants.ANY, DataConstants.ANY);
	}

	public static Technology browserName(String browserName) {
		return new Technology(
			DataConstants.ANY, browserName, DataConstants.ANY);
	}

	public static Technology deviceType(String deviceType) {
		return new Technology(deviceType, DataConstants.ANY, DataConstants.ANY);
	}

	public static Technology platformName(String platformName) {
		return new Technology(
			DataConstants.ANY, DataConstants.ANY, platformName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Technology)) {
			return false;
		}

		Technology technology = (Technology)obj;

		if (Objects.equals(_browserName, technology._browserName) &&
			Objects.equals(_deviceType, technology._deviceType) &&
			Objects.equals(_platformName, technology._platformName)) {

			return true;
		}

		return false;
	}

	public String getBrowserName() {
		return _browserName;
	}

	public String getDeviceType() {
		return _deviceType;
	}

	public String getPlatformName() {
		return _platformName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_browserName, _deviceType, _platformName);
	}

	public boolean isMobile() {
		if (Objects.equals(_deviceType, DataConstants.DEVICE_TYPE_MOBILE) ||
			Objects.equals(
				_deviceType, DataConstants.DEVICE_TYPE_SMART_PHONE) ||
			Objects.equals(_deviceType, DataConstants.DEVICE_TYPE_TABLET)) {

			return true;
		}

		return false;
	}

	private Technology(
		String deviceType, String browserName, String platformName) {

		if (StringUtils.isEmpty(deviceType)) {
			deviceType = DataConstants.ANY;
		}

		_deviceType = deviceType;

		if (StringUtils.isEmpty(browserName)) {
			browserName = DataConstants.ANY;
		}

		_browserName = browserName;

		if (StringUtils.isEmpty(platformName)) {
			platformName = DataConstants.ANY;
		}

		_platformName = platformName;
	}

	private final String _browserName;
	private final String _deviceType;
	private final String _platformName;

}