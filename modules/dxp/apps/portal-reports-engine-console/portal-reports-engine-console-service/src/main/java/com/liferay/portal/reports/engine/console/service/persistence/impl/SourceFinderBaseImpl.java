/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.reports.engine.console.service.persistence.impl;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.reports.engine.console.model.Source;
import com.liferay.portal.reports.engine.console.service.persistence.SourcePersistence;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SourceFinderBaseImpl extends BasePersistenceImpl<Source> {

	public SourceFinderBaseImpl() {
		setModelClass(Source.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

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
		return getSourcePersistence().getBadColumnNames();
	}

	/**
	 * Returns the source persistence.
	 *
	 * @return the source persistence
	 */
	public SourcePersistence getSourcePersistence() {
		return sourcePersistence;
	}

	/**
	 * Sets the source persistence.
	 *
	 * @param sourcePersistence the source persistence
	 */
	public void setSourcePersistence(SourcePersistence sourcePersistence) {
		this.sourcePersistence = sourcePersistence;
	}

	@BeanReference(type = SourcePersistence.class)
	protected SourcePersistence sourcePersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		SourceFinderBaseImpl.class);

}