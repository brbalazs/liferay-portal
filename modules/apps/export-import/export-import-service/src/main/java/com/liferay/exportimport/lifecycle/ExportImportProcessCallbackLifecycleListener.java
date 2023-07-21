/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.lifecycle;

import com.liferay.exportimport.internal.lar.ExportImportProcessCallbackUtil;
import com.liferay.exportimport.kernel.lifecycle.BaseProcessExportImportLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Serializable;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author     Daniel Kocsis
 * @deprecated As of Judson (7.1.x)
 */
@Deprecated
public class ExportImportProcessCallbackLifecycleListener
	extends BaseProcessExportImportLifecycleListener {

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	protected void onProcessFailed(List<Serializable> attributes)
		throws Exception {

		ExportImportProcessCallbackUtil.popCallbackList();
	}

	@Override
	protected void onProcessStarted(List<Serializable> attributes)
		throws Exception {

		ExportImportProcessCallbackUtil.pushCallbackList();
	}

	@Override
	protected void onProcessSucceeded(List<Serializable> attributes)
		throws Exception {

		List<Callable<?>> callables =
			ExportImportProcessCallbackUtil.popCallbackList();

		for (Callable<?> callable : callables) {
			try {
				callable.call();
			}
			catch (Exception e) {
				_log.error(
					"Unable to execute export import process callback", e);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportImportProcessCallbackLifecycleListener.class);

}