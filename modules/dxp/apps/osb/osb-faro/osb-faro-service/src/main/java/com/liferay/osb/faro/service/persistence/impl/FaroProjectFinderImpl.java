/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.service.persistence.impl;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.impl.FaroProjectImpl;
import com.liferay.osb.faro.service.persistence.FaroProjectFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.List;

/**
 * @author Geyson Silva
 */
public class FaroProjectFinderImpl
	extends FaroProjectFinderBaseImpl implements FaroProjectFinder {

	public static final String FIND_BY_ED =
		FaroProjectFinder.class.getName() + ".findByED";

	@Override
	public List<FaroProject> findByEmailAddressDomain(
		String emailAddressDomain) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_ED);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("OSBFaro_FaroProject", FaroProjectImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(emailAddressDomain);

			return (List<FaroProject>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

}