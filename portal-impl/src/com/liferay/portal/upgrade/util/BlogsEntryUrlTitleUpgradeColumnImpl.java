/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.upgrade.util.BaseUpgradeColumnImpl;
import com.liferay.portal.kernel.upgrade.util.UpgradeColumn;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class BlogsEntryUrlTitleUpgradeColumnImpl extends BaseUpgradeColumnImpl {

	public BlogsEntryUrlTitleUpgradeColumnImpl(
		UpgradeColumn entryIdColumn, UpgradeColumn titleColumn) {

		super(null);

		throw new UnsupportedOperationException();
	}

	@Override
	public Object getNewValue(Object oldValue) throws Exception {
		throw new UnsupportedOperationException();
	}

	protected String getUrlTitle(long entryId, String title) {
		throw new UnsupportedOperationException();
	}

}