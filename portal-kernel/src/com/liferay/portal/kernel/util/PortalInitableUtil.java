/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import java.util.List;
import java.util.Vector;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Paton (6.1.x), replaced by {@link PortalLifecycleUtil}
 */
@Deprecated
public class PortalInitableUtil {

	public static synchronized void flushInitables() {
		if (_portalInitables != null) {
			for (PortalInitable portalInitable : _portalInitables) {
				portalInitable.portalInit();
			}

			_portalInitables = null;
		}
	}

	public static synchronized void init(PortalInitable portalInitable) {
		if (_portalInitables == null) {
			portalInitable.portalInit();
		}
		else {
			_portalInitables.add(portalInitable);
		}
	}

	private static List<PortalInitable> _portalInitables = new Vector<>();

}