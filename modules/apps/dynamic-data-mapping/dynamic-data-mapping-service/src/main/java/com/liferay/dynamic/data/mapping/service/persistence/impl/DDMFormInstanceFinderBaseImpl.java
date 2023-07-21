/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.service.persistence.impl;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstancePersistence;
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
public class DDMFormInstanceFinderBaseImpl
	extends BasePersistenceImpl<DDMFormInstance> {

	public DDMFormInstanceFinderBaseImpl() {
		setModelClass(DDMFormInstance.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put("settings", "settings_");

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
		return getDDMFormInstancePersistence().getBadColumnNames();
	}

	/**
	 * Returns the ddm form instance persistence.
	 *
	 * @return the ddm form instance persistence
	 */
	public DDMFormInstancePersistence getDDMFormInstancePersistence() {
		return ddmFormInstancePersistence;
	}

	/**
	 * Sets the ddm form instance persistence.
	 *
	 * @param ddmFormInstancePersistence the ddm form instance persistence
	 */
	public void setDDMFormInstancePersistence(
		DDMFormInstancePersistence ddmFormInstancePersistence) {

		this.ddmFormInstancePersistence = ddmFormInstancePersistence;
	}

	@BeanReference(type = DDMFormInstancePersistence.class)
	protected DDMFormInstancePersistence ddmFormInstancePersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormInstanceFinderBaseImpl.class);

}