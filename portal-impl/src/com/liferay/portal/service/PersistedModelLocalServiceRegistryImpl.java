/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.PermissionedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 * @author Connor McKay
 */
public class PersistedModelLocalServiceRegistryImpl
	implements PersistedModelLocalServiceRegistry {

	@Override
	public PersistedModelLocalService getPersistedModelLocalService(
		String className) {

		return _persistedModelLocalServices.get(className);
	}

	@Override
	public List<PersistedModelLocalService> getPersistedModelLocalServices() {
		return ListUtil.fromMapValues(_persistedModelLocalServices);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isPermissionedModelLocalService(String className) {
		PersistedModelLocalService persistedModelLocalService =
			getPersistedModelLocalService(className);

		if (persistedModelLocalService == null) {
			return false;
		}

		if (persistedModelLocalService instanceof
				PermissionedModelLocalService) {

			return true;
		}

		return false;
	}

	@Override
	public void register(
		String className,
		PersistedModelLocalService persistedModelLocalService) {

		PersistedModelLocalService oldPersistedModelLocalService =
			_persistedModelLocalServices.put(
				className, persistedModelLocalService);

		if ((oldPersistedModelLocalService != null) && _log.isWarnEnabled()) {
			_log.warn("Duplicate class name " + className);
		}
	}

	@Override
	public void unregister(String className) {
		_persistedModelLocalServices.remove(className);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PersistedModelLocalServiceRegistryImpl.class);

	private final Map<String, PersistedModelLocalService>
		_persistedModelLocalServices = new ConcurrentHashMap<>();

}