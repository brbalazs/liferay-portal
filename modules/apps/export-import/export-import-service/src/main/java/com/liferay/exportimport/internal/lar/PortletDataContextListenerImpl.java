/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.lar;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author     Raymond Augé
 * @deprecated As of Judson (7.1.x), see {@link
 *             com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleEvent}
 */
@Deprecated
public class PortletDataContextListenerImpl
	implements PortletDataContextListener {

	public PortletDataContextListenerImpl(
		PortletDataContext portletDataContext) {
	}

	@Override
	public void onAddZipEntry(String path) {
		if (_log.isInfoEnabled()) {
			_log.info("Export " + path);
		}
	}

	@Override
	public void onGetZipEntry(String path) {
		if (_log.isInfoEnabled()) {
			_log.info("Import " + path);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletDataContextListenerImpl.class);

}