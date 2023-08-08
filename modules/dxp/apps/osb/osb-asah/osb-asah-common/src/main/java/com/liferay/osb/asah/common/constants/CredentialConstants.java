/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Inácio Nery
 */
public class CredentialConstants {

	public static final String POSTGRESQL_DB;

	public static final String POSTGRESQL_PASSWORD;

	public static final String POSTGRESQL_USER;

	private static String _getCredential(
		String credentialKey, String defaultValue) {

		String credential = defaultValue;

		String overrideCredential = System.getenv(credentialKey);

		if (overrideCredential != null) {
			credential = overrideCredential;
		}

		if (_log.isInfoEnabled()) {
			if (credential != null) {
				_log.info(credentialKey + " found");
			}
			else {
				_log.info(credentialKey + " not found");
			}
		}

		return credential;
	}

	private static final Log _log = LogFactory.getLog(
		CredentialConstants.class);

	static {
		POSTGRESQL_DB = _getCredential("POSTGRESQL_DB", "osbasah");
		POSTGRESQL_PASSWORD = _getCredential("POSTGRESQL_PASSWORD", "password");
		POSTGRESQL_USER = _getCredential("POSTGRESQL_USER", "postgres");
	}

}