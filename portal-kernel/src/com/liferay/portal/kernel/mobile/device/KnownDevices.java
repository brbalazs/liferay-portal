/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.mobile.device;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;
import java.util.Set;

/**
 * @author Milen Dyankov
 * @author Michael C. Han
 */
@ProviderType
public interface KnownDevices {

	public Set<VersionableName> getBrands();

	public Set<VersionableName> getBrowsers();

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public Map<Capability, Set<String>> getDeviceIds();

	public Set<VersionableName> getOperatingSystems();

	public Set<String> getPointingMethods();

	public void reload() throws Exception;

}