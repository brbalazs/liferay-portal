/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.bookmarks.service.persistence.impl;

import com.liferay.bookmarks.model.BookmarksEntry;
import com.liferay.bookmarks.service.persistence.BookmarksEntryPersistence;
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
 * @deprecated As of Judson (7.1.x), with no direct replacement
 * @generated
 */
@Deprecated
public class BookmarksEntryFinderBaseImpl
	extends BasePersistenceImpl<BookmarksEntry> {

	public BookmarksEntryFinderBaseImpl() {
		setModelClass(BookmarksEntry.class);

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
		return getBookmarksEntryPersistence().getBadColumnNames();
	}

	/**
	 * Returns the bookmarks entry persistence.
	 *
	 * @return the bookmarks entry persistence
	 */
	public BookmarksEntryPersistence getBookmarksEntryPersistence() {
		return bookmarksEntryPersistence;
	}

	/**
	 * Sets the bookmarks entry persistence.
	 *
	 * @param bookmarksEntryPersistence the bookmarks entry persistence
	 */
	public void setBookmarksEntryPersistence(
		BookmarksEntryPersistence bookmarksEntryPersistence) {

		this.bookmarksEntryPersistence = bookmarksEntryPersistence;
	}

	@BeanReference(type = BookmarksEntryPersistence.class)
	protected BookmarksEntryPersistence bookmarksEntryPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		BookmarksEntryFinderBaseImpl.class);

}