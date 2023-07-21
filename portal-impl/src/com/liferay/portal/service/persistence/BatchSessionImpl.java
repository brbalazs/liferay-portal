/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.persistence;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BatchSession;

/**
 * @author     Raymond Augé
 * @author     Brian Wing Shun Chan
 * @deprecated As of Newton (6.2.x), see LPS-30598
 */
@Deprecated
public class BatchSessionImpl implements BatchSession {

	@Override
	public void delete(Session session, BaseModel<?> model)
		throws ORMException {

		if (!session.contains(model)) {
			model = (BaseModel<?>)session.get(
				model.getClass(), model.getPrimaryKeyObj());
		}

		if (model != null) {
			session.delete(model);
		}
	}

	@Override
	public boolean isEnabled() {
		return _enabled.get();
	}

	@Override
	public void setEnabled(boolean enabled) {
		_enabled.set(enabled);
	}

	@Override
	public void update(Session session, BaseModel<?> model, boolean merge)
		throws ORMException {

		if (model.isNew()) {
			session.save(model);

			model.setNew(false);
		}
		else {
			session.merge(model);
		}
	}

	private static final ThreadLocal<Boolean> _enabled =
		new CentralizedThreadLocal<>(
			BatchSessionImpl.class + "._enabled", () -> Boolean.FALSE, false);

}