/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.repository.util;

import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author     Adolfo Pérez
 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
 */
@Deprecated
public class RepositoryUserUtil {

	/**
	 * See {@link BaseServiceImpl#getUserId()}
	 *
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public static long getUserId() throws PrincipalException {
		String name = PrincipalThreadLocal.getName();

		if (Validator.isNull(name)) {
			throw new PrincipalException("Principal is null");
		}

		for (int i = 0; i < BaseServiceImpl.ANONYMOUS_NAMES.length; i++) {
			if (StringUtil.equalsIgnoreCase(
					name, BaseServiceImpl.ANONYMOUS_NAMES[i])) {

				throw new PrincipalException(
					"Principal cannot be " +
						BaseServiceImpl.ANONYMOUS_NAMES[i]);
			}
		}

		return GetterUtil.getLong(name);
	}

}