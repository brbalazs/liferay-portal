/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.service.persistence.impl;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.persistence.FragmentEntryLinkPersistence;
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
public class FragmentEntryLinkFinderBaseImpl
	extends BasePersistenceImpl<FragmentEntryLink> {

	public FragmentEntryLinkFinderBaseImpl() {
		setModelClass(FragmentEntryLink.class);

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
		return getFragmentEntryLinkPersistence().getBadColumnNames();
	}

	/**
	 * Returns the fragment entry link persistence.
	 *
	 * @return the fragment entry link persistence
	 */
	public FragmentEntryLinkPersistence getFragmentEntryLinkPersistence() {
		return fragmentEntryLinkPersistence;
	}

	/**
	 * Sets the fragment entry link persistence.
	 *
	 * @param fragmentEntryLinkPersistence the fragment entry link persistence
	 */
	public void setFragmentEntryLinkPersistence(
		FragmentEntryLinkPersistence fragmentEntryLinkPersistence) {

		this.fragmentEntryLinkPersistence = fragmentEntryLinkPersistence;
	}

	@BeanReference(type = FragmentEntryLinkPersistence.class)
	protected FragmentEntryLinkPersistence fragmentEntryLinkPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryLinkFinderBaseImpl.class);

}