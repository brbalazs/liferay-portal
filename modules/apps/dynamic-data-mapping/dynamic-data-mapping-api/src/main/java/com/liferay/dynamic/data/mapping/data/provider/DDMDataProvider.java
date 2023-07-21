/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider;

import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.List;

/**
 * @author Luca Comin
 */
public interface DDMDataProvider {

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #getData(DDMDataProviderRequest)}
	 */
	@Deprecated
	public List<KeyValuePair> getData(
			DDMDataProviderContext ddmDataProviderContext)
		throws DDMDataProviderException;

	public default DDMDataProviderResponse getData(
			DDMDataProviderRequest ddmDataProviderRequest)
		throws DDMDataProviderException {

		List<KeyValuePair> keyValuePairs = getData(
			ddmDataProviderRequest.getDDMDataProviderContext());

		DDMDataProviderResponseOutput defaultDDMDataProviderResponseOutput =
			DDMDataProviderResponseOutput.of(
				"Default-Output", "list", keyValuePairs);

		return DDMDataProviderResponse.of(defaultDDMDataProviderResponseOutput);
	}

	public Class<?> getSettings();

}