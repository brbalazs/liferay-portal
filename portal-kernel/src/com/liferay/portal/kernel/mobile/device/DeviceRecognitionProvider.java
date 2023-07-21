/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.mobile.device;

import aQute.bnd.annotation.ProviderType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Milen Dyankov
 * @author Michael C. Han
 */
@ProviderType
public interface DeviceRecognitionProvider {

	public Device detectDevice(HttpServletRequest request);

	public KnownDevices getKnownDevices();

	public void reload() throws Exception;

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setDeviceCapabilityFilter(
		DeviceCapabilityFilter deviceCapabilityFilter);

}