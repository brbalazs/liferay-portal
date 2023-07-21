/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.service.persistence.impl;

import com.liferay.osb.faro.model.FaroUser;
import com.liferay.osb.faro.service.persistence.FaroUserPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Kong
 * @generated
 */
public class FaroUserFinderBaseImpl extends BasePersistenceImpl<FaroUser> {

	public FaroUserFinderBaseImpl() {
		setModelClass(FaroUser.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("key", "key_");

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
		return getFaroUserPersistence().getBadColumnNames();
	}

	/**
	 * Returns the faro user persistence.
	 *
	 * @return the faro user persistence
	 */
	public FaroUserPersistence getFaroUserPersistence() {
		return faroUserPersistence;
	}

	/**
	 * Sets the faro user persistence.
	 *
	 * @param faroUserPersistence the faro user persistence
	 */
	public void setFaroUserPersistence(
		FaroUserPersistence faroUserPersistence) {

		this.faroUserPersistence = faroUserPersistence;
	}

	@BeanReference(type = FaroUserPersistence.class)
	protected FaroUserPersistence faroUserPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		FaroUserFinderBaseImpl.class);

}