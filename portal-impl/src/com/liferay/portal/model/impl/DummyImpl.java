/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.model.impl;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Dummy;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 */
public class DummyImpl extends BaseModelImpl<Dummy> implements Dummy {

	@Override
	public Object clone() {
		return new DummyImpl();
	}

	@Override
	public int compareTo(Dummy dummy) {
		return 0;
	}

	@Override
	public Class<?> getModelClass() {
		return DummyImpl.class;
	}

	@Override
	public String getModelClassName() {
		return DummyImpl.class.getName();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return StringPool.BLANK;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return false;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return false;
	}

	@Override
	public void resetOriginalValues() {
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
	}

	@Override
	public String toXmlString() {
		return StringPool.BLANK;
	}

}