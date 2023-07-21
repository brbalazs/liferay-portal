/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.liferay;

/**
 * Represents a ServiceTrackerMap that also checks for company specialization. A
 * service is the best match if it is registered for both the company and the
 * key. If not such a service exists the best next candidate would be if it
 * matches only the key. If no service is registered for the key then a service
 * registered for the company only will be searched.
 *
 * @author Carlos Sierra Andrés
 * @see    com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap
 * @review
 */
public interface ScopedServiceTrackerMap<T> {

	public void close();

	public T getService(long companyId, String key);

}