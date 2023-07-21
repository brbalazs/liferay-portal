/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.upgrade.util.BaseUpgradeColumnImpl;
import com.liferay.portal.kernel.upgrade.util.ValueMapper;
import com.liferay.portal.kernel.upgrade.util.ValueMapperFactoryUtil;

import java.sql.Types;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public class PKUpgradeColumnImpl extends BaseUpgradeColumnImpl {

	public PKUpgradeColumnImpl(String name, boolean trackValues) {
		this(name, null, trackValues);
	}

	public PKUpgradeColumnImpl(
		String name, Integer oldColumnType, boolean trackValues) {

		super(name, oldColumnType);

		_newColumnType = Integer.valueOf(Types.BIGINT);

		_trackValues = trackValues;

		if (_trackValues) {
			_valueMapper = ValueMapperFactoryUtil.getValueMapper();
		}
		else {
			_valueMapper = null;
		}
	}

	@Override
	public Integer getNewColumnType(Integer defaultType) {
		return _newColumnType;
	}

	@Override
	public Object getNewValue(Object oldValue) throws Exception {
		Long newValue = Long.valueOf(increment());

		if (_trackValues) {
			_valueMapper.mapValue(oldValue, newValue);
		}

		return newValue;
	}

	public ValueMapper getValueMapper() {
		return _valueMapper;
	}

	public boolean isTrackValues() {
		return _trackValues;
	}

	private final Integer _newColumnType;
	private final boolean _trackValues;
	private final ValueMapper _valueMapper;

}