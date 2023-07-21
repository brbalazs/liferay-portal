/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sync.service.persistence.impl;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.sync.model.SyncDLObject;
import com.liferay.sync.service.persistence.SyncDLObjectPersistence;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SyncDLObjectFinderBaseImpl
	extends BasePersistenceImpl<SyncDLObject> {

	public SyncDLObjectFinderBaseImpl() {
		setModelClass(SyncDLObject.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("size", "size_");
		dbColumnNames.put("type", "type_");

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
		return getSyncDLObjectPersistence().getBadColumnNames();
	}

	/**
	 * Returns the sync dl object persistence.
	 *
	 * @return the sync dl object persistence
	 */
	public SyncDLObjectPersistence getSyncDLObjectPersistence() {
		return syncDLObjectPersistence;
	}

	/**
	 * Sets the sync dl object persistence.
	 *
	 * @param syncDLObjectPersistence the sync dl object persistence
	 */
	public void setSyncDLObjectPersistence(
		SyncDLObjectPersistence syncDLObjectPersistence) {

		this.syncDLObjectPersistence = syncDLObjectPersistence;
	}

	@BeanReference(type = SyncDLObjectPersistence.class)
	protected SyncDLObjectPersistence syncDLObjectPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		SyncDLObjectFinderBaseImpl.class);

}