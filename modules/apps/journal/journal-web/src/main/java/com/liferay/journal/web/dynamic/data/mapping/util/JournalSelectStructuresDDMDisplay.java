/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.web.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.service.component.annotations.Reference;

/**
 * @author     Eudaldo Alonso
 * @deprecated As of Judson (7.1.x), moved to {@link
 *             com.liferay.journal.web.internal.dynamic.data.mapping.util.JournalSelectStructuresDDMDisplay}
 */
@Deprecated
public class JournalSelectStructuresDDMDisplay extends JournalDDMDisplay {

	@Override
	public String getPortletId() {
		return JournalPortletKeys.JOURNAL + ".selectStructure";
	}

	@Override
	public boolean isEnableSelectStructureLink(
		DDMStructure structure, long classPK) {

		if (structure.getStructureId() == classPK) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isShowAddStructureButton() {
		return false;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		this.portal = portal;
	}

}