/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Leslie Wong
 */
public class IndividualIdThreadLocal {

	public static String getIndividualId() {
		return _individualId.get();
	}

	public static void remove() {
		_individualId.remove();
	}

	public static void setIndividualId(String individualId) {
		if (_log.isDebugEnabled()) {
			_log.debug("setIndividualId" + individualId);
		}

		_individualId.set(individualId);
	}

	private static final Log _log = LogFactory.getLog(
		IndividualIdThreadLocal.class);

	private static final ThreadLocal<String> _individualId =
		new ThreadLocal<>();

}