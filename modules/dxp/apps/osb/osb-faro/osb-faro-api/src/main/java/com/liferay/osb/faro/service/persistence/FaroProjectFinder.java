/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.service.persistence;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Matthew Kong
 * @generated
 */
@ProviderType
public interface FaroProjectFinder {

	public java.util.List<com.liferay.osb.faro.model.FaroProject>
		findByEmailAddressDomain(String emailAddressDomain);

}