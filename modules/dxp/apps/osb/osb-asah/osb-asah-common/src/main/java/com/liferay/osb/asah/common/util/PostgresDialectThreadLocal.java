/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcos Martins
 */
public class PostgresDialectThreadLocal {

	public static boolean isPostgresDialect() {
		if (_postgresDialect.get() == null) {
			return false;
		}

		return _postgresDialect.get();
	}

	public static void remove() {
		_postgresDialect.remove();
	}

	public static void setPostgresDialect(Boolean postgresDialect) {
		if (_log.isDebugEnabled()) {
			_log.debug("setPostgresDialect" + postgresDialect);
		}

		_postgresDialect.set(postgresDialect);
	}

	private static final Log _log = LogFactory.getLog(
		IndividualIdThreadLocal.class);

	private static final ThreadLocal<Boolean> _postgresDialect =
		new ThreadLocal<>();

}