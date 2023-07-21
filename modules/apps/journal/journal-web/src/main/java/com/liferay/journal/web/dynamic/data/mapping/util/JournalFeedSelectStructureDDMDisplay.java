/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.web.dynamic.data.mapping.util;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.service.component.annotations.Reference;

/**
 * @author     Eudaldo Alonso
 * @deprecated As of Judson (7.1.x), moved to {@link
 *             com.liferay.journal.web.internal.dynamic.data.mapping.util.JournalFeedSelectStructureDDMDisplay}
 */
@Deprecated
public class JournalFeedSelectStructureDDMDisplay extends JournalDDMDisplay {

	@Override
	public String getPortletId() {
		return JournalPortletKeys.JOURNAL + ".selectStructureFeed";
	}

	@Override
	public boolean isShowAddStructureButton() {
		return false;
	}

	@Override
	public boolean isShowConfirmSelectStructure() {
		return false;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		this.portal = portal;
	}

}