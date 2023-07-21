/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.file.rank.service.persistence.impl;

import com.liferay.document.library.file.rank.model.DLFileRank;
import com.liferay.document.library.file.rank.model.impl.DLFileRankImpl;
import com.liferay.document.library.file.rank.service.persistence.DLFileRankFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.List;

/**
 * @author Alexander Chow
 */
public class DLFileRankFinderImpl
	extends DLFileRankFinderBaseImpl implements DLFileRankFinder {

	public static final String FIND_BY_STALE_RANKS =
		DLFileRankFinder.class.getName() + ".findByStaleRanks";

	public static final String FIND_BY_FOLDER_ID =
		DLFileRankFinder.class.getName() + ".findByFolderId";

	@Override
	public List<Object[]> findByStaleRanks(int count) {
		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_STALE_RANKS);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar("groupId", Type.LONG);
			q.addScalar("userId", Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(count);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<DLFileRank> findByFolderId(long folderId) {
		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_FOLDER_ID);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("DLFileRank", DLFileRankImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(folderId);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

}