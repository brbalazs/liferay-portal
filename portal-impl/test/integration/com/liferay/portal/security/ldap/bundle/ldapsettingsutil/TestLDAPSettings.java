/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.ldap.bundle.ldapsettingsutil;

import com.liferay.portal.kernel.security.ldap.LDAPSettings;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = LDAPSettings.class
)
public class TestLDAPSettings implements LDAPSettings {

	@Override
	public String getAuthSearchFilter(
			long ldapServerId, long companyId, String emailAddress,
			String screenName, String userId)
		throws Exception {

		return "(companyId=" + companyId + ")";
	}

	@Override
	public Properties getContactExpandoMappings(
			long ldapServerId, long companyId)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("ldapServerId", String.valueOf(ldapServerId));

		return properties;
	}

	@Override
	public Properties getContactMappings(long ldapServerId, long companyId)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("ldapServerId", String.valueOf(ldapServerId));

		return properties;
	}

	@Override
	public String[] getErrorPasswordHistoryKeywords(long companyId) {
		return new String[] {"history"};
	}

	@Override
	public Properties getGroupMappings(long ldapServerId, long companyId)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("ldapServerId", ldapServerId + "");

		return properties;
	}

	@Override
	public long getPreferredLDAPServerId(long companyId, String screenName) {
		if (companyId == 1) {
			return 1234567890;
		}

		return 0;
	}

	@Override
	public String getPropertyPostfix(long ldapServerId) {
		if (ldapServerId == 1) {
			return "liferay.ldap";
		}

		return "unknown";
	}

	@Override
	public Properties getUserExpandoMappings(long ldapServerId, long companyId)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("ldapServerId", ldapServerId + "");

		return properties;
	}

	@Override
	public Properties getUserMappings(long ldapServerId, long companyId)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("ldapServerId", ldapServerId + "");

		return properties;
	}

	@Override
	public boolean isExportEnabled(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isExportGroupEnabled(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isImportEnabled(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isImportOnStartup(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isPasswordPolicyEnabled(long companyId) {
		if (companyId == 1) {
			return true;
		}

		return false;
	}

}