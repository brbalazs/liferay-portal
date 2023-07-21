/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.service.persistence;

import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.model.BaseModel;

/**
 * @author     Brian Wing Shun Chan
 * @see        BatchSession
 * @deprecated As of Wilberforce (7.0.x), see LPS-30598
 */
@Deprecated
public class BatchSessionUtil {

	public static void delete(Session session, BaseModel<?> model)
		throws ORMException {

		getBatchSession().delete(session, model);
	}

	public static BatchSession getBatchSession() {
		return _batchSession;
	}

	public static boolean isEnabled() {
		return getBatchSession().isEnabled();
	}

	public static void setEnabled(boolean enabled) {
		getBatchSession().setEnabled(enabled);
	}

	public static void update(
			Session session, BaseModel<?> model, boolean merge)
		throws ORMException {

		getBatchSession().update(session, model, merge);
	}

	public void setBatchSession(BatchSession batchSession) {
		_batchSession = batchSession;
	}

	private static BatchSession _batchSession;

}