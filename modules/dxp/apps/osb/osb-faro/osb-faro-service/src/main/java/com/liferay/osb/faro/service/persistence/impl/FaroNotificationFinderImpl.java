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
import com.liferay.osb.faro.model.impl.FaroNotificationImpl;
import com.liferay.osb.faro.service.persistence.FaroNotificationFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.List;

/**
 * @author Geyson Silva
 */
public class FaroNotificationFinderImpl
	extends FaroNotificationFinderBaseImpl implements FaroNotificationFinder {

	public static final String FIND_DISMISSED =
		FaroNotificationFinder.class.getName() + ".findDismissed";

	public static final String FIND_LAST_30_DAYS =
		FaroNotificationFinder.class.getName() + ".findLast30Days";

	@Override
	public List<FaroNotification> findDismissedNotifications() {
		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_DISMISSED);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("OSBFaro_FaroNotification", FaroNotificationImpl.class);

			return (List<FaroNotification>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<FaroNotification> findLast30Days(long groupId, long userId) {
		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_LAST_30_DAYS);

			if (!_isGroupAdmin(groupId)) {
				sql = StringUtil.removeSubstring(sql, _WORKSPACE_SQL);
			}

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("OSBFaro_FaroNotification", FaroNotificationImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(userId);

			return (List<FaroNotification>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private boolean _isGroupAdmin(long groupId) {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		return permissionChecker.isGroupAdmin(groupId);
	}

	private static final String _WORKSPACE_SQL =
		"OR (OSBFaro_FaroNotification.`scope` = 'WORKSPACE')";

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

}