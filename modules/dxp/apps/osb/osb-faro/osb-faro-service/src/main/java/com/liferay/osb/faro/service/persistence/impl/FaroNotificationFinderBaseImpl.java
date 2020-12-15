/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.service.persistence.impl;

import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.osb.faro.service.persistence.FaroNotificationPersistence;
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
public class FaroNotificationFinderBaseImpl
	extends BasePersistenceImpl<FaroNotification> {

	public FaroNotificationFinderBaseImpl() {
		setModelClass(FaroNotification.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("read", "read_");
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
		return getFaroNotificationPersistence().getBadColumnNames();
	}

	/**
	 * Returns the faro notification persistence.
	 *
	 * @return the faro notification persistence
	 */
	public FaroNotificationPersistence getFaroNotificationPersistence() {
		return faroNotificationPersistence;
	}

	/**
	 * Sets the faro notification persistence.
	 *
	 * @param faroNotificationPersistence the faro notification persistence
	 */
	public void setFaroNotificationPersistence(
		FaroNotificationPersistence faroNotificationPersistence) {

		this.faroNotificationPersistence = faroNotificationPersistence;
	}

	@BeanReference(type = FaroNotificationPersistence.class)
	protected FaroNotificationPersistence faroNotificationPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		FaroNotificationFinderBaseImpl.class);

}