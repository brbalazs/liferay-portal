/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.upgrade.util.ValueMapper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Chow
 * @author Brian Wing Shun Chan
 */
public class DatabaseValueMapper implements ValueMapper {

	public DatabaseValueMapper() {

		// Delete

	}

	@Override
	public void appendException(Object exception) {

		// Exceptions

	}

	@Override
	public Object getNewValue(Object oldValue) throws Exception {

		// Select

		return null;
	}

	@Override
	public Iterator<Object> iterator() throws Exception {
		List<Object> list = Collections.emptyList();

		return list.iterator();
	}

	@Override
	public void mapValue(Object oldValue, Object newValue) throws Exception {

		// Insert

	}

	@Override
	public int size() throws Exception {
		return 0;
	}

}