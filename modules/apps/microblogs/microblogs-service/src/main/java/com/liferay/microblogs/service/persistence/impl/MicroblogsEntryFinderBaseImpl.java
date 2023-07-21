/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.microblogs.service.persistence.impl;

import com.liferay.microblogs.model.MicroblogsEntry;
import com.liferay.microblogs.service.persistence.MicroblogsEntryPersistence;
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
public class MicroblogsEntryFinderBaseImpl
	extends BasePersistenceImpl<MicroblogsEntry> {

	public MicroblogsEntryFinderBaseImpl() {
		setModelClass(MicroblogsEntry.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

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
		return getMicroblogsEntryPersistence().getBadColumnNames();
	}

	/**
	 * Returns the microblogs entry persistence.
	 *
	 * @return the microblogs entry persistence
	 */
	public MicroblogsEntryPersistence getMicroblogsEntryPersistence() {
		return microblogsEntryPersistence;
	}

	/**
	 * Sets the microblogs entry persistence.
	 *
	 * @param microblogsEntryPersistence the microblogs entry persistence
	 */
	public void setMicroblogsEntryPersistence(
		MicroblogsEntryPersistence microblogsEntryPersistence) {

		this.microblogsEntryPersistence = microblogsEntryPersistence;
	}

	@BeanReference(type = MicroblogsEntryPersistence.class)
	protected MicroblogsEntryPersistence microblogsEntryPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		MicroblogsEntryFinderBaseImpl.class);

}