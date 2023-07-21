/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.service.access.quota.metric;

import aQute.bnd.annotation.ProviderType;

import java.util.Set;

/**
 * @author Stian Sigvartsen
 */
@ProviderType
public interface SAQContextMatcher {

	public Set<String> getMetricNames();

	public boolean matches(String metricName, String metricValue);

}