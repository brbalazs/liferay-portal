/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.license.enterprise.app.internal;

import com.liferay.petra.lang.HashUtil;

import java.util.Objects;

/**
 * @author Tina Tian
 */
public class PortalLicenseEnterpriseAppBlockedBundleData {

	public PortalLicenseEnterpriseAppBlockedBundleData(
		String fragmentHost, String location, int startLevel,
		String webContextPath) {

		_fragmentHost = fragmentHost;
		_location = location;
		_startLevel = startLevel;
		_webContextPath = webContextPath;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PortalLicenseEnterpriseAppBlockedBundleData)) {
			return false;
		}

		PortalLicenseEnterpriseAppBlockedBundleData
			portalLicenseEnterpriseAppBlockedBundleData =
				(PortalLicenseEnterpriseAppBlockedBundleData)object;

		if (Objects.equals(
				_fragmentHost,
				portalLicenseEnterpriseAppBlockedBundleData._fragmentHost) &&
			Objects.equals(
				_location,
				portalLicenseEnterpriseAppBlockedBundleData._location) &&
			(_startLevel ==
				portalLicenseEnterpriseAppBlockedBundleData._startLevel) &&
			Objects.equals(
				_webContextPath,
				portalLicenseEnterpriseAppBlockedBundleData._webContextPath)) {

			return true;
		}

		return false;
	}

	public String getFragmentHost() {
		return _fragmentHost;
	}

	public String getLocation() {
		return _location;
	}

	public int getStartLevel() {
		return _startLevel;
	}

	public String getWebContextPath() {
		return _webContextPath;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, _fragmentHost);

		hashCode = HashUtil.hash(hashCode, _location);
		hashCode = HashUtil.hash(hashCode, _startLevel);

		return HashUtil.hash(hashCode, _webContextPath);
	}

	private final String _fragmentHost;
	private final String _location;
	private final int _startLevel;
	private final String _webContextPath;

}