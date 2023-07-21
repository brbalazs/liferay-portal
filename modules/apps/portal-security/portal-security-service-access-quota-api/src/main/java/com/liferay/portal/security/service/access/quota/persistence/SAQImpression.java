/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.service.access.quota.persistence;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

/**
 * @author Stian Sigvartsen
 */
@ProviderType
public interface SAQImpression {

	public long getCreatedMillis();

	public String getKey();

	public Map<String, String> getMetrics();

	public int getWeight();

}