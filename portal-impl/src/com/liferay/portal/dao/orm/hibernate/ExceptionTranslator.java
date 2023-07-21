/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.ObjectNotFoundException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.StringBundler;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;

/**
 * @author Brian Wing Shun Chan
 */
public class ExceptionTranslator {

	public static ORMException translate(Exception e) {
		if (e instanceof org.hibernate.ObjectNotFoundException) {
			return new ObjectNotFoundException(e);
		}

		return new ORMException(e);
	}

	public static ORMException translate(
		Exception e, Session session, Object object) {

		if (e instanceof StaleObjectStateException) {
			BaseModel<?> baseModel = (BaseModel<?>)object;

			Object currentObject = session.get(
				object.getClass(), baseModel.getPrimaryKeyObj());

			BaseModel<?> currentObjectBaseModel = (BaseModel<?>)currentObject;

			currentObject = currentObjectBaseModel.clone();

			object = baseModel.clone();

			JSONSerializer jsonSerializer =
				JSONFactoryUtil.createJSONSerializer();

			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			try {
				PermissionThreadLocal.setPermissionChecker(null);

				return new ORMException(
					StringBundler.concat(
						jsonSerializer.serialize(object),
						" is stale in comparison to ",
						jsonSerializer.serialize(currentObject)),
					e);
			}
			finally {
				PermissionThreadLocal.setPermissionChecker(permissionChecker);
			}
		}

		return new ORMException(e);
	}

}