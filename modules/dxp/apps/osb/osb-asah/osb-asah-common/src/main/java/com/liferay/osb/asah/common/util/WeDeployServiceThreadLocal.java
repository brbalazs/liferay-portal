/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.wedeploy.data.WeDeployDataService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcellus Tavares
 */
public class WeDeployServiceThreadLocal {

	public static WeDeployDataService getWeDeployDataService() {
		WeDeployDataService weDeployDataService = _weDeployDataService.get();

		if (weDeployDataService == null) {
			throw new IllegalStateException("WeDeployDataService is not set");
		}

		return weDeployDataService;
	}

	public static void remove() {
		_weDeployDataService.remove();
	}

	public static void setWeDeployDataService(
		WeDeployDataService weDeployDataService) {

		if (_log.isDebugEnabled()) {
			_log.debug("setWeDeployDataService " + weDeployDataService);
		}

		_weDeployDataService.set(weDeployDataService);
	}

	private static final Log _log = LogFactory.getLog(
		WeDeployServiceThreadLocal.class);

	private static final ThreadLocal<WeDeployDataService> _weDeployDataService =
		new ThreadLocal<>();

}