/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.instance.lifecycle;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.Company;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface PortalInstanceLifecycleListener {

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void portalInstancePreregistered(long companyId);

	public void portalInstanceRegistered(Company company) throws Exception;

	public void portalInstanceUnregistered(Company company) throws Exception;

}