/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.service.persistence.impl;

import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.persistence.DDMDataProviderInstancePersistence;
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
public class DDMDataProviderInstanceFinderBaseImpl
	extends BasePersistenceImpl<DDMDataProviderInstance> {

	public DDMDataProviderInstanceFinderBaseImpl() {
		setModelClass(DDMDataProviderInstance.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
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
		return getDDMDataProviderInstancePersistence().getBadColumnNames();
	}

	/**
	 * Returns the ddm data provider instance persistence.
	 *
	 * @return the ddm data provider instance persistence
	 */
	public DDMDataProviderInstancePersistence
		getDDMDataProviderInstancePersistence() {

		return ddmDataProviderInstancePersistence;
	}

	/**
	 * Sets the ddm data provider instance persistence.
	 *
	 * @param ddmDataProviderInstancePersistence the ddm data provider instance persistence
	 */
	public void setDDMDataProviderInstancePersistence(
		DDMDataProviderInstancePersistence ddmDataProviderInstancePersistence) {

		this.ddmDataProviderInstancePersistence =
			ddmDataProviderInstancePersistence;
	}

	@BeanReference(type = DDMDataProviderInstancePersistence.class)
	protected DDMDataProviderInstancePersistence
		ddmDataProviderInstancePersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderInstanceFinderBaseImpl.class);

}