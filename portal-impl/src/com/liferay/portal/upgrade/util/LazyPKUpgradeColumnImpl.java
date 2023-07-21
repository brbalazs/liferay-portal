/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.StagnantRowException;
import com.liferay.portal.kernel.upgrade.util.ValueMapper;

/**
 * @author Brian Wing Shun Chan
 */
public class LazyPKUpgradeColumnImpl extends PKUpgradeColumnImpl {

	public LazyPKUpgradeColumnImpl(String name) {
		super(name, true);
	}

	public LazyPKUpgradeColumnImpl(String name, Integer oldColumnType) {
		super(name, oldColumnType, true);
	}

	@Override
	public Object getNewValue(Object oldValue) throws Exception {
		ValueMapper valueMapper = getValueMapper();

		Long newValue = null;

		try {
			newValue = (Long)valueMapper.getNewValue(oldValue);
		}
		catch (StagnantRowException sre) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(sre, sre);
			}

			newValue = Long.valueOf(increment());

			valueMapper.mapValue(oldValue, newValue);
		}

		return newValue;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LazyPKUpgradeColumnImpl.class);

}