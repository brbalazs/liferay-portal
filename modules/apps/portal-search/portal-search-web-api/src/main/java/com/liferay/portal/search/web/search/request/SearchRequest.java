/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.search.request;

import aQute.bnd.annotation.ProviderType;

/**
 * @author André de Oliveira
 */
@ProviderType
public interface SearchRequest {

	public void addSearchSettingsContributor(
		SearchSettingsContributor searchSettingsContributor);

	public void removeSearchSettingsContributor(
		SearchSettingsContributor searchSettingsContributor);

	public SearchResponse search();

}