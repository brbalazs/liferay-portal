/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.chat.service.persistence.impl;

import com.liferay.chat.model.Status;
import com.liferay.chat.service.persistence.StatusPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class StatusFinderBaseImpl extends BasePersistenceImpl<Status> {

	public StatusFinderBaseImpl() {
		setModelClass(Status.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("online", "online_");

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
				"_dbColumnNames");

			field.setAccessible(true);

			field.set(this, dbColumnNames);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}
	}

	@Override
	public Set<String> getBadColumnNames() {
		return getStatusPersistence().getBadColumnNames();
	}

	/**
	 * Returns the status persistence.
	 *
	 * @return the status persistence
	 */
	public StatusPersistence getStatusPersistence() {
		return statusPersistence;
	}

	/**
	 * Sets the status persistence.
	 *
	 * @param statusPersistence the status persistence
	 */
	public void setStatusPersistence(StatusPersistence statusPersistence) {
		this.statusPersistence = statusPersistence;
	}

	@BeanReference(type = StatusPersistence.class)
	protected StatusPersistence statusPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		StatusFinderBaseImpl.class);

}