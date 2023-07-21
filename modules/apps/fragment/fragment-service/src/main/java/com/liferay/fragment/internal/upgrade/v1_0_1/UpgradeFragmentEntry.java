/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.upgrade.v1_0_1;

import com.liferay.fragment.internal.upgrade.v1_0_1.util.FragmentEntryLinkTable;
import com.liferay.fragment.internal.upgrade.v1_0_1.util.FragmentEntryTable;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Eudaldo Alonso
 */
public class UpgradeFragmentEntry extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			FragmentEntryTable.class, new AlterColumnType("css", "TEXT null"),
			new AlterColumnType("html", "TEXT null"),
			new AlterColumnType("js", "TEXT null"));

		alter(
			FragmentEntryLinkTable.class,
			new AlterColumnType("css", "TEXT null"),
			new AlterColumnType("html", "TEXT null"),
			new AlterColumnType("js", "TEXT null"),
			new AlterColumnType("editableValues", "TEXT null"));
	}

}